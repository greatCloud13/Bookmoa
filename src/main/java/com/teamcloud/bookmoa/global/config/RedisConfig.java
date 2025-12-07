package com.teamcloud.bookmoa.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory){

        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
                .builder()
                .allowIfSubType(Object.class)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // LocalDate 처리 가능하도록 설정
        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);  // "2025-12-06" 형식으로 저장

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper))
                )
                .entryTtl(Duration.ofHours(1));     // 기본 TTL 1시간

        // 신간 발간의 경우가 있으므로 책 검색 결과 TTL= 30분
        RedisCacheConfiguration bookSearchConfig = config
                .entryTtl(Duration.ofMinutes(30));

        // 책의 상세 정보 같은 경우 잘 변하지 않으므로 TTL = 6시간
        RedisCacheConfiguration bookConfig = config
                .entryTtl(Duration.ofHours(6));

        // 인기 도서의 경우 실시간성이 중요하고 변경이 있긴 하지만 매우 자주 있지 않으므로 1시간으로 설정
        RedisCacheConfiguration popularBookConfig = config
                .entryTtl(Duration.ofHours(1));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .withCacheConfiguration("bookSearch", bookSearchConfig)
                .withCacheConfiguration("book", bookConfig)
                .withCacheConfiguration("popularBooks", popularBookConfig)
                .build();
    }

}
