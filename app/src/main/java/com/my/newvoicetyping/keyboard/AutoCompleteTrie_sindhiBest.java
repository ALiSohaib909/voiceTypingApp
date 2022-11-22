package com.my.newvoicetyping.keyboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AutoCompleteTrie_sindhiBest {
    protected final Map<Character, AutoCompleteTrie_sindhiBest> children;
    protected String value;
    protected boolean terminal = false;

    public AutoCompleteTrie_sindhiBest() {
        this(null);
    }

    private AutoCompleteTrie_sindhiBest(String value) {
        this.value = value;
        children = new HashMap<>();
    }

    protected void add(char c) {
        String val;
        if (this.value == null) {
            val = Character.toString(c);
        } else {
            val = this.value + c;
        }
        children.put(c, new AutoCompleteTrie_sindhiBest(val));
    }

    public void insert(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Cannot add null to a Trie");
        }
        AutoCompleteTrie_sindhiBest node = this;
        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) {
                node.add(c);
            }
            node = node.children.get(c);
        }
        node.terminal = true;
    }

    public String find(String word) {
        AutoCompleteTrie_sindhiBest node = this;
        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return "";
            }
            node = node.children.get(c);
        }
        return node.value;
    }

    public Collection<String> autoComplete(String prefix) {
        AutoCompleteTrie_sindhiBest node = this;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return Collections.emptyList();
            }
            node = node.children.get(c);
        }
        return node.allPrefixes();
    }

    protected Collection<String> allPrefixes() {
        List<String> results = new ArrayList<>();
        if (this.terminal) {
            results.add(this.value);
        }
        for (Entry<Character, AutoCompleteTrie_sindhiBest> entry : children.entrySet()) {
            AutoCompleteTrie_sindhiBest child = entry.getValue();
            Collection<String> childPrefixes = child.allPrefixes();
            results.addAll(childPrefixes);
        }
        return results;
    }
}