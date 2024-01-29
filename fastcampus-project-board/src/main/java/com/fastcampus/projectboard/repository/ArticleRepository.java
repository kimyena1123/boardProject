package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import com.fastcampus.projectboard.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

//@Repository
@RepositoryRestResource
public interface ArticleRepository extends JpaRepository<Article, Long>
        ,QuerydslPredicateExecutor<Article>
        , QuerydslBinderCustomizer<QArticle> {

    //여기 안에 구현된 내용을 토대로 검색에 대한 세부적인 규칙이 다시 재구성된다.
    //근데 여기는 interface라 원래는 구현을 넣을 수 없다.
    //근데 java 파일 이후로 그게 가능해졌다.
    //그래서 defualt 메소드로 바꿔준다. 왜냐면 나는 repository layer에서 직접 구현체를 만들지 않을 거다.
    //spring data jpa를 이용해서 interface만 가지고 기능을 다 사용하게끔 접근하고 있다. 그래서 defualt 메소드를 사용하는 게 적절해 보임
    @Override
    default void customize(QuerydslBindings bindings, QArticle root){
        //bindings.excludeUnlistedProperties(): 현재 QuerydslPredicateExecutor 이 기능에 의해서 Article에 있는 모든 필드들에 대한 검색이 열려 있다.
        //근데 그건 내가 원한 게 아니다. 선택적인 필드들만 필터를 하고 싶다. 예를 들어, modifiecAt이나 modifiedBy는 검색 대상에서 제외하고 싶다.
        //그래서 선택적으로 검색이 가능하게 하고 싶은데 그때 이 메소드를 사용할 수 있다.
        //먼저 listing 하지 않은 프로퍼티는 검색에서 제외를 시키는 것을 true로 바꿔준다. (기본값 : false)
        bindings.excludeUnlistedProperties(true);
        //bindings.including(): 이걸로 원하는 필드를 추가하는 것이다. root 이용해서 한다.
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy);
        //title은 argument는 하나만 받는다. 그래서 first 사용
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // like '%s{v}%'
//                bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // like '${v};
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // like '%s{v}%'
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // 이건 문자열이 아니다
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);

    }
}