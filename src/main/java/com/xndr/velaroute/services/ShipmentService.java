package com.xndr.velaroute.services;

import com.xndr.velaroute.models.ResourceNotFoundException;
import com.xndr.velaroute.models.Shipment;
import com.xndr.velaroute.repositories.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

import static com.xndr.velaroute.specifications.ShipmentSpecifications.*;

@Service
public class ShipmentService {

    @Autowired // This injects the repository so we can use it
    private final ShipmentRepository shipmentRepository;
    private final ShipmentTrie shipmentTrie; // 1. Add the Trie field

    public List<Shipment> searchShipments(String status, String destination) {
        // We start with an "Empty" search (find all)
        Specification<Shipment> spec = (root, query, cb) -> cb.conjunction();

        // If the user provided a status, add the "hasStatus" rule
        if (status != null && !status.isEmpty()) {
            spec = spec.and(hasStatus(status));
        }

        // If the user provided a destination, add the "hasDestination" rule
        if (destination != null && !destination.isEmpty()) {
            spec = spec.and(hasDestination(destination));
        }

        // The Repository handles the SQL generation automatically
        return shipmentRepository.findAll(spec);
    }

    // 2. Update the constructor to include the Trie
    public ShipmentService(ShipmentRepository shipmentRepository, ShipmentTrie shipmentTrie) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentTrie = shipmentTrie;
    }

    public Shipment createShipment(Shipment shipment) {
        // 1. Logic for Smart Prefixing
        String originInfo = shipment.getOrigin().toUpperCase();
        String prefix = "VELA-GEN-"; // Our fallback

        if (originInfo.contains("FL") || originInfo.contains("FLORIDA")) {
            prefix = "VELA-FL-";
        } else if (originInfo.contains("TX") || originInfo.contains("TEXAS")) {
            prefix = "VELA-TX-";
        }

        // 2. Generate 5-digit random number
        //We use %05d to ensure it's always 5 digits (e.g., 00123)
        Random random = new Random();
        String randomDigits = String.format("%5d", random.nextInt(100000));

        // 3. Set the tracking number before saving
        shipment.setTrackingNumber(prefix + randomDigits);

        // You could add logic here: e.g., check is tracking number is valid
        // Standard Database Save
        Shipment saved = shipmentRepository.save(shipment);

        // 3. The 'Sync' Hook: Push the new tracking number into the Trie
        shipmentTrie.insert(saved.getTrackingNumber());
        return saved;
    }

    public Shipment getShipmentById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
    }

    public void deleteByTracking(String trackingNumber) {
        // 1. Find the shipment. If it doesn't exist, stop here.
        Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new RuntimeException("Shipment not found: " + trackingNumber));

        // 2. Remove from the database
        shipmentRepository.delete(shipment);

        // 3. Remove from the Trie
        shipmentTrie.delete(trackingNumber);
    }

    public Shipment updateShipment(String trackingNumber, Shipment updatedDetails) {
        // 1. Find the existing shipment
        Shipment existingShipment = shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        // 2a. If the tracking number is changing (rare, but possible), we must remove the OLD one from the Trie
//        shipmentTrie.delete(existingShipment.getTrackingNumber());

        // 2b. Store the old tracking number to remove from Trie later
        String oldTrackingNumber = existingShipment.getTrackingNumber();

        // 3a. Update the fields
//        existingShipment.setOrigin(updatedDetails.getOrigin());
//        existingShipment.setDestination(updatedDetails.getDestination());
//        existingShipment.setStatus(updatedDetails.getStatus());
        // ... update other fields as needed

        // 3b. DEFENSIVE UPDATES (This stops the nulling!)
        // 3c. Adding handler for Origin/Prefix change
        if (updatedDetails.getOrigin() != null && !updatedDetails.getOrigin().equalsIgnoreCase(existingShipment.getOrigin())) {
            existingShipment.setOrigin(updatedDetails.getOrigin());

            // Extracts new prefix (assuming format "City, ST")
            String newPrefix = extractStatePrefix(updatedDetails.getOrigin());

            // Replace the old prefix on the tracking number
            // Example: VELA-TX-12345 -> vela-fl-12345
            String currentTrackingNumber = existingShipment.getTrackingNumber();
            String newTrackingNumber = currentTrackingNumber.replaceFirst("(?i)Vela-[A-Z]{2}", "VELA-" + newPrefix);
            existingShipment.setTrackingNumber(newTrackingNumber.toUpperCase());
        }
        if (updatedDetails.getDestination() != null) {
            existingShipment.setDestination(updatedDetails.getDestination());
        }
        if (updatedDetails.getStatus() != null) {
            existingShipment.setStatus(updatedDetails.getStatus());
        }
        // ... update other fields as needed

        // 4. Save to database
        Shipment savedShipment = shipmentRepository.save(existingShipment);

        // 5. Add the (potentially new) tracking number back to the Trie
        shipmentTrie.insert(savedShipment.getTrackingNumber());

        return savedShipment;
    }
    // --- HELPER METHODS ---

    private String extractStatePrefix(String location) {
        try {
            // Split "Miami, FL" by the comma
            String[] parts = location.split(",");
            if (parts.length < 2) return "XX"; // Fallback if no comma

            // Trim whitespace and grab the first 2 letters (e.g., "FL")
            return parts[1].trim().substring(0, 2).toUpperCase();
        } catch (Exception e) {
            return "XX"; // Fallback is string is too short or weirdly formatted
        }
    }
}
