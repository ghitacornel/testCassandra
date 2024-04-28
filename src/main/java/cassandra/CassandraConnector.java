package cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;


public class CassandraConnector {

    private Cluster cluster;
    private Session session;

    public void connect() {
        String node = "localhost";
        int port = 9042;
        Cluster.Builder b = Cluster.builder().addContactPoint(node);
        b.withPort(port);
        cluster = b.build();
        session = cluster.connect();
    }

    public void close() {
        session.close();
        cluster.close();
    }

    public Session getSession() {
        return this.session;
    }
}