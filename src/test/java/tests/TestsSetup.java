package tests;

import cassandra.CassandraConnector;
import cassandra.repository.KeySpaceRepository;
import com.datastax.driver.core.Session;
import org.junit.*;

public abstract class TestsSetup {

    static private final CassandraConnector client = new CassandraConnector();
    static protected KeySpaceRepository schemaRepository;

    static protected Session session;

    @BeforeClass
    static public void beforeAll() {
        client.connect();
        session = client.getSession();
        schemaRepository = new KeySpaceRepository(session);
    }

    @AfterClass
    static public void afterAll() {
        client.close();
    }

}
