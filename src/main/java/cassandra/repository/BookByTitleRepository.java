package cassandra.repository;

import cassandra.model.Book;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.util.ArrayList;
import java.util.List;

import static cassandra.repository.KeySpaceRepository.KEYSPACE_NAME;

public class BookByTitleRepository {

    public static final String TABLE_NAME = KEYSPACE_NAME + "." + "booksByTitle";

    final private Session session;

    public BookByTitleRepository(Session session) {
        this.session = session;
    }

    public void dropTable() {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        session.execute(query);
    }

    public void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                "id uuid, " +
                "title text," +
                "subject text," +
                "PRIMARY KEY (title, id));";
        session.execute(query);
    }

    public void insert(Book book) {
        String query = "INSERT INTO " +
                TABLE_NAME + "(id, title) " +
                "VALUES (" + book.getId() +
                ", '" + book.getTitle() + "');";
        session.execute(query);
    }

    public List<Book> selectAll() {
        String query = "SELECT * FROM " + TABLE_NAME;
        ResultSet rs = session.execute(query);

        List<Book> books = new ArrayList<>();
        rs.forEach(r -> books.add(new Book(r.getUUID("id"), r.getString("title"), r.getString("subject"))));
        return books;
    }

    public List<Book> selectByTitle(String title) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE title = ?";
        ResultSet rs = session.execute(query, title);

        List<Book> books = new ArrayList<>();
        rs.forEach(r -> books.add(new Book(r.getUUID("id"), r.getString("title"), r.getString("subject"))));
        return books;
    }
}
