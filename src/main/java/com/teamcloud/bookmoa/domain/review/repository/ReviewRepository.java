package com.teamcloud.bookmoa.domain.review.repository;

import com.teamcloud.bookmoa.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByBookIsbn(String isbn);

}
