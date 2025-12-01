package com.teamcloud.bookmoa.domain.book.repository;

import com.teamcloud.bookmoa.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
}
