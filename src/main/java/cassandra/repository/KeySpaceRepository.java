package cassandra.repository;

import com.datastax.driver.core.Session;

public class KeySpaceRepository {

    public static final String KEYSPACE_NAME = "library";

    final private Session session;

    public KeySpaceRepository(Session session) {
        this.session = session;
    }

    public void createKeyspace(String keyspaceName, String replicationStrategy, int replicationFactor) {
        String query = "CREATE KEYSPACE IF NOT EXISTS " +
                keyspaceName + " WITH replication = {" +
                "'class':'" + replicationStrategy +
                "','replication_factor':" + replicationFactor +
                "};";
        session.execute(query);
    }

}
