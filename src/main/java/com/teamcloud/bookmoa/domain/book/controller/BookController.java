package com.teamcloud.bookmoa.domain.book.controller;

import com.teamcloud.bookmoa.domain.book.dto.AladinApiResponse;
import com.teamcloud.bookmoa.domain.book.entity.Book;
import com.teamcloud.bookmoa.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
