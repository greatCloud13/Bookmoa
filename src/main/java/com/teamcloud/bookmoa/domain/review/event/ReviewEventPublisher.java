package com.teamcloud.bookmoa.domain.review.event;

import com.teamcloud.bookmoa.global.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishReviewEvent(ReviewEvent event){
        log.info("이벤트 발행 - ISBN: {}, EventType: {}",event.getIsbn(), event.getEventType());
        rabbitTemplate.convertAndSend(RabbitMQConfig.REVIEW_STATISTICS_QUEUE, event);
    }


}
