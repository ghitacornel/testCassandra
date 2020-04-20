package tests;

import cassandra.repository.BookRepository;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static cassandra.repository.KeySpaceRepository.KEYSPACE_NAME;

public class TestBookRepository extends TestsSetup {

    private final BookRepository repository = new BookRepository(session);

    @Before
    public void dropTable() {
        repository.dropTable();
    }

    @Test
    public void whenCreatingATable_thenCreatedCorrectly() {
        repository.createTable();

        ResultSet result = session.execute("SELECT * FROM " + KEYSPACE_NAME + ".books;");

        List<String> columnNames =
                result.getColumnDefinitions().asList().stream()
                        .map(ColumnDefinitions.Definition::getName)
                        .collect(Collectors.toList());

        Assert.assertEquals(columnNames.size(), 3);
        Assert.assertTrue(columnNames.contains("id"));
        Assert.assertTrue(columnNames.contains("title"));
        Assert.assertTrue(columnNames.contains("subject"));
    }

    @Test
    public void whenAlteringTable_thenAddedColumnExists() {
        repository.createTable();
        repository.alterTable("publisher", "text");

        ResultSet result = session.execute("SELECT * FROM " + KEYSPACE_NAME + "." + "books" + ";");

        boolean columnExists = result.getColumnDefinitions().asList().stream().
                anyMatch(cl -> cl.getName().equals("publisher"));

        Assert.assertTrue(columnExists);
    }

}
