package com.ohgiraffers.section01.problem;

import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProblemOfUsingDirectSqlTest {

    private Connection con;

    @BeforeEach
    void setConnection() throws SQLException, ClassNotFoundException {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/menudb";
        String user = "swcamp";
        String password = "swcamp";

        Class.forName(driver);

        con = DriverManager.getConnection(url, user, password);
        con.setAutoCommit(false);
    }

    @AfterEach
    void closeConnection() throws SQLException {
        con.rollback();
        con.close();
    }

    /* 설명.
     *  JDBC API를 이용해 직접 SQL을 다룰 때 발생할 수 있는 문제점들
     *  1. 데이터 변환, SQL 작성, JDBC API 코드 등의 중복 작성(개발 시간 증가, 유지보수성 저하)
     *  2. SQL에 의존하여 개발
     *  3. 패러다임 불일치(상속, 연관관계, 객체 그래프 탐색)
     *  4. 동일성 보장 문제
    * */

    /* 설명. 1. 데이터 변환, SQL 작성, JDBC API 코드 등의 중복 작성(개발 시간 증가, 유지보수성 저하) */
    @DisplayName("직접 SQL을 작성하여 메뉴를 조회할 때 발생하는 문제 확인")
    @Test
    void testDirectSelectSql() throws SQLException {
        //given
        String query = "SELECT MENU_CODE, MENU_NAME, MENU_PRICE, CATEGORY_CODE, ORDERABLE_STATUS FROM TBL_MENU";

        //when
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        List<Menu> menuList = new ArrayList<>();
        while(rs.next()){
            Menu menu = new Menu();
            menu.setMenuCode(rs.getInt("MENU_CODE"));
            menu.setMenuName(rs.getString("MENU_NAME"));
            menu.setMenuPrice(rs.getInt("MENU_PRICE"));
            menu.setCategoryCode(rs.getInt("CATEGORY_CODE"));
            menu.setOrderableStatus(rs.getString("ORDERABLE_STATUS"));

            menuList.add(menu);
        }

        //then
        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);

        rs.close();
        stmt.close();
    }

    @DisplayName("직접 SQL을 작성하여 신규 메뉴를 추가할 떄 발생하는 문제 확인")
    @Test
    void testDirectInsertSQL() throws SQLException {

        //given
        Menu menu = new Menu();
        menu.setMenuName("민트초코짜장면");
        menu.setMenuPrice(12000);
        menu.setCategoryCode(1);
        menu.setOrderableStatus("Y");

        String query = "INSERT INTO TBL_MENU(MENU_NAME, MENU_PRICE, CATEGORY_CODE, ORDERABLE_STATUS) VALUES (?, ?, ?, ?)";

        //when
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1,menu.getMenuName());
        pstmt.setInt(2,menu.getMenuPrice());
        pstmt.setInt(3,menu.getCategoryCode());
        pstmt.setString(4,menu.getOrderableStatus());

        int result = pstmt.executeUpdate();

        //then
        Assertions.assertEquals(1,result);
        pstmt.close();
    }

    /* 설명.
     *  2. SQL에 의존하여 개발
     *  요구사항의 변경에 따라 애플리케이션의 수정이 SQL의 수정으로도 이어진다.
     *  이러한 수정 영향을 미치는 것은 오류를 발생시킬 가능성도 있지만 유지보수성에도 악영향을 미친다.
     *  또한 객체를 사용할 때 SQL에 의존하면 객체에 값이 무서이 들어있는지 확인하기 위해 SQL을 매번 살펴야 한다.
    * */

    /* 설명. 2-1 조회 항목 변경 시 */
    @DisplayName("조회 항목 변경에 따른 의존성 확인")
    @Test
    void testChangeSelectColumn() throws SQLException {

        //given
        String query = "SELECT MENU_CODE, MENU_NAME, MENU_PRICE FROM TBL_MENU";

        //when
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        List<Menu> menuList = new ArrayList<>();
        while(rs.next()){
            Menu menu = new Menu();
            menu.setMenuCode(rs.getInt("MENU_CODE"));
            menu.setMenuName(rs.getString("MENU_NAME"));
            menu.setMenuPrice(rs.getInt("MENU_PRICE"));

            menuList.add(menu);
        }

        //then
        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);

        rs.close();
        stmt.close();


    }
}
