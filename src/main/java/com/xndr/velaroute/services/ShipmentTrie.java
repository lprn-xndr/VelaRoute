package com.xndr.velaroute.services;

import com.xndr.velaroute.models.TrieNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ShipmentTrie {
    private final TrieNode root = new TrieNode();

    // 1. Insert a tracking number (Normalized to Uppercase)
    public void insert(String trackingNumber) {
        TrieNode current = root;
        for (char l : trackingNumber.toUpperCase().toCharArray()) {
            current = current.getChildren().computeIfAbsent(l, c -> new TrieNode());
        }
        current.setEndOfWord(true);
    }

    // 2. Search for all tracking numbers starting with a prefix
    public List<String> search(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode current = root;

        // Navigate to the end of the prefix
        for (char l : prefix.toUpperCase().toCharArray()) {
            current = current.getChildren().get(l);
            if (current == null) return results; // Prefix doesn't exist
        }

        // From that point, find all possible completions
        findAllChildWords(current, prefix.toUpperCase(), results);
        return results;
    }

    private void findAllChildWords(TrieNode node, String currentWord, List<String> results) {
        if (node.isEndOfWord()) {
            results.add(currentWord);
        }
        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            findAllChildWords(entry.getValue(), currentWord + entry.getKey(), results);
        }
    }

    public void delete(String trackingNumber) {
        TrieNode current = root;
        for (char ch : trackingNumber.toCharArray()) {
            TrieNode node = current.getChildren().get(ch);
            if (node == null) return; // Not found, nothing to do
            current = node;
        }
        current.setEndOfWord(false); // It;s no longer a valid searchable ID
    }
}
