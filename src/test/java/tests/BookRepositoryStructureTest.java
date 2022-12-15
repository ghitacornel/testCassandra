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

public class BookRepositoryStructureTest extends TestsSetup {

    private final BookRepository repository = new BookRepository(session);

    @Before
    public void setup() {
        repository.dropTable();
        repository.createTable();
    }

    @Test
    public void whenCreatingATable_thenCreatedCorrectly() {

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
    public void whenAlteringTable_thenAddedColumnExists() {

        repository.alterTable("publisher", "text");

        ResultSet result = session.execute("SELECT * FROM " + KEYSPACE_NAME + "." + "books" + ";");

        boolean columnExists = result.getColumnDefinitions().asList().stream().
                anyMatch(cl -> cl.getName().equals("publisher"));

        Assert.assertTrue(columnExists);
    }

    @Test
    public void whenSelectingAll_thenReturnAllRecords() {

        {
            Book book = new Book(UUIDs.timeBased(), "Effective Java", "Programming");
            repository.insert(book);

            List<Book> books = repository.selectByTitle("Effective Java");
            Assert.assertTrue(books.stream().anyMatch(b -> b.getTitle().equals("Effective Java")));
        }

        {
            Book book = new Book(UUIDs.timeBased(), "Clean Code", "Programming");
            repository.insert(book);

            List<Book> books = repository.selectAll();

            Assert.assertEquals(2, books.size());
            Assert.assertTrue(books.stream().anyMatch(b -> b.getTitle().equals("Effective Java")));
            Assert.assertTrue(books.stream().anyMatch(b -> b.getTitle().equals("Clean Code")));
        }
    }

}
