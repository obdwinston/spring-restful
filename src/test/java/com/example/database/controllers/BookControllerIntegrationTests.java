package com.example.database.controllers;

import com.example.database.TestData;
import com.example.database.domain.entities.Author;
import com.example.database.domain.entities.Book;
import com.example.database.services.BookService;
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

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTests {

    private MockMvc mockMvc;

    private BookService bookService;

    private ObjectMapper objectMapper;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, BookService bookService) {
        this.mockMvc = mockMvc;
        this.bookService = bookService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateBookControllerReturnsHttpStatus201() throws Exception {

        Book book = TestData.createTestBook(null);

        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/books/" + book.getIsbn())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bookJson))
                .andExpect(
                        MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testCreateBookControllerReturnsCreatedBook() throws Exception {

        Book book = TestData.createTestBook(null);

        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/books/" + book.getIsbn())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bookJson))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()));
    }

    @Test
    public void testGetBooksControllerReturnsHttpStatus200() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/books"))
                .andExpect(
                        MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetBooksControllerReturnsBooks() throws Exception {

        Book book = TestData.createTestBook(null);

        bookService.createUpdateBook(book.getIsbn(), book);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/books"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].isbn").value(book.getIsbn()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].title").value(book.getTitle()));
    }

    @Test
    public void testGetBookControllerReturnsHttpStatus200IfBookExists() throws Exception {

        Book book = TestData.createTestBook(null);

        bookService.createUpdateBook(book.getIsbn(), book);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/books/" + book.getIsbn()))
                .andExpect(
                        MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetBookControllerReturnsBookIfBookExists() throws Exception {

        Book book = TestData.createTestBook(null);

        bookService.createUpdateBook(book.getIsbn(), book);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/books/" + book.getIsbn()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()));
    }

    @Test
    public void testGetBookControllerReturnsHttpStatus404IfBookDoesNotExist() throws Exception {

        Book book = TestData.createTestBook(null);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/books/" + book.getIsbn()))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUpdateBookControllerReturnsHttpStatus200() throws Exception {

        Book book = TestData.createTestBook(null);

        Book createdBook = bookService.createUpdateBook(book.getIsbn(), book);

        String bookJson = objectMapper.writeValueAsString(createdBook);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/books/" + createdBook.getIsbn())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bookJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateBookControllerReturnsUpdatedBook() throws Exception {

        Book book = TestData.createTestBook(null);

        Book createdBook = bookService.createUpdateBook(book.getIsbn(), book);

        createdBook.setIsbn(book.getIsbn());
        createdBook.setTitle("UPDATED");

        String bookJson = objectMapper.writeValueAsString(createdBook);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/books/" + createdBook.getIsbn())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bookJson))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.isbn").value(createdBook.getIsbn()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.title").value(createdBook.getTitle()));
    }

    @Test
    public void testPatchBookControllerReturnsHttpStatus404IfBookDoesNotExist() throws Exception {

        Book book = TestData.createTestBook(null);

        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch("/books/123-1-2345-6789-0")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bookJson))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testPatchBookControllerReturnsHttpStatus200IfBookExists() throws Exception {

        Book book = TestData.createTestBook(null);

        Book createdBook = bookService.createUpdateBook(book.getIsbn(), book);

        String bookJson = objectMapper.writeValueAsString(createdBook);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch("/books/" + createdBook.getIsbn())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bookJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPatchBookControllerReturnsUpdatedBookIfBookExists() throws Exception {

        Book book = TestData.createTestBook(null);

        Book createdBook = bookService.createUpdateBook(book.getIsbn(), book);

        createdBook.setTitle("UPDATED");

        String bookJson = objectMapper.writeValueAsString(createdBook);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/books/" + createdBook.getIsbn())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bookJson))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.isbn").value(createdBook.getIsbn()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.title").value(createdBook.getTitle()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.author").value(createdBook.getAuthor()));
    }

    @Test
    public void testDeleteBookControllerReturnsHttpStatus204IfBookDoesNotExist() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/books/123-1-2345-6789-0"))
                .andExpect(
                        MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDeleteBookControllerReturnsHttpStatus204IfBookExists() throws Exception {

        Book book = TestData.createTestBook(null);

        Book createdBook = bookService.createUpdateBook(book.getIsbn(), book);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/books/" + createdBook.getIsbn()))
                .andExpect(
                        MockMvcResultMatchers.status().isNoContent());
    }
}
