package com.xndr.velaroute.services;

import com.xndr.velaroute.models.ResourceNotFoundException;
import com.xndr.velaroute.models.Shipment;
import com.xndr.velaroute.repositories.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentService {

    @Autowired // This injects the repository so we can use it
    private ShipmentRepository shipmentRepository;

    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
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
