package com.ohgiraffers.section05.compositekey.subsection01.embedded;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class EmbeddedKeyTest {
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @Test
    public void 임베디드_아이디를_사용한_복합키_테이블_매핑_테스트() {
        Member member = new Member();
        member.setMemberPK(new MemberPK(1, "user01"));
        member.setPhone("010-1234-5678");
        member.setAddress("서울시 종로구");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(member);

        transaction.commit();

        Member foundMember =
                entityManager.find(Member.class, new MemberPK(1, "user01"));
        Assertions.assertEquals(member.getMemberPK(), foundMember.getMemberPK());
    }
}
