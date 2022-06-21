package com.redis.demo.repository

import com.redis.demo.domain.Member
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : CrudRepository<Member, String>//, MemberCustomRepository