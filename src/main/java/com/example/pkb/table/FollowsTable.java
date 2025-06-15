package com.example.pkb.table;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowsTable {
    private static FollowsTable instance;

    private final Map<Integer, Integer> followsMap = new HashMap<>();

    public static FollowsTable getInstance() {
        if (instance == null) instance = new FollowsTable();
        return instance;
    }

    public void addFollows(int stmt1, int stmt2) {
        followsMap.put(stmt1, stmt2);
    }

    public boolean isFollows(int stmt1, int stmt2) {
        return followsMap.getOrDefault(stmt1, -1) == stmt2;
    }

    public Integer getFollows(int stmt1) {
        return followsMap.get(stmt1);
    }

    public Integer getFollowedBy(int stmt2) {
        for (Map.Entry<Integer, Integer> entry : followsMap.entrySet()) {
            if (entry.getValue() == stmt2) return entry.getKey();
        }
        return null;
    }

    public Set<Integer> getAllFollowKeys() {
        return followsMap.keySet();
    }

    public Set<Integer> getAllFollowValues() {
        return new HashSet<>(followsMap.values());
    }
}
