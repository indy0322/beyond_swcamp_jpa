package com.ohgiraffers.section03.persistencecontext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class EntityLifeCycleTest {

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
    public void 비영속성_테스트(){
        Menu foundMenu = entityManager.find(Menu.class, 10);

        /* 설명. 비영속 상태의 newMenu */
        Menu newMenu = new Menu();
        newMenu.setMenuCode(foundMenu.getMenuCode());
        newMenu.setMenuName(foundMenu.getMenuName());
        newMenu.setMenuPrice(foundMenu.getMenuPrice());
        newMenu.setCategoryCode(foundMenu.getCategoryCode());
        newMenu.setOrderableStatus(foundMenu.getOrderableStatus());

        Assertions.assertFalse(foundMenu == newMenu); // 동등하지만 동일하지는 않다.
    }

    @Test
    public void 영속성_테스트(){
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 11);

        Assertions.assertTrue(foundMenu1 == foundMenu2);
    }

    @Test
    public void 영속성_객체_추가_테스트(){
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Menu menuToRegist = new Menu();
        menuToRegist.setMenuCode(110);   //Menu의 @GeneratedValue(strategy = GenerationType.IDENTITY)를 주석처리해야만 작동한다. 자동으로 증가하는 속성은 마음대로 지정할 수 없기 때문이다.
        menuToRegist.setMenuName("수박죽");
        menuToRegist.setMenuPrice(100000);
        menuToRegist.setCategoryCode(10);
        menuToRegist.setOrderableStatus("Y");

        entityManager.persist(menuToRegist);
        Menu foundMenu = entityManager.find(Menu.class, 110);

        transaction.commit();

        Assertions.assertTrue(foundMenu == menuToRegist);
    }

    @Test
    public void 영속성_객체_추가_및_변경_테스트(){
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Menu menuToRegist = new Menu();
        menuToRegist.setMenuCode(111);
        menuToRegist.setMenuName("수박죽");
        menuToRegist.setMenuPrice(100000);
        menuToRegist.setCategoryCode(10);
        menuToRegist.setOrderableStatus("Y");

        entityManager.persist(menuToRegist);
        menuToRegist.setMenuName("메론죽");

        Menu foundMenu = entityManager.find(Menu.class, 111);

        transaction.commit();

        Assertions.assertEquals("메론죽", foundMenu.getMenuName());
    }

    @Test
    public void 준영속성_detach_테스트(){
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Menu foundMenu1 = entityManager.find(Menu.class, 11);  // select
        Menu foundMenu2 = entityManager.find(Menu.class, 12);  // select
        System.out.println("foundMenu1 = " + foundMenu1);
        System.out.println("foundMenu2 = " + foundMenu2);

        /* 설명.
         *  영속성 컨텍스트가 관리하던 엔티티 객체를 잠시 관리하지 않는 상태가 되게 한 것을 준영속 상태라고 한다.
         *  detach, clear, close가 준영속 상태를 만들기 위한 메소드이다.
         *
         * 설명.
         *   준영속을 사용하는 이유
         *   : JPA에서 엔티티를 더 이상 영속성 컨텍스트에서 관리하지 않음을 의미하며,
         *    특정 상황(flush를 특정 시점에 별도로 사용)에서 성능 최적화나
         *    데이터 무결성 유지, 특정 작업 후 엔티티 변경 방지를 위해 사용 된다.
         *    (DB에서 가져온 객체를 DB에 영향을 크게 주지 않는 선에서 활용하고 싶을 때, 둘 다 활용도 가능)
        * */
        entityManager.detach(foundMenu2);

        foundMenu1.setMenuPrice(7000);
        foundMenu2.setMenuPrice(7000);
        
        transaction.commit();
    }

    @Test
    public void 준영속성_clear_close_테스트() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);

//        entityManager.clear();  // 영속성 컨텍스트에 있는 모든 영속 상태의 엔티티를 준영속으로 변경
        entityManager.close();     // 기존의 영속 상태의 엔티티들이 모두 준영속 상태가 되면서 영속성 컨텍스트가 파괴됨
        entityManager = entityManagerFactory.createEntityManager();

        foundMenu1.setMenuPrice(11000);
        foundMenu2.setMenuPrice(11000);

        transaction.commit();

        Assertions.assertNotEquals(8000, entityManager.find(Menu.class, 11).getMenuPrice());
        Assertions.assertNotEquals(8000, entityManager.find(Menu.class, 12).getMenuPrice());
    }

    @Test
    public void 병합_merge_수정_테스트() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Menu menuToDetach = entityManager.find(Menu.class, 5);
        entityManager.clear();

        menuToDetach.setMenuName("수박죽");

        Menu refoundMenu = entityManager.find(Menu.class, 5);
        System.out.println("첫 번쨰 5번 메뉴의 이름: " + menuToDetach.getMenuName());
        System.out.println("두 번쨰 5번 메뉴의 이름: " + refoundMenu.getMenuName());
        entityManager.merge(menuToDetach);

        /* 설명. 결과적으로 준영속 상태였던 것과 DB에서 다시 조회한 것 중에 어떤 것이 살아남았는지 확인하기 위해 다시 find */
        Menu managedMenu = entityManager.find(Menu.class, 5);

        transaction.commit();

        Assertions.assertEquals("수박죽", managedMenu.getMenuName());
    }
}
