package com.redis.demo.repository.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.redis.demo.domain.Member
import com.redis.demo.repository.MemberCustomRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import java.util.concurrent.TimeUnit

class MemberCustomRepositoryImpl (
    @Qualifier(value = "memberRedisTemplate")
    private var redisTemplate: RedisTemplate<String, String>,
    private var mapper: ObjectMapper
) : MemberCustomRepository {

    override fun selectById(id: String): String? {
        return redisTemplate.opsForValue().get(id)
    }

    override fun saveMember(member: Member) {
        redisTemplate.opsForValue().set(
            member.id!!,
            mapper.writeValueAsString(member),
            60,
            TimeUnit.SECONDS
        )
    }

    override fun deleteMember(id: String): Boolean {
        return redisTemplate.delete(id)
    }
}
