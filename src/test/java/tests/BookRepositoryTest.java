package tests;

import cassandra.model.Book;
import cassandra.repository.BookRepository;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.utils.UUIDs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static cassandra.repository.KeySpaceRepository.KEYSPACE_NAME;

public class BookRepositoryTest extends TestsSetup {

    private final BookRepository repository = new BookRepository(session);

    @Before
    public void setup() {
        repository.dropTable();
        repository.createTable();
    }

    @Test
    public void create() {

        ResultSet result = session.execute("SELECT * FROM " + KEYSPACE_NAME + ".books;");

        List<String> columnNames =
                result.getColumnDefinitions().asList().stream()
                        .map(ColumnDefinitions.Definition::getName)
                        .toList();

        Assert.assertEquals(columnNames.size(), 3);
        Assert.assertTrue(columnNames.contains("id"));
        Assert.assertTrue(columnNames.contains("title"));
        Assert.assertTrue(columnNames.contains("subject"));
    }

    @Test
    public void alter() {

        repository.alterTable("publisher", "text");

        ResultSet result = session.execute("SELECT * FROM " + KEYSPACE_NAME + "." + "books" + ";");

        boolean columnExists = result.getColumnDefinitions().asList().stream().
                anyMatch(cl -> cl.getName().equals("publisher"));

        Assert.assertTrue(columnExists);
    }

    @Test
    public void crud() {

        Book initialBook;
        // insert
        {
            initialBook = new Book(UUIDs.timeBased(), "Effective Java", "Programming");
            repository.insert(initialBook);

            List<Book> books = repository.selectByTitle("Effective Java");
            Assert.assertEquals(1, books.size());
            Assert.assertEquals(1, repository.countAll());
            Assert.assertTrue(books.stream().anyMatch(b -> b.getTitle().equals("Effective Java")));
        }

        // insert
        {
            Book book = new Book(UUIDs.timeBased(), "Clean Code", "Programming");
            repository.insert(book);

            List<Book> books = repository.selectAll();

            Assert.assertEquals(2, books.size());
            Assert.assertEquals(2, repository.countAll());
            Assert.assertTrue(books.stream().anyMatch(b -> b.getTitle().equals("Effective Java")));
            Assert.assertTrue(books.stream().anyMatch(b -> b.getTitle().equals("Clean Code")));
        }

        // find by id
        {
            Book book = repository.findById(initialBook.getId());
            Assert.assertEquals(initialBook, book);
        }

        // update subject
        {
            repository.updateFieldSubject(initialBook.getId(), initialBook.getSubject() + " II");
            Book book = repository.findById(initialBook.getId());
            initialBook.setSubject(initialBook.getSubject() + " II");// in order to match the database state
            Assert.assertEquals(initialBook, book);
        }

        // delete field subject
        {
            repository.deleteFieldSubject(initialBook.getId());
            Book book = repository.findById(initialBook.getId());
            initialBook.setSubject(null);// in order to match the database state
            Assert.assertEquals(initialBook, book);
        }

        // delete by id
        {
            repository.deleteById(initialBook.getId());
            List<Book> books = repository.selectAll();

            Assert.assertEquals(1, books.size());
            Assert.assertEquals(1, repository.countAll());
            Assert.assertTrue(books.stream().anyMatch(b -> b.getTitle().equals("Clean Code")));
        }

        // delete all
        {
            repository.deleteAll();
            Assert.assertTrue(repository.selectAll().isEmpty());
            Assert.assertEquals(0, repository.countAll());
        }

    }

}
