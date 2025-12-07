package com.teamcloud.bookmoa.domain.book.controller;

import com.teamcloud.bookmoa.domain.book.dto.AladinApiResponse;
import com.teamcloud.bookmoa.domain.book.entity.Book;
import com.teamcloud.bookmoa.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/search")
    public ResponseEntity<AladinApiResponse> searchBooks(@RequestParam String query){
        AladinApiResponse result = bookService.searchBooks(query);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn){
        Book book = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Book>> getPopularBooks(
            @RequestParam(defaultValue = "10") int limit) {
        List<Book> popularBooks = bookService.getPopularBooks(limit);
        return ResponseEntity.ok(popularBooks);
    }

}
