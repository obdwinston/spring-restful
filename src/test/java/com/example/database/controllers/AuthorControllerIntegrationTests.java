package com.example.database.controllers;

import com.example.database.TestData;
import com.example.database.domain.entities.Author;
import com.example.database.services.AuthorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

// @AutoConfigureMockMvc is part of Spring Test and provides a way to test Spring MVC applications without starting a
// full HTTP server. It allows you to simulate HTTP requests and responses, making it ideal for testing controllers.

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTests {

    private MockMvc mockMvc;

    private AuthorService authorService;

    private ObjectMapper objectMapper;
    // ModelMapper from the ModelMapper library is used to map DTOs to domain entities and vice versa. ObjectMapper from
    // the Jackson library serialises (i.e. marshals) Java objects to JSON objects and vice versa.

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.authorService = authorService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateAuthorControllerReturnsHttpStatus201() throws Exception {

        Author author = TestData.createTestAuthor();

        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/authors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson))
                .andExpect(
                        MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testCreateAuthorControllerReturnsCreatedAuthor() throws Exception {

        Author author = TestData.createTestAuthor();
        author.setId(null);
        // Setting ID to null tests whether the database auto-generates the author ID.

        String authorJson = objectMapper.writeValueAsString(author);

        // The request should return the created author.
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/authors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name").value(author.getName()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.age").value(author.getAge()));
        // The $ sign serves as the root element in a JSON document.
    }

    @Test
    public void testGetAuthorsControllerReturnsHttpStatus200() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/authors"))
                .andExpect(
                        MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetAuthorsControllerReturnsAuthors() throws Exception {

        Author author = TestData.createTestAuthor();

        authorService.createAuthor(author);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/authors"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].name").value(author.getName()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].age").value(author.getAge()));
    }

    @Test
    public void testGetAuthorControllerReturnsHttpStatus200IfAuthorExists() throws Exception {

        Author author = TestData.createTestAuthor();

        authorService.createAuthor(author);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/authors/1"))
                .andExpect(
                        MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetAuthorControllerReturnsAuthorIfAuthorExists() throws Exception {

        Author author = TestData.createTestAuthor();

        authorService.createAuthor(author);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/authors/1"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name").value(author.getName()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.age").value(author.getAge()));
    }

    @Test
    public void testGetAuthorControllerReturnsHttpStatus404IfAuthorDoesNotExist() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/authors/1"))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUpdateAuthorControllerReturnsHttpStatus404IfAuthorDoesNotExist() throws Exception {

        Author author = TestData.createTestAuthor();

        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/authors/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUpdateAuthorControllerReturnsHttpStatus200IfAuthorExists() throws Exception {

        Author author = TestData.createTestAuthor();

        Author createdAuthor = authorService.createAuthor(author);

        String authorJson = objectMapper.writeValueAsString(createdAuthor);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/authors/" + createdAuthor.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateAuthorControllerReturnsUpdatedAuthorIfAuthorExists() throws Exception {

        Author author = TestData.createTestAuthor();

        Author createdAuthor = authorService.createAuthor(author);

        createdAuthor.setName("UPDATED");
        createdAuthor.setAge(100);

        String authorJson = objectMapper.writeValueAsString(createdAuthor);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/authors/" + createdAuthor.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").value(createdAuthor.getId()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name").value(createdAuthor.getName()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.age").value(createdAuthor.getAge()));
    }

    @Test
    public void testPatchAuthorControllerReturnsHttpStatus404IfAuthorDoesNotExist() throws Exception {

        Author author = TestData.createTestAuthor();

        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch("/authors/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testPatchAuthorControllerReturnsHttpStatus200IfAuthorExists() throws Exception {

        Author author = TestData.createTestAuthor();

        Author createdAuthor = authorService.createAuthor(author);

        String authorJson = objectMapper.writeValueAsString(createdAuthor);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch("/authors/" + createdAuthor.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPatchAuthorControllerReturnsUpdatedAuthorIfAuthorExists() throws Exception {

        Author author = TestData.createTestAuthor();

        Author createdAuthor = authorService.createAuthor(author);

        createdAuthor.setName("UPDATED");

        String authorJson = objectMapper.writeValueAsString(createdAuthor);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/authors/" + createdAuthor.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").value(createdAuthor.getId()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name").value(createdAuthor.getName()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.age").value(createdAuthor.getAge()));
    }

    @Test
    public void testDeleteAuthorControllerReturnsHttpStatus204IfAuthorDoesNotExist() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/authors/1"))
                .andExpect(
                        MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDeleteAuthorControllerReturnsHttpStatus204IfAuthorExists() throws Exception {

        Author author = TestData.createTestAuthor();

        Author createdAuthor = authorService.createAuthor(author);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/authors/" + createdAuthor.getId()))
                .andExpect(
                        MockMvcResultMatchers.status().isNoContent());
    }
}
