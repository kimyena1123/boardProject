package com.fastcampus.projectboard.domain;

import jakarta.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mysql의 auto increment는 indentity 방식으로 auto increment가 만들어진다.
    private Long id;

    //setter를 클래스의 전체 애들한테 걸지 않는 이유는 일부러 사요앚가 특정 필드에 접근한 세팅을 하지 못하게끔 막고 싶어서! 예를 들면 id
    //id는 내가 부여하는게 아니라 JPA persistence Context가 영속성을 연속화 할 때 자동으로 부여해주는 고유번호이다.
    //특히 auto increment로 자동으로 번호를 만든다.
    //메타데이터도 내가 세팅하는게 아니라 자동으로 JPA가 세팅을 해주게끔 이따 만들 것이다.
    //그렇기 때문에 setter를 열어두게 되면 내가 임의의로 메타데이터 내용을 바꿀 수 있게 되는데, 그것은 내 설계의도에 벗어나게 된다.
    //이런 이유로 전체 클래스에디가 setter를 사용하지 않는 것임

    //setter 필요 -> setter는 어떨 때 필요? 수정이 필요할 때!
    @Setter @Column(nullable = false) private String title; // 제목(not null)
    @Setter @Column(nullable = false, length = 10000) private String content;// 본문(not null)

    @Setter private String hashtag; // 해시태그(null 가능)

    //One to Many 관계를 보여줘야 한다.
    //이 article에 연동되어 있는 comment(댓글)은 중복을 허용하지 않고 다 여기에서 모아서 Collection으로 보겠다는 의도로 One to many으로 보여주는 것임
    @ToString.Exclude
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    //메타 데이터
    //내가 세팅하는게 아니라 자동으로 JPA가 세팅을 해주게끔 이따 만들 것이다.
    //이때 사용되는 것이 JPA의 auditing이라는 기술이다.

    //생성자: 누가 만들었는지를 알아야 하는데, 지금 나는 spring security나 인증기능을 달아놓지 않았다.
    //그러니까 어떤 테이블에 데이터를 집어넣으려고 할 때, 누가 만들었는지에 대한 정보는 어디에도 지금 제공하지 않다.
    //이것을 세팅하기 위해서 JPA config으로 돌아가서 설정한다.
    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt; // 생성일시 (not null)
    @CreatedBy @Column(nullable = false, length = 100) private String createdBy; // 생성자 (not null)
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시 (not null)
    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy; // 수정자 (not null)

    //모든 JPA Entity들은 Hibernate 구현체를 사용하는 경우를 기준으로 설명을 드릴 때 "기본 생성자"를 가지고 있어야 한다.
    //public Article() {}: 기본 생성자는 평소에는 오픈하지 않을 것이기 때문에 public으로 할 이유가 없고 protected로 할 것이다.
    //private은 안된다. 오류가 난다. Article class는 entity이기 때문에 public이나 protected, 아무 인자 없는 기본 생성자를 필요로 한다.
    protected Article() {}

    //생성자 하나더 생성

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    //domain Article을 생성하고자 할 때, 어떤 값을 필요로 한다는 걸 이것으로 가이드 하는 것이다.
    //제목과 본문, 해시태그를 넣어주세요' 라고 하는 것임
    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    //게시글에 리스트를 이용해서 게시판 화면을 구성한다던가 할 때는 리스트로 어떤 데이터를 받아오고 내보내주고 하는 니즈가 있을 수 있다.
    //그러면 리스트에 넣거나 혹은 리스트에 있는 내용에서 중복 요소를 제거하거나 컬렉션에서 중복 요소를 제거하거나 혹은 정렬을 해야 할 때,
    //비교를 할 수 있어야 될 것이다. => 그래서 동일성-동등성 검사를 할 수 있는 equals과 hashcode를 구현해야 한다.
    //이걸 lombok을 이용하면 간단하게 할 수 있다 => @EqualsAndHashCode가 있다. 하지만 이걸 사용하진 않을 것이다.
    //  왜? 이 어노테이션을 사용하게 되면 기본적인 방식으로 equals  hashcode가 구성되는데, 그말인즉 여기 있는 필드를 모두 다 비교해서
    //      보통의 표준적인 방법으로 equals hashcode를 구현하는 것이다. 근데 나는 Entity에서만큼은 독득한 방법으로 equals hashcode를 만들어야 한다.
    //먼저 뼈대인 skeleton 코드를 만들어볼 것이다.
    //필드를 보는데, 동등성 검사를 하기 위해서 각 필드가 모두 맞는지 검사할 필요가 없다.
    // 데이터베이스에 연동되어 있는 각 raw data가 서로 동일한지의 기준은 "id"만 같은지만 보면 된다.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id != null && id.equals(article.id);
        //지금 막 만든 아직 영속화되지 않은 엔테테는 모두 동등성 검사를 탈락한다.

    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
