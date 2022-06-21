package com.redis.demo.service

import com.redis.demo.domain.Member
import com.redis.demo.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class MemberService(
    @Autowired
    private val memberRepository: MemberRepository
) {
    fun getMemberById(id : String): Member {
        return memberRepository.findById(id).get()
    }

    fun saveMember(member: Member){
        memberRepository.save(member)
    }

    @Cacheable(value = ["member"], key = "#id", cacheManager = "memberManager")
    fun getMember(id: String): Member {
        return memberRepository.findById(id).get()
    }
}