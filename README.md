#### Minecraft UUID Library

Information coming "soon".

**Sample Usage**
```java
import com.turt2live.uuid.PlayerRecord;
import com.turt2live.uuid.ServiceProvider;
import com.turt2live.uuid.CachingServiceProvider;
import com.turt2live.uuid.turt2live.v2.ApiV2Service;
import java.util.UUID;
// ...

ServiceProvider uuidProvider = new CachingServiceProvider(new ApiV2Service());

// UUID Lookup (uuid -> name)
// ---------------------------------------------
UUID uuid = /* ... */; // Get UUID however you please

PlayerRecord record = uuidProvider.doLookup(uuid);
if(record == null) { /* ... */ } // Check to see if the lookup was OK (null = failed)

String playerName = record.getName(); // May also return null

// Name Lookup (name -> uuid)
// ---------------------------------------------
PlayerRecord record = uuidProvider.doLookup("turt2live");
if(record == null) { /* ... */ } // Check to see if the lookup was OK (null = failed)

UUID playerUUID = record.getUuid(); // May also return null
```