package cassandra.repository;

import cassandra.model.Book;
import com.datastax.driver.core.Session;

import java.util.List;

import static cassandra.repository.KeySpaceRepository.KEYSPACE_NAME;

public class BookByTitleRepository {

    public static final String TABLE_NAME = KEYSPACE_NAME + "." + "booksByTitle";

    final private Session session;

    public BookByTitleRepository(Session session) {
        this.session = session;
    }

    public void dropTable() {
        session.execute("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public void createTable() {
        session.execute("CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                "id uuid" + "," +
                "title text" + "," +
                "subject text" + "," +
                "PRIMARY KEY (title, id)" +
                ")");
    }

    public void insert(Book book) {
        session.execute("INSERT INTO " +
                TABLE_NAME + "(id, title) " +
                "VALUES (" +
                book.getId() + "," +
                "'" + book.getTitle() + "'" +
                ")");
    }

    public List<Book> selectAll() {
        return session.execute("SELECT * FROM " + TABLE_NAME)
                .all().stream()
                .map(row -> new Book(row.getUUID("id"), row.getString("title"), row.getString("subject")))
                .toList();

    }

    public List<Book> selectByTitle(String title) {
        return session.execute("SELECT * FROM " + TABLE_NAME + " WHERE title = ? ", title)
                .all().stream()
                .map(row -> new Book(row.getUUID("id"), row.getString("title"), row.getString("subject")))
                .toList();
    }
}
