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
import java.util.Objects;


@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class ArticleComment extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //사실 private int articleId라고 해서 연관관계 매핑 하지 않고 해도 되지만 객체지향적으로
    //댓글을 변경하거나 지웠을 때 관련되어 있는 게시글이 영향을 받아야 하는가 생각해보면 -> 댓글은 혼자 깔끔하게 지워지고 게시글은 영향받지 말아야 한다.
    //그래서 cascading 옵션을 주지 않겠다. 이럴 경우 기본값은 none이다.
    @Setter @ManyToOne(optional = false) private Article article; // 게시글 (ID)

    @Setter @Column(nullable = false, length = 500) private String content; // 댓글 내용


    protected ArticleComment() {}
    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }
    public static ArticleComment of(Article article, String content) {
        return new ArticleComment(article, content);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
