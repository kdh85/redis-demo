package com.redis.demo.repository.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.redis.config.RedisConfig
import com.redis.demo.TestRedisConfiguration
import com.redis.demo.domain.Member
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest
@Import(TestRedisConfiguration::class)
internal class MemberCustomRepositoryImplTest {


    @Test
    fun RedisTemplate_create_Test() {

        val redisConfig = RedisConfig("localhost", 6379)

        val memberCustomRepositoryImpl = MemberCustomRepositoryImpl(
            redisConfig.memberRedisTemplate(), ObjectMapper()
        )

        val mapper = ObjectMapper()

        val member1 = Member(
            "123",
            "tester",
            22
        )

        memberCustomRepositoryImpl.saveMember(member1)

        val redisCache = memberCustomRepositoryImpl.selectById("123")
        println("redisCache = $redisCache")

        assertEquals(redisCache, mapper.writeValueAsString(member1))
    }
}