package com.example.pkb.table;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParentTable {
    private static ParentTable instance;

    private final Map<Integer, List<Integer>> parentToChildren = new HashMap<>();
    private final Map<Integer, Integer> childToParent = new HashMap<>();

    public static ParentTable getInstance() {
        if (instance == null) instance = new ParentTable();
        return instance;
    }

    public void addParent(int parent, int child) {
        parentToChildren.computeIfAbsent(parent, k -> new ArrayList<>()).add(child);
        childToParent.put(child, parent);
        System.out.println("PARENT: " + parent + " -> " + child);
    }

    public boolean isParent(int parent, int child) {
        return parentToChildren.getOrDefault(parent, List.of()).contains(child);
    }

    public List<Integer> getChildren(int parent) {
        return parentToChildren.getOrDefault(parent, List.of());
    }

    public Integer getParent(int child) {
        return childToParent.get(child);
    }

    public Set<Integer> getAllParents() {
        return parentToChildren.keySet();
    }
}
