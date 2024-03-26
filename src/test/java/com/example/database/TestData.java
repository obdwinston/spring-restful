package com.example.database;

import com.example.database.domain.entities.Author;
import com.example.database.domain.entities.Book;

public final class TestData {

    private TestData() {} // private no-arg constructor

    public static Author createTestAuthor() {
        return Author.builder()
                .id(1L)
                .name("Abigail Rose")
                .age(80)
                .build();
    }

    public static Author createAnotherTestAuthor() {
        return Author.builder()
                .id(2L)
                .name("Thomas Cronin")
                .age(44)
                .build();
    }

    public static Book createTestBook(final Author author) {
        return Book.builder()
                .isbn("123-1-2345-6789-0")
                .title("The Shadow in the Attic")
                .author(author)
                .build();
    }

    public static Book createAnotherTestBook(final Author author) {
        return Book.builder()
                .isbn("123-1-2345-6789-1")
                .title("The Crack on the Wall")
                .author(author)
                .build();
    }
}
