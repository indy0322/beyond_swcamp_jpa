package com.ohgiraffers.section03.primarykey.subsection01.identity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class PrimaryKeyMappingTest {
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager() {

        /* 설명. EntityManager가 생성될때 마다 고유의 새로운 영속성 컨텍스트(Entity 객체를 관리하는 창고)가 생성된다. */
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    public void CloseManger(){
        entityManager.close();
    }

    @AfterAll
    public static void closeFactory(){
        entityManagerFactory.close();
    }

    @Test
    public void 식별자_매핑_테스트() {
        Member member = new Member();
//        member.setMemberNo(1);
        member.setMemberId("user01");
        member.setMemberPwd("pass01");
        member.setNickname("홍길동");
        member.setEmail("hong@gmail.com");
        member.setAddress("서울시 서초구");
        member.setEnrollDate(new java.util.Date());
        member.setMemberRole("ROLE_MEMBER");
        member.setStatus("Y");

        Member member2 = new Member();
//        member.setMemberNo(1);
        member.setMemberId("user02");
        member.setMemberPwd("pass02");
        member.setNickname("유관순");
        member.setEmail("yu@gmail.com");
        member.setAddress("서울시 강남구");
        member.setEnrollDate(new java.util.Date());
        member.setMemberRole("ROLE_ADMIN");
        member.setStatus("Y");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        System.out.println("persist 전 member: " + member);
        entityManager.persist(member);
        entityManager.persist(member2);
        System.out.println("persist 후(flush 된 후) member: " + member);
        /* 설명. persist 당시에는 부여되지 않는 pk값으로 commit 이후 조회를 하면 가능할까? */
        Member selectedMember = entityManager.find(Member.class, 1);
        System.out.println("selectedMember = " + selectedMember);

        transaction.commit();
        
        /* 설명. insert 전에 영속상태의 엔티티 객체에는 pk값이 없었지만 persist 이후 find 시에는 존재 */
        /* 설명. persist 시점에 insert가 날아감 */
    }
}
