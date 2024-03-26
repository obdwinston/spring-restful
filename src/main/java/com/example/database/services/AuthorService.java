package com.example.database.services;

import com.example.database.domain.dto.AuthorDto;
import com.example.database.domain.entities.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    Author createAuthor(Author author);

    List<Author> getAuthors();

    Optional<Author> getAuthor(Long id);

    Author updateAuthor(Long id, Author author);

    Author patchAuthor(Long id, Author author);

    void deleteAuthor(Long id);

    boolean isPresent(Long id);
}
