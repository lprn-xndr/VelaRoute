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

}
