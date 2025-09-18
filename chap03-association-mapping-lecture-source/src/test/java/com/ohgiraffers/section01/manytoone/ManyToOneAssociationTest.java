package com.ohgiraffers.section01.manytoone;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.*;

public class ManyToOneAssociationTest {
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

    /* 설명.
     *  Association Mapping은 Entity 클래스 간의 관계를 매핑하는 것을 의마한다.(join을 고려함)
     *  이를 통해 객체를 이용해 데이터베이스의 테이블 간의 관계를 활용해 한번에 객체로 조회할 수 있다.
     *
     *  설명.
     *   1. 다중성에 의한 분류
     *     : 연관관계가 있는 객체 관계에서 실제로 연관을 가지는 객체의 수에 따라 분류한다.
     *      - N:1(ManyToOne) 연관관계
     *      - 1:N(OneToMany) 연관관계
     *      - 1:1(OneToOne) 연관관계
     *      - M:N(ManyToMany) 연관관계(교차 엔티티에 해당하는 중간 테이블을 자동으로 만듦)
     *   2. 방향에 따른 분류
     *     : 테이블의 연관 관계는 외래 키를 이용하여 양방향 연관 관계의 특징을 가진다.
     *       반면, 객체는 참조에 의한 연관관계로 단방향이다.
     *       객체 간의 연관 관계를 양방향으로 만들고 싶을 경우 반대 쪽에서도 필드를 추가해서 참조를 보관하면 된다.
     *       하지만, 엄밀하게는 양방향 관계가 나이라 단방향 관계 2개로 볼 수 있다.
     *      - 단방향(UniDirectional) 연관관계
     *      - 양방향(BiDirectional) 연관관계
    * */
    @Test
    public void 다대일_연관관계_객체_그래프_탐색을_이용한_조회_테스트() {
        int menuCode = 15;

        /* 설명. 만약에 메뉴만 보고 싶다면?
         *      : FetchType.EAGER가 아닌 FetchType.LAZY로 바꿔 주어야 한다.(toString에서 접근하지 않게 해야한다.)
         *      조인도 하고 싶다면?
         *      : jpql을 활용한 fetch join을 진행하면 된다.
        * */
        /* 설명. 1. 메뉴만 조회한 경우 */
        MenuAndCategory foundMenu = entityManager.find(MenuAndCategory.class, menuCode);
        System.out.println("foundMenu = " + foundMenu);

        /* 설명. 2. 카테고리와 fetch 조인을 하는 경우 */
        String jpql = "select m from menu_and_category m join fetch m.category where m.menuCode ="
                + menuCode;
        TypedQuery<MenuAndCategory> typeMenu = entityManager.createQuery(jpql, MenuAndCategory.class);
        MenuAndCategory menu = typeMenu.getSingleResult();
        System.out.println("menu = " + menu);

        System.out.println("카테고리만 확인: " + menu.getMenuCode());
        System.out.println("카테고리의 이름확인: " + menu.getCategory().getCategoryCode());
    }
}
