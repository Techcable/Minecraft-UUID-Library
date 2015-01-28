package com.turt2live.uuid.mojang;

import java.nio.charset.Charset;
import java.util.UUID;

import com.turt2live.uuid.PlayerRecord;

public class MojangPlayerRecord implements PlayerRecord {
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    
    public MojangPlayerRecord(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
    
    private final UUID id;
    private final String name;
    
    @Override
    public UUID getUuid() {
        return id;
    }
    
    @Override
    public UUID getOfflineUuid() {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + getName()).getBytes(UTF_8));
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean isCached() {
        return false;
    }

    //Isn't cached
    public long getExpirationTime() { 
        return 0;
    }

    public long getTimeLeft() {
        return 0;
    }
}