package com.redis.demo.domain

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash(value = "member")
data class Member(
    @Id
    var id: String? = null,
    var name: String? = null,
    var age: Int? = null
)
