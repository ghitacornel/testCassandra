package tests;

import cassandra.CassandraConnector;
import cassandra.repository.KeySpaceRepository;
import com.datastax.driver.core.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import static cassandra.repository.KeySpaceRepository.KEYSPACE_NAME;

public abstract class TestsSetup {

    static private final CassandraConnector client = new CassandraConnector();
    static protected KeySpaceRepository keySpaceRepository;

    static protected Session session;

    @BeforeClass
    static public void beforeAll() {
        client.connect();
        session = client.getSession();
        keySpaceRepository = new KeySpaceRepository(session);
        keySpaceRepository.createKeyspace(KEYSPACE_NAME, "SimpleStrategy", 1);
    }

    @AfterClass
    static public void afterAll() {
        client.close();
    }

}
