package com.example.database.repositories;

import com.example.database.TestData;
import com.example.database.domain.entities.Author;
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
public class AuthorRepositoryIntegrationTests {

    private final AuthorRepository underTest;

    @Autowired
    public AuthorRepositoryIntegrationTests(AuthorRepository underTest) {
        this.underTest = underTest;
    };

    @Test
    public void testCreateAuthorRepositoryResult() {

        Author author = TestData.createTestAuthor();
        underTest.save(author);

        Optional<Author> result = underTest.findById(author.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(author);
    }

    @Test
    public void testGetAuthorsRepositoryResult() {

        Author author = TestData.createTestAuthor();
        Author anotherAuthor = TestData.createAnotherTestAuthor();
        underTest.save(author);
        underTest.save(anotherAuthor);

        Iterable<Author> result = underTest.findAll();

        assertThat(result)
                .hasSize(2)
                .containsExactly(author, anotherAuthor);
    }

    @Test
    public void testUpdateAuthorRepositoryResult() {

        Author author = TestData.createTestAuthor();
        underTest.save(author);

        author.setName("UPDATED");
        underTest.save(author);

        Optional<Author> result = underTest.findById(author.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(author);
    }

    @Test
    public void testDeleteAuthorRepositoryResult() {

        Author author = TestData.createTestAuthor();
        underTest.save(author);

        underTest.deleteById(author.getId());

        Optional<Author> result = underTest.findById(author.getId());

        assertThat(result).isNotPresent();
    }
}
