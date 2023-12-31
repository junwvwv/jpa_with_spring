package com.ex.repository;

import com.ex.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void member() {

        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertThat(savedMember.getId()).isEqualTo(findMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void crud() {

        Member memberA = new Member("memberA");
        Member memberB = new Member("memberB");
        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        Member findMemberA = memberJpaRepository.findById(memberA.getId()).get();
        Member findMemberB = memberJpaRepository.findById(memberB.getId()).get();
        assertThat(findMemberA).isEqualTo(memberA);
        assertThat(findMemberB).isEqualTo(memberB);

        List<Member> members = memberJpaRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        memberJpaRepository.delete(memberA);
        memberJpaRepository.delete(memberB);

        long deletedCount = memberJpaRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThan() {

        Member memberA = new Member("KIM", 10);
        Member memberB = new Member("KIM", 20);
        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("KIM", 15);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getUsername()).isEqualTo("KIM");
        assertThat(result.get(0).getAge()).isEqualTo(20);
    }

    @Test
    void namedQuery() {

        Member memberA = new Member("memberA");
        Member memberB = new Member("memberB");
        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        List<Member> result = memberJpaRepository.findByUsername("memberA");
        assertThat(result.get(0).getUsername()).isEqualTo("memberA");
    }

    @Test
    void paging() {

        memberJpaRepository.save(new Member("memberA", 10));
        memberJpaRepository.save(new Member("memberB", 10));
        memberJpaRepository.save(new Member("memberC", 10));
        memberJpaRepository.save(new Member("memberD", 10));
        memberJpaRepository.save(new Member("memberE", 10));

        int age = 10;
        int offset = 1;
        int limit = 3;

        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
    }

    @Test
    void bulkUpdate() {

        memberJpaRepository.save(new Member("memberA", 10));
        memberJpaRepository.save(new Member("memberB", 15));
        memberJpaRepository.save(new Member("memberC", 20));
        memberJpaRepository.save(new Member("memberD", 25));
        memberJpaRepository.save(new Member("memberE", 30));

        int updatedCount = memberJpaRepository.bulkAgePlus(20);
        assertThat(updatedCount).isEqualTo(3);
    }

}