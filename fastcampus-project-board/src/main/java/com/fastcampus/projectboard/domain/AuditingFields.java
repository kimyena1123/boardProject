package com.fastcampus.projectboard.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter // 각 필드에 접근 가능해야 하기에
@ToString //lombok의 toString
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AuditingFields { //이렇게 추출한 클래스 필드들을 어떻게 article과 연결시킬까? -> 상속, Aritcle에서 상속

    //createdAt과 creatdBy는 컬럼에 옵션을 추가하도록 하겠다.
    //updatable과 insertable이라는 옵션이 추가로 있는데, updatable만 기준으로 봤을 때, 이 필드는 업데이트가 불가하다는 걸 명시하는 것이다.

    //이 데이터들이 실제로 웹 화면에 보여줄 때, 그리고 웹 화면에서 파라미터를 받아서 세팅을 할 때 파싱이 잘 돼야 한다.
    //파싱에 대한 룰을 넣어줘야 하는데, spring 프레임워크 포매터 관련된 어노테이션 중에 @DateTimeFormat이 있다. 이걸 통해서 할 수 있는 방법이 있다.
    //iso 객체(클래스)를 쓰는 방법, 패턴으로 문자를 넣는 방법 -> 이렇게 2가지 쓸 수 있다(나는 iso 사용)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성일시 (not null)

    @CreatedBy
    @Column(nullable = false, updatable = false, length = 100)
    private String createdBy; // 생성자 (not null)

    //modifiedAt과 modifiedBy는 수정이 수시로 일어나니까 updatable의 옵션을 넣을 수 없다.

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt; // 수정일시 (not null)

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy; // 수정자 (not null)

}
