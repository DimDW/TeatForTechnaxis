package dev.dim.listofbooks.controller;

import dev.dim.listofbooks.model.Book;
import dev.dim.listofbooks.model.BookSchema;
import dev.dim.listofbooks.repository.BookRepository;
import dev.dim.listofbooks.service.AwsS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.annotation.MultipartConfig;
import java.io.File;
import java.util.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    BucketController bucketController;

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }



    @GetMapping("/books")
    public ResponseEntity<Map<String, Object>> getAllBooksPage(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        try {
            List<Order> orders = new ArrayList<Order>();

            if (sort[0].contains(",")) {

                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                orders.add(new Order(getSortDirection(sort[1]), sort[0]));
            }

            List<Book> books;
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

            Page<Book> bookPage;
            if (title == null)
                bookPage = bookRepository.findAll(pagingSort);
            else
                bookPage = bookRepository.findByTitleIgnoreCaseOrderByTitleAsc(title, pagingSort);

            books = bookPage.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("books", books);
            response.put("currentPage", bookPage.getNumber());
            response.put("totalItems", bookPage.getTotalElements());
            response.put("totalPages", bookPage.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@RequestBody BookSchema book, File image) {
        try {
            //AwsS3Client awsS3Client = new AwsS3Client();

            Book _book = bookRepository.save(new Book(
                    book.getTitle(),
                    book.getDescription(),
                    book.getAuthor(),
                    book.getISBN(),
                    book.getPrintYear(),
                    book.getReadAlready(),
                    bucketController.uploadFile(image)));


            return new ResponseEntity<>(_book, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book>  readAlreadyBook (@PathVariable("id") long id){
        Optional<Book> bookData = bookRepository.findById(id);
        if (bookData.isPresent()) {
            if (bookData.get().getReadAlready())
                return new ResponseEntity<>(HttpStatus.OK);
            else {
                Book _book = bookData.get();
                _book.setReadAlready(true);
                return new ResponseEntity<>(bookRepository.save(_book), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/books/update/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") long id, @RequestBody BookSchema book, File image) {
        Optional<Book> bookData = bookRepository.findById(id);

        if (bookData.isPresent()) {
            Book _book = bookData.get();
            _book.setTitle(book.getTitle());
            _book.setDescription(book.getDescription());
            _book.setISBN(book.getISBN());
            _book.setPrintYear(book.getPrintYear());
            _book.setImage(bucketController.uploadFile(image));
            _book.setReadAlready(false);
            return new ResponseEntity<>(bookRepository.save(_book), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") long id) {
        try {
            bucketController.deleteFile( bookRepository.getById(id).getImage());
            bookRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
