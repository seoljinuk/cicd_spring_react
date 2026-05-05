package com.coffee.repository;

import com.coffee.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 이메일을 사용하여 해당 회원 정보를 조회하는 쿼리 메소드
    Member findByEmail(String email);
}