package dev.dim.listofbooks;


import dev.dim.listofbooks.model.Book;
import dev.dim.listofbooks.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.Column;

@SpringBootApplication
public class ListOfBooksApplication {

    public static void main(String[] args) {
        SpringApplication.run(ListOfBooksApplication.class, args);
    }

    @Bean
    public CommandLineRunner DataForTest(BookRepository repository) {
        return (args) -> {
            // save a few book for test
            repository.save(new Book("Java: The Complete Reference","Java","Herbert Schildt", "0071808558", 2018, true,"https://images-na.ssl-images-amazon.com/images/I/412nwcoynrL._SX406_BO1,204,203,200_.jpg"));
            repository.save(new Book("Java","Java"," Schildt", "0071808520", 2019, false,"https://cdn1.ozone.ru/multimedia/wc1200/1023718303.jpg"));
            repository.save(new Book("The Complete Reference","Java","Herbert ", "3071808558", 2020, false,"https://avatars.mds.yandex.net/get-mpic/1673800/img_id3058617897293134862.png/orig"));
        };
    }
}
