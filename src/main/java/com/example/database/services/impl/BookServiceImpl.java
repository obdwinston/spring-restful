package com.example.database.services.impl;

import com.example.database.domain.entities.Book;
import com.example.database.repositories.BookRepository;
import com.example.database.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book createUpdateBook(String isbn, Book book) {

        book.setIsbn(isbn);

        return bookRepository.save(book);
    }

    @Override
    public List<Book> getBooks() {
        return StreamSupport
                .stream(bookRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Book> getBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Optional<Book> getBook(String isbn) {

        return bookRepository.findById(isbn);
    }

    @Override
    public Book patchBook(String isbn, Book book) {

        book.setIsbn(isbn);

        return bookRepository.findById(isbn).map(existingBook -> {
            Optional.ofNullable(book.getTitle()).ifPresent(existingBook::setTitle);
            // Optional.ofNullable(book.getAuthor()).ifPresent(existingBook::setAuthor);

            return bookRepository.save(existingBook);

        }).orElseThrow(() -> new RuntimeException("Book does not exist"));
    }

    @Override
    public void deleteBook(String isbn) {

        bookRepository.deleteById(isbn);
    }

    @Override
    public boolean isPresent(String isbn) {

        return bookRepository.existsById(isbn);
    }
}
