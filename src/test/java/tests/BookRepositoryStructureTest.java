package tests;

import cassandra.repository.BookRepository;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
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

}
