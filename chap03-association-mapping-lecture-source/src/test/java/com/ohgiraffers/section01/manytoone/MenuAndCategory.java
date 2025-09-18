package com.ohgiraffers.section01.manytoone;

import jakarta.persistence.*;

@Entity(name="menu_and_category")
@Table(name="tbl_menu")
public class MenuAndCategory {
    @Id
    @Column(name="menu_code")
    private int menuCode;

    @Column(name="menu_name")
    private String menuName;

    @Column(name="menu_price")
    private int menuPrice;

    /* 설명.
     *  FetchType.LAZY(지연 로딩)와 FetchType.EAGER(즉시 로딩)
     *  1. FetchType.LAZY는 연관관계를 미리 고려해서 해당 엔티티와 조인하는 쿼리가 작성됨
     *     (항상 조인)
     *  2. FetchType.LAZY는 연관관계를 나중에 필요한 시점에 고려하여 따로 select을 날려서
     *     추가하는 개념(나중에 해당 필드를 접근하면 쿼리가 따로 발생)
     *     (toString()에서 접근하는 것도 해당되므로 고려할 것)
    * */
    @ManyToOne(fetch = FetchType.LAZY)   // 메뉴 -> 카테고리의 전체 카디널리티
    @JoinColumn(name="category_code")  //FK 제약조건이 있는 칼럼(자식 테이블에 있는 칼럼명만 쓴다.)
    private Category category;  // 메뉴 1개가 카테고리 엔티티 객체 몇개를 가지는지(List<타입>/타입)

    @Column(name = "orderable_status")
    private String orderableStatus;

    public MenuAndCategory() {
    }

    public MenuAndCategory(int menuCode, String menuName, int menuPrice, Category category, String orderableStatus) {
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.category = category;
        this.orderableStatus = orderableStatus;
    }

    public int getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(int menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getOrderableStatus() {
        return orderableStatus;
    }

    public void setOrderableStatus(String orderableStatus) {
        this.orderableStatus = orderableStatus;
    }

    @Override
    public String toString() {
        return "MenuAndCategory{" +
                "menuCode=" + menuCode +
                ", menuName='" + menuName + '\'' +
                ", menuPrice=" + menuPrice +
                ", category=" + category +
                ", orderableStatus='" + orderableStatus + '\'' +
                '}';
    }
}
