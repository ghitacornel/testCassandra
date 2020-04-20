package tests;

import cassandra.repository.BookByTitleRepository;
import cassandra.model.Book;
import com.datastax.driver.core.utils.UUIDs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TestBookByTitleRepository extends TestsSetup {

    private final BookByTitleRepository repository = new BookByTitleRepository(session);

    @Before
    public void dropTable() {
        repository.dropTable();
        repository.createTable();
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
