package com.xndr.velaroute.controllers;

import com.xndr.velaroute.models.Shipment;
import com.xndr.velaroute.services.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Tells Spring: "Send data back as JSON, not HTML"
@RequestMapping("/api/shipments") // The base URL for all routes in this file
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    // GET: http://localhost:8080/api/shipments
    @GetMapping
    public List<Shipment> getAll() {
        return shipmentService.getAllShipments();
    }

    // POST: http://localhost:8080/api/shipments
    @PostMapping
    public Shipment create(@RequestBody Shipment shipment) {
        return shipmentService.createShipment(shipment);
    }
}
