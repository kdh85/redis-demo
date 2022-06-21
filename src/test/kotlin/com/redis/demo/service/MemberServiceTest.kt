package com.redis.demo.service

import com.redis.demo.TestRedisConfiguration
import com.redis.demo.domain.Member
import com.redis.demo.repository.MemberRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import


@SpringBootTest
@Import(TestRedisConfiguration::class)
internal class MemberServiceTest (
    @Autowired
    private var memberService: MemberService,
    @Autowired
    private var memberRepository: MemberRepository
) {

    @BeforeEach
    private fun setUp() {
        memberService = MemberService(memberRepository)
    }

    @AfterEach
    @Throws(Exception::class)
    private fun tearDown() {
        memberRepository.deleteAll()
    }

    @Test
    fun getMemberInRedisTest() {

        val member1 = Member(
            "123",
            "tester",
            22
        )

        memberService.saveMember(member1)

        val cacheMember = memberService.getMember("123")
        assertEquals(member1, cacheMember)
    }
}