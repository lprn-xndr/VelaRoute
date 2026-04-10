package com.xndr.velaroute.controllers;

import com.xndr.velaroute.models.ResourceNotFoundException;
import com.xndr.velaroute.models.Shipment;
import com.xndr.velaroute.services.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Tells Spring: "Send data back as JSON, not HTML"
@RequestMapping("/api/shipments") // The base URL for all routes in this file
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    // GET ALL: http://localhost:8080/api/shipments
    @GetMapping
    public List<Shipment> getAll() {
        return shipmentService.searchShipments(null, null);
    }

    // GET BY FILTER: http://localhost:8080/api/shipments/filter/{prefix}
    @GetMapping("/filter")
    public List<Shipment> search(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String destination) {
        // We pass these straight to the Service
        return shipmentService.searchShipments(status, destination);
    }

    // POST: http://localhost:8080/api/shipments
    @PostMapping
    public ResponseEntity<Shipment> createShipment(@Valid @RequestBody Shipment shipment) {
        // Adding @Valid triggers those @NotBlank and @Size rules we wrote in the Entity!
        return ResponseEntity.ok(shipmentService.createShipment(shipment));
    }

    // GET BY ID: http://localhost:8080/api/shipments/:id
    @GetMapping("/{id}")
    public Shipment getById(@PathVariable Long id) {
        return shipmentService.getShipmentById(id);
    }

    // DELETE BY TRACKINGNUMBER
    @DeleteMapping("/tracking/{trackingNumber}")
    public ResponseEntity<Void> deleteShipment(@PathVariable String trackingNumber) {
        shipmentService.deleteByTracking(trackingNumber);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/tracking/{trackingNumber}")
    public ResponseEntity<Shipment> updateShipment(@PathVariable String trackingNumber, @RequestBody Shipment updatedDetails) {
        Shipment shipment = shipmentService.updateShipment(trackingNumber, updatedDetails);
        return ResponseEntity.ok(shipment);
    }
}
