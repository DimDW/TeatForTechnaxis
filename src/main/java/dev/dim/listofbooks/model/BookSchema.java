package dev.dim.listofbooks.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BookSchema {

    private String title;

    private String description;

    private String author;

    private String ISBN;

    private int printYear;

    private Boolean ReadAlready;

    private MultipartFile image;

}
