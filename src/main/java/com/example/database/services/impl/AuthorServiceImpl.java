package com.example.database.services.impl;

import com.example.database.domain.dto.AuthorDto;
import com.example.database.domain.entities.Author;
import com.example.database.repositories.AuthorRepository;
import com.example.database.services.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author createAuthor(Author author) {

        return authorRepository.save(author);
        // Recall that the save method returns the same object by default.
    }

    @Override
    public List<Author> getAuthors() {

        // Recall that the findAll method returns an Iterable. The following converts the Iterable to a List.
        return StreamSupport
                .stream(authorRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Author> getAuthor(Long id) {

        return authorRepository.findById(id);
    }

    @Override
    public Author updateAuthor(Long id, Author author) {

        author.setId(id);

        return authorRepository.save(author);
    }

    @Override
    public Author patchAuthor(Long id, Author author) {

        author.setId(id);

        return authorRepository.findById(id).map(existingAuthor -> {
            Optional.ofNullable(author.getName()).ifPresent(existingAuthor::setName);
            // getName() may return null, but if present, setName for existingAuthor with that value.
            Optional.ofNullable(author.getAge()).ifPresent(existingAuthor::setAge);
            // getAge() may return null, but if present, setAge for existingAuthor with that value.

            return authorRepository.save(existingAuthor);

        }).orElseThrow(() -> new RuntimeException("Author does not exist"));
        // Note that it is near impossible for this runtime exception to be thrown because the existence of the author
        // in the database is already verified in the controller.
    }

    @Override
    public void deleteAuthor(Long id) {

        authorRepository.deleteById(id);
    }

    @Override
    public boolean isPresent(Long id) {

        return authorRepository.existsById(id);
    }
}

// Of note, the List interface extends the Collection interface, which extends the Iterable interface. Iterable is the
// basic interface for looping over a collection of elements. Collection represents a group of elements with basic
// operations like adding and removing. List is an ordered collection of elements that allows duplicates and provides
// indexing. Stream is a sequence of elements that supports functional operations (e.g. map, filter, reduce, etc.) in a
// possibly parallel manner, and without modifying the source (i.e. creates a new stream for processing).