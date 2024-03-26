package com.example.database.controllers;

import com.example.database.domain.dto.AuthorDto;
import com.example.database.domain.dto.BookDto;
import com.example.database.domain.entities.Author;
import com.example.database.domain.entities.Book;
import com.example.database.mappers.Mapper;
import com.example.database.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// +----------------------------------------------+
// | Method | Route         | Function            |
// +----------------------------------------------+
// | PUT    | /books/{isbn} | Create book         |
// | GET    | /books/{isbn} | Read one book       |
// | GET    | /books        | Read many books     |
// | PUT    | /books/{isbn} | Update book         |
// | PATCH  | /books/{isbn} | Update partial book |
// | DELETE | /books/{isbn} | Delete book         |
// +----------------------------------------------+

@RestController
public class BookController {

    private BookService bookService;

    private Mapper<Book, BookDto> bookMapper;

    public BookController(BookService bookService, Mapper<Book, BookDto> bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> createUpdateBook(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto) {

        Book book = bookMapper.mapFrom(bookDto);

        boolean bookExists = bookService.isPresent(isbn);
        // The bookExists boolean must be performed here and not in the if-else statement because otherwise the book
        // would have been created (see below) and the response status code will always be 200 for updated.

        Book createdBook = bookService.createUpdateBook(isbn, book);
        // Regardless of whether the book exists in the database, the createBook method should be called because it
        // serves to create or update a book. The only difference is the response status code, with 201 for created and
        // 200 for updated.

        if (!bookExists) {
            return new ResponseEntity<>(bookMapper.mapTo(createdBook), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(bookMapper.mapTo(createdBook), HttpStatus.OK);
        }
    }

    @GetMapping(path = "/books")
    public Page<BookDto> getBooks(Pageable pageable) {

        Page<Book> books = bookService.getBooks(pageable);

        return books.map(bookMapper::mapTo);
    }
    // The PagingAndSortingRepository allows you to control the page size and retrieved page using query parameters
    // defined in the API URL endpoint (e.g. /books?size=5&page=10)

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> getBook(@PathVariable("isbn") String isbn) {

        Optional<Book> book = bookService.getBook(isbn);

        return book
                .map(entity -> new ResponseEntity<>(bookMapper.mapTo(entity), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> patchBook(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto) {

        if (!bookService.isPresent(isbn)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Book book = bookMapper.mapFrom(bookDto);

        Book patchedBook = bookService.patchBook(isbn, book);

        return new ResponseEntity<>(bookMapper.mapTo(patchedBook), HttpStatus.OK);

    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> deleteBook(@PathVariable("isbn") String isbn) {
        bookService.deleteBook(isbn);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
