package dev.dim.listofbooks.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.File;

@Entity
@Table (name = "Books")
@Getter
@Setter

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private String author;
    @Column
    private String ISBN;
    @Column
    private int printYear;
    @Column
    private Boolean readAlready;
    @Column
    private String image;


    public Book(String title, String description, String author, String ISBN, int printYear, Boolean readAlready, String image) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.ISBN = ISBN;
        this.printYear = printYear;
        this.readAlready = readAlready;
        this.image = image;
    }

    protected Book() {

    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", printYear=" + printYear +
                ", readAlready=" + readAlready +
                ", image='" + image + '\'' +
                '}';
    }
}
