package com.example.pkb.table;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowsTable {
    private static FollowsTable instance;

    private final Map<Integer, Integer> followsMap = new HashMap<>();
    private final Map<Integer, Set<Integer>> followsStarCache = new HashMap<>();

    public static FollowsTable getInstance() {
        if (instance == null) instance = new FollowsTable();
        return instance;
    }

    public void addFollows(int stmt1, int stmt2) {
        followsMap.put(stmt1, stmt2);
        // Invalidate cache when adding new relationships
        followsStarCache.clear();
    }

    public boolean isFollows(int stmt1, int stmt2) {
        return followsMap.getOrDefault(stmt1, -1) == stmt2;
    }

    public boolean isFollowsStar(int stmt1, int stmt2) {
        Set<Integer> allFollowing = getAllFollowing(stmt1);
        return allFollowing != null && allFollowing.contains(stmt2);
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

    public Set<Integer> getAllFollowing(int stmt) {
        // Check cache first
        if (followsStarCache.containsKey(stmt)) {
            return followsStarCache.get(stmt);
        }

        Set<Integer> result = new HashSet<>();
        Integer current = stmt;

        // Follow the chain until no more follows relationships
        while ((current = followsMap.get(current)) != null) {
            // Prevent infinite loops (though SIMPLE doesn't allow cycles)
            if (result.contains(current)) break;
            result.add(current);
        }

        // Cache the result
        followsStarCache.put(stmt, result);
        return result;
    }

    public Set<Integer> getAllFollowedByStar(int stmt) {
        Set<Integer> result = new HashSet<>();
        Integer current = stmt;

        // Follow the chain backwards until no more followedBy relationships
        while ((current = getFollowedBy(current)) != null) {
            // Prevent infinite loops
            if (result.contains(current)) break;
            result.add(current);
        }

        return result;
    }

    public Map<Integer, Integer> getAllFollowsRelationships() {
        return Collections.unmodifiableMap(followsMap);
    }

    public Map<Integer, Set<Integer>> getAllFollowsStarRelationships() {
        // Build complete cache if not already built
        if (followsStarCache.size() != followsMap.size()) {
            for (Integer stmt : followsMap.keySet()) {
                if (!followsStarCache.containsKey(stmt)) {
                    getAllFollowing(stmt);
                }
            }
        }
        return Collections.unmodifiableMap(followsStarCache);
    }
}