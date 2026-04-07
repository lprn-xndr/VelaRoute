package com.xndr.velaroute.models;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    // Each node points to its possible next characters
    private Map<Character, TrieNode> children = new HashMap<>();
    private boolean isEndOfWord;

    public Map<Character, TrieNode> getChildren() { return children; }
    public boolean isEndOfWord() { return isEndOfWord; }
    public void setEndOfWord(boolean endOfWord) { isEndOfWord = endOfWord; }
}
