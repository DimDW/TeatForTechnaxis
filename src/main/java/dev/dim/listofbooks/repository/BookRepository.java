package dev.dim.listofbooks.repository;

import dev.dim.listofbooks.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAll(Pageable pageable);
    Page<Book> findByTitleIgnoreCaseOrderByTitleAsc(String title, Pageable pageable);
}
