package com.redis.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.CacheKeyPrefix
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration


@Configuration
@EnableRedisRepositories
class RedisConfig(
    @Value("\${spring.redis.host}")
    private val host: String,
    @Value("\${spring.redis.port}")
    private val port: Int
) {
    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory {
        val lettuceConnectionFactory = LettuceConnectionFactory(RedisStandaloneConfiguration(host, port))
        lettuceConnectionFactory.afterPropertiesSet()
        return lettuceConnectionFactory
    }

    @Bean(name = ["memberRedisTemplate"])
    fun memberRedisTemplate(): RedisTemplate<String, String> {
        val redisTemplate = RedisTemplate<String, String>()
        redisTemplate.setConnectionFactory(redisConnectionFactory())

        // 아래의 설정값이 없으면 스프링에서 조회할 때는 값이 정상으로 보이지만 redis-cli로 조회하면 `xec\x83\x98\xed\x94\x8c1` 이런식으로 보여짐
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = StringRedisSerializer()
        redisTemplate.afterPropertiesSet()
        return redisTemplate
    }

    /**
     * Redis Cache를 관리
     * @return RedisCacheManager
     */
    @Primary
    @Bean(name = ["memberManager"])
    fun memberManager(): RedisCacheManager {
        println("call Redis cache manager by Annotation")
        val configuration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    GenericJackson2JsonRedisSerializer()
                )
            )   // json 형식으로 value 저장
            .computePrefixWith(CacheKeyPrefix.simple())             // key앞에 '::'를 삽입
            .disableCachingNullValues()                             // null 값 금지
            .entryTtl(Duration.ofHours(8))                     // 캐싱 유지 시간 설정

        return RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory())
            .cacheDefaults(configuration)
            .build()
    }
}