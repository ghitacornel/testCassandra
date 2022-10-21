package cassandra.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Book {

    private UUID id;
    private String title;
    private String subject;

}
