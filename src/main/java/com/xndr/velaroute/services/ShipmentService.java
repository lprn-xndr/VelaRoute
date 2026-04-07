package com.xndr.velaroute.services;

import com.xndr.velaroute.models.ResourceNotFoundException;
import com.xndr.velaroute.models.Shipment;
import com.xndr.velaroute.repositories.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.xndr.velaroute.specifications.ShipmentSpecifications.*;

@Service
public class ShipmentService {

    @Autowired // This injects the repository so we can use it
    private ShipmentRepository shipmentRepository;

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

    public Shipment createShipment(Shipment shipment) {
        // You could add logic here: e.g., check is tracking number is valid
        return shipmentRepository.save(shipment);
    }

    public Shipment getShipmentById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
    }
}
