package com.xndr.velaroute.services;

import com.xndr.velaroute.repositories.ShipmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TrieDataSeed implements CommandLineRunner {

    private final ShipmentRepository repository;
    private final ShipmentTrie shipmentTrie;

    public TrieDataSeed(ShipmentRepository repository, ShipmentTrie shipmentTrie) {
        this.repository = repository;
        this.shipmentTrie = shipmentTrie;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🌱 Loading Trie with tracking numbers from Database...");

        // 1. Get every shipment from Postgres
        // 2. Insert each tracking number into out Trie
        repository.findAll().forEach(shipment -> {
            shipmentTrie.insert(shipment.getTrackingNumber());
        });

        System.out.println("✅ Trie Sync Complete!");
        // Temporary test to make sure Trie is working properly.
        System.out.println("Trie Test (Searching 'VELA'): " + shipmentTrie.search("VELA"));
    }
}