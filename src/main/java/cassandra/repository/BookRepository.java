package cassandra.repository;

import cassandra.model.Book;
import com.datastax.driver.core.Session;

import java.util.List;

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

    public void insert(Book book) {
        String query = "INSERT INTO " +
                TABLE_NAME + "(id, title, subject) " +
                "VALUES ("
                + book.getId()
                + ", '" + book.getTitle() + "'"
                + ", '" + book.getSubject() + "'"
                + ");";
        session.execute(query);
    }

    public List<Book> selectAll() {
        return session.execute("SELECT * FROM " + TABLE_NAME + " ALLOW FILTERING")// need ALLOW FILTERING due to full scan
                .all().stream()
                .map(row -> new Book(row.getUUID("id"), row.getString("title"), row.getString("subject")))
                .toList();

    }

    public List<Book> selectByTitle(String title) {
        return session.execute("SELECT * FROM " + TABLE_NAME + " WHERE title = ? ALLOW FILTERING", title)// need ALLOW FILTERING due to full scan
                .all().stream()
                .map(row -> new Book(row.getUUID("id"), row.getString("title"), row.getString("subject")))
                .toList();
    }

}
