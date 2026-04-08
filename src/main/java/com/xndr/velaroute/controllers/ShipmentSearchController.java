package com.xndr.velaroute.controllers;

import com.xndr.velaroute.services.ShipmentTrie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@CrossOrigin(origins = "*") // Allows the future frontend to talk to this API
public class ShipmentSearchController {

    private final ShipmentTrie shipmentTrie;

    // Dependency Injection: Spring gives us the Trie we built yesterday
    public ShipmentSearchController(ShipmentTrie shipmentTrie) {
        this.shipmentTrie = shipmentTrie;
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getSuggestions(@RequestParam String prefix) {
        // We use the Trie search instead of the Database!
        List<String> matches = shipmentTrie.search(prefix);
        return ResponseEntity.ok(matches);
    }
}
