package com.redis.demo.repository

import com.redis.demo.domain.Member
import org.springframework.stereotype.Repository

@Repository
interface MemberCustomRepository {
    fun selectById(id: String): String?
    fun saveMember(member: Member)
    fun deleteMember(id: String): Boolean
}