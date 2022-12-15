package cassandra.repository;

import com.datastax.driver.core.Session;

import static cassandra.repository.KeySpaceRepository.KEYSPACE_NAME;

public class BookRepository {

    private static final String TABLE_NAME = KEYSPACE_NAME + "." + "books";

    final private Session session;

    public BookRepository(Session session) {
        this.session = session;
    }

    public void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                "id uuid PRIMARY KEY, " +
                "title text," +
                "subject text);";
        session.execute(query);
    }

    public void alterTable(String columnName, String columnType) {
        String query = "ALTER TABLE " + TABLE_NAME + " ADD " + columnName + " " + columnType + ";";
        session.execute(query);
    }

    public void dropTable() {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        session.execute(query);
    }

}
