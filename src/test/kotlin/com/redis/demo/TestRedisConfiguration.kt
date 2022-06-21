package com.redis.demo

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@TestConfiguration
class TestRedisConfiguration(
    @Value("\${spring.redis.host}")
    private val host: String,
    @Value("\${spring.redis.port}")
    private val port: Int
) {

    private val redisServer: RedisServer = RedisServer(port)

    @PostConstruct
    fun postConstruct() {
        println("------------------Embed redis server start!------------------")
        redisServer.start()
    }

    @PreDestroy
    fun preDestroy() {
        println("------------------Embed redis server end!------------------")
        redisServer.stop()
    }
}