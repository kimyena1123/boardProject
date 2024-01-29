package com.fastcampus.projectboard.domain;

import jakarta.persistence.*;

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
@Entity
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
})
public class Article extends AuditingFields{ //이렇게 하면 이제 auditingFields 안에 있는 모든 4개 필드는 article의 일부가 된다. $
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //setter 필요 -> setter는 어떨 때 필요? 수정이 필요할 때!
    @Setter @Column(nullable = false) private String title; // 제목(not null)
    @Setter @Column(nullable = false, length = 10000) private String content;// 본문(not null)

    @Setter private String hashtag; // 해시태그(null 가능)

    //Article과 ArticleComment 테이블 간 관계: One to Many 관계를 보여줘야 한다.
    @ToString.Exclude
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();


    //Entity는 기본 생성자 필수!! JPA 스펙상 규정이 되어 있다.
    protected Article() {}

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
