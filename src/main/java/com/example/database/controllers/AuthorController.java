package com.example.database.controllers;

import com.example.database.domain.dto.AuthorDto;
import com.example.database.domain.entities.Author;
import com.example.database.mappers.Mapper;
import com.example.database.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// +------------------------------------------------+
// | Method | Route         | Function              |
// +------------------------------------------------+
// | POST   | /authors      | Create author         |
// | GET    | /authors/{id} | Read one author       |
// | GET    | /authors      | Read many authors     |
// | PUT    | /authors/{id} | Update author         |
// | PATCH  | /authors/{id} | Update partial author |
// | DELETE | /authors/{id} | Delete author         |
// +------------------------------------------------+

@RestController
public class AuthorController {

    private AuthorService authorService;

    private Mapper<Author, AuthorDto> authorMapper;

    public AuthorController(AuthorService authorService, Mapper<Author, AuthorDto> authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    // Note that AuthorDto (used in controllers) is used here instead of Author (used in services). ResponseEntity
    // allows you to customise the response status code, headers, and body. It provides flexibility in constructing and
    // returning responses from controller methods.
    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto authorDto) {

        Author author = authorMapper.mapFrom(authorDto);

        Author createdAuthor = authorService.createAuthor(author);

        return new ResponseEntity<>(authorMapper.mapTo(createdAuthor), HttpStatus.CREATED);
    }

    @GetMapping(path = "/authors")
    public List<AuthorDto> getAuthors() {

        List<Author> authors = authorService.getAuthors();

        return authors
                .stream()
                .map(authorMapper::mapTo) // instance method reference
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable("id") Long id) {

        Optional<Author> author = authorService.getAuthor(id);

        return author
                .map(entity -> new ResponseEntity<>(authorMapper.mapTo(entity), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        // The map and orElse methods are available with the Optional object.
    }

    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(
            @PathVariable("id") Long id,
            @RequestBody AuthorDto authorDto) {

        if (!authorService.isPresent(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Author author = authorMapper.mapFrom(authorDto);

        Author updatedAuthor = authorService.updateAuthor(id, author);

        return new ResponseEntity<>(authorMapper.mapTo(updatedAuthor), HttpStatus.OK);
    }

    @PatchMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> patchAuthor(
            @PathVariable("id") Long id,
            @RequestBody AuthorDto authorDto) {

        if (!authorService.isPresent(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Author author = authorMapper.mapFrom(authorDto);

        Author patchedAuthor = authorService.patchAuthor(id, author);

        return new ResponseEntity<>(authorMapper.mapTo(patchedAuthor), HttpStatus.OK);
    }

    @DeleteMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> deleteAuthor(@PathVariable("id") Long id) {
        authorService.deleteAuthor(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

// The methods in controllers are never invoked, even in the integration tests. Reason being that the integration tests
// uses MockMvc to directly send and receive HTTP requests and responses respectively to verify that the API endpoints
// are working as intended.