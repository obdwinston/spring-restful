package com.example.database.services;

import com.example.database.domain.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book createUpdateBook(String isbn, Book book);

    List<Book> getBooks();

    Page<Book> getBooks(Pageable pageable);

    Optional<Book> getBook(String isbn);

    Book patchBook(String isbn, Book book);

    void deleteBook(String isbn);

    boolean isPresent(String isbn);
}
