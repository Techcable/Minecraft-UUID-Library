#### Minecraft UUID Library

Information coming "soon".

**Sample Usage**
```java
import com.turt2live.uuid.PlayerRecord;
import com.turt2live.uuid.ServiceProvider;
import com.turt2live.uuid.CachingServiceProvider;
import com.turt2live.uuid.turt2live.v2.ApiV2Service;
// ...

ServiceProvider uuidProvider = new CachingServiceProvider(new ApiV2Service());

// UUID Lookup (uuid -> name)
PlayerRecord record = uuidProvider.doLookup(uuid); // Where uuid is a UUID object

// Name Lookup (name -> uuid)
PlayerRecord record = uuidProvider.doLookup("turt2live");
```