package com.teamcloud.bookmoa.domain.book.repository;

import com.teamcloud.bookmoa.domain.book.entity.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    @Query("SELECT AVG(b.averageRating) FROM Book b WHERE b.reviewCount > 0")
    Double getGlobalAverageRating();

    @Query("""
            SELECT b FROM Book b
            WHERE b.reviewCount > 0
            ORDER BY (b.reviewCount * b.averageRating + :minReviews * :globalAvg) / (b.reviewCount + :minReviews) DESC
            """)
    List<Book> findPopularBooks(
            @Param("minReviews") int minReviews,
            @Param("globalAvg") double globalAvg,
            Pageable pageable
    );


}
