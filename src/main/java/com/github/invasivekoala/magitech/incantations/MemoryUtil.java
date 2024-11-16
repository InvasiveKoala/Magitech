package com.github.invasivekoala.magitech.incantations;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.UUID;

public class MemoryUtil {

    // The "stack" of this mod
    // Not saved on restart bc I don't care enough for that!!!
    public static HashMap<UUID, Deque<Object>> MEMORY_MAP = new HashMap<>();

    public static Deque<Object> getOrCreateMemory(UUID uuid){
        if (!MEMORY_MAP.containsKey(uuid)) MEMORY_MAP.put(uuid, new ArrayDeque<>());
        return MEMORY_MAP.get(uuid);
    }

}
