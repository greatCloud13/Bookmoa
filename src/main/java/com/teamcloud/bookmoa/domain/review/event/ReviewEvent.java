package com.teamcloud.bookmoa.domain.review.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEvent implements Serializable {

    private String isbn;

    private EventType eventType;

    public enum EventType {
        CREATE, // 리뷰 생성
        UPDATE, // 리뷰 수정
        DELETE  // 리뷰 삭제
    }

    public static ReviewEvent create(String isbn){
        return new ReviewEvent(isbn, EventType.CREATE);
    }

    public static ReviewEvent update(String isbn){
        return new ReviewEvent(isbn, EventType.UPDATE);
    }

    public static ReviewEvent delete(String isbn){
        return new ReviewEvent(isbn, EventType.DELETE);
    }

}
