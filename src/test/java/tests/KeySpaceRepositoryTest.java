package tests;

import com.datastax.driver.core.ResultSet;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static cassandra.repository.KeySpaceRepository.KEYSPACE_NAME;

public class KeySpaceRepositoryTest extends TestsSetup {

    @Test
    public void whenCreating_A_Keyspace_then_Keyspace_is_Created() {
        keySpaceRepository.createKeyspace(KEYSPACE_NAME, "SimpleStrategy", 1);

        ResultSet result = session.execute("SELECT * FROM system_schema.keyspaces");

        List<String> matchedKeyspaces = result.all()
                .stream()
                .filter(r -> r.getString(0).equals(KEYSPACE_NAME.toLowerCase()))
                .map(r -> r.getString(0))
                .toList();

        Assert.assertEquals(matchedKeyspaces.size(), 1);
        Assert.assertEquals(matchedKeyspaces.get(0), KEYSPACE_NAME.toLowerCase());
    }
}
