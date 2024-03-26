package com.example.database.repositories;

import com.example.database.TestData;
import com.example.database.domain.entities.Author;
import com.example.database.domain.entities.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookRepositoryIntegrationTests {

    private final BookRepository underTest;

    @Autowired
    public BookRepositoryIntegrationTests(BookRepository underTest) {
        this.underTest = underTest;
    };

    @Test
    public void testCreateBookRepositoryResult() {

        Author author = TestData.createTestAuthor();

        Book book = TestData.createTestBook(author);
        underTest.save(book);

        Optional<Book> result = underTest.findById(book.getIsbn());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(book);
    }

    @Test
    public void testGetBooksRepositoryResult() {

        Author author = TestData.createTestAuthor();
        Author anotherAuthor = TestData.createAnotherTestAuthor();

        Book book = TestData.createTestBook(author);
        Book anotherBook = TestData.createAnotherTestBook(anotherAuthor);
        underTest.save(book);
        underTest.save(anotherBook);

        Iterable<Book> result = underTest.findAll();

        assertThat(result)
                .hasSize(2)
                .containsExactly(book, anotherBook);
    }

    @Test
    public void testUpdateBookRepositoryResult() {

        Author author = TestData.createTestAuthor();

        Book book = TestData.createTestBook(author);
        underTest.save(book);

        book.setTitle("UPDATED");
        underTest.save(book);

        Optional<Book> result = underTest.findById(book.getIsbn());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(book);
    }

    @Test
    public void testDeleteBookRepositoryResult() {

        Author author = TestData.createTestAuthor();

        Book book = TestData.createTestBook(author);
        underTest.save(book);

        underTest.deleteById(book.getIsbn());

        Optional<Book> result = underTest.findById(book.getIsbn());

        assertThat(result).isNotPresent();
    }
}
