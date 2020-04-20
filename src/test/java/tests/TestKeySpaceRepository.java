package tests;

import com.datastax.driver.core.ResultSet;
import org.junit.*;

import java.util.List;
import java.util.stream.Collectors;

import static cassandra.repository.KeySpaceRepository.KEYSPACE_NAME;

public class TestKeySpaceRepository extends TestsSetup {

    @Test
    public void whenCreatingAKeyspace_thenCreated() {
        schemaRepository.createKeyspace(KEYSPACE_NAME, "SimpleStrategy", 1);

        ResultSet result = session.execute("SELECT * FROM system_schema.keyspaces;");

        List<String> matchedKeyspaces = result.all()
                .stream()
                .filter(r -> r.getString(0).equals(KEYSPACE_NAME.toLowerCase()))
                .map(r -> r.getString(0))
                .collect(Collectors.toList());

        Assert.assertEquals(matchedKeyspaces.size(), 1);
        Assert.assertEquals(matchedKeyspaces.get(0), KEYSPACE_NAME.toLowerCase());
    }
}
