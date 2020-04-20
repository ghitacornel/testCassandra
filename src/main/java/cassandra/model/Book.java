package cassandra.model;

import java.util.UUID;

public class Book {

    private UUID id;
    private String title;
    private String subject;

    public Book(UUID id, String title, String subject) {
        this.id = id;
        this.title = title;
        this.subject = subject;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
