package com.ohgiraffers.section05.compositekey.subsection01.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

/* 설명.
 *  Embeddable 타입의 하나의 값의 묶음이자 불변 객체로 다루는 타입이다.
 *  (불변 객체: setter가 없고 필드값에 변화가 생기면 새로운 객체를 생성해야 하는 객체)
 *  그리고 (equals/hashCode)를 오버라이딩 해야 한다.
 *  VO와 유사하며 @EmbeddedID로 복합키로 표현하고자 할 때 쓰기도 한다.
 * */
@Embeddable
public class MemberPK {

    @Column(name="member_no")
    private int memberNo;

    @Column(name="member_id")
    private String memberId;

    public MemberPK() {
    }

    public MemberPK(int memberNo, String memberId) {
        this.memberNo = memberNo;
        this.memberId = memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MemberPK memberPK = (MemberPK) o;
        return memberNo == memberPK.memberNo && Objects.equals(memberId, memberPK.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberNo, memberId);
    }
}
