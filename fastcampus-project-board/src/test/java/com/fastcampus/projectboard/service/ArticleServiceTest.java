package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleUpdateDto;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.RequiredTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

//이 서비스 비즈니스 로직은
// Spring Boot의 슬라이스 테스트 기능을 사용하지 않고 작성하는 방법으로 접근해보자.
//그렇게 함으로써 Spring Boot 애플리케이션 컨텍스트가 뜨는데 걸리는 시간을 없앤다. 가능하면 테스트는 가볍게 만들수록 좋다.
//그래서 여기서는 굳이 불필요한 spring boot application 띄우는 과정이 생기지 않게끔 spring boot의 지원을 받지 않게끔 한다.
//그 대신에 여기에 dependency가 추가되어야 할 부분이 있다면 모킹을 하는 방으로 접근하겠다.
//그래서 많이 사용되는 라이브러리가 => Mockito 가 있다.
//Mockito는 이미 spring 테스트 패키지에 잘 이미 포함되어져 있어서 이걸 따로 설치하는 과정을 밟을 필요가 없다.

//간단한 예제로 설명하면, ArticleServiceTest 클래스에서 ArticleService 클래스를 테스트할 때,
// ArticleRepository의 실제 구현체 대신 Mock 객체를 주입하여 특정 상황에서 어떻게 동작하는지 테스트할 수 있다.
// Mock 객체를 사용하면 외부 의존성을 제어하고 테스트를 더 쉽게 작성할 수 있다.

@DisplayName("비즈니스 로직 - 게시글") // 테스트 코드 부분을 controller라고 생각하자
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    //Modk을 주입하는 대상  : InjectMock을 붙여준다.
    //                  : 나머지는 Mock
    @InjectMocks private ArticleService sut;

    @Mock private ArticleRepository articleRepository;

    //검색 기능
    @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
        // Given
        //한 페이지에 20개의 항목을 표시하기 위한 Pageable 객체를 생성하는 것이다.
        // 이 객체는 게시글을 검색하거나 조회할 때 페이징된 결과를 처리하는 데 사용될 것이다.
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());

        //When
        //결과물을 List를 받아야 함
        //searchType 종류: title, content, hashtag, id
//      List<ArticleDto> articleList = articleBO.getArticleList(SearchType.TITLE, "search Keyword") ; //제목, 본문, ID, 닉네임, 해시태그
        Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);

        //Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findAll(pageable);

    }

    @DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());

        // When
        Page<ArticleDto> articles = sut.searchArticles(searchType, searchKeyword, pageable);

        // Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findByTitleContaining(searchKeyword, pageable);
    }
    
    //각 게시글 페이지로 이동
    @DisplayName("게시글을 조회하면, 게시글을 반환한다.") //예를들어 100개의 게시글 중 50번째 게시글 클릭하면 해당 게시글의 상세정보 보여주기
    // index 페이지에서 여러 개의 게시글 중 하나를 선택하면 id 번호를 기반으로 해당 행의 정보를 가져와서
    // detail 페이지로 가는 거라고 생각하면 된다
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
        // Given
        Long articleId = 1L;
        Article article = createArticle();
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        // When
        ArticleWithCommentsDto dto = sut.getArticle(articleId);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent())
                .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("없는 게시글을 조회하면, 예외를 던진다.")
    @Test
    void givenNonexistentArticleId_whenSearchingArticle_thenThrowsException(){
        // Given
        Long articleId = 0L;
        given(articleRepository.findById(articleId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> sut.getArticle(articleId));

        //Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - articleId: " + articleId);
        then(articleRepository).should().findById(articleId);
    }


    @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle() {
        // given
        //articleRepository.save 메서드가 호출될 때 어떤 Article 객체를 받아들이든지 간에 Articlee 객체를 반환하도록 설정한다.
        ArticleDto dto = createArticleDto();
        given(articleRepository.save(any(Article.class))).willReturn(createArticle());

        //when
        //articleBO 객체의 createArticle 메서드를 호출하면서 ArticleDTO 객체를 전달한다.
        sut.saveArticle(dto);

        //then
        //articleRepository의 save 메서드가 한 번 호출되었는지를 검출한다.
        then(articleRepository).should().save(any(Article.class));

    }

    @DisplayName("게시글의 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenModifiedArticleInfo_whenUpdatingArticle_thenUpdatesArticle() {
        //given
        Article article = createArticle();
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
        given(articleRepository.getReferenceById(dto.id())).willReturn(article);

        //when
        sut.updateArticle(dto);

        //then
        assertThat(article)
                .hasFieldOrPropertyWithValue("title", dto.title())
                .hasFieldOrPropertyWithValue("content", dto.content())
                .hasFieldOrPropertyWithValue("hashtag", dto.hashtag());
        then(articleRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무 것도 하지 않는다.")
    @Test
    void givenNonexistentArticleInfo_whenUpdatingArticle_thenLogsWarningAndDoesNothing() {
        // Given
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
        given(articleRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

        // When
        sut.updateArticle(dto);

        // Then
        then(articleRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        //given
        Long articleId = 1L;
        willDoNothing().given(articleRepository).deleteById(articleId);

        //when
        sut.deleteArticle(1L);

        //then
        then(articleRepository).should().deleteById(articleId);
    }

    @DisplayName("게시글 수를 조회하면, 게시글 수를 반환한다")
    @Test
    void givenNothing_whenCountingArticles_thenReturnsArticleCount() {
        //given
        long expected = 0L;
        BDDMockito.given(articleRepository.count()).willReturn(expected);

        //when
        long actual = sut.getArticleCount();

        //then
        assertThat(actual).isEqualTo(expected);
        BDDMockito.then(articleRepository).should().count();
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
                "uno",
                "password",
                "uno@email.com",
                "Uno",
                null
        );
    }

    private Article createArticle() {
        return Article.of(
                createUserAccount(),
                "title",
                "content",
                "#java"
        );
    }

    private ArticleDto createArticleDto() {
        return createArticleDto("title", "content", "#java");
    }

    private ArticleDto createArticleDto(String title, String content, String hashtag) {
        return ArticleDto.of(1L,
                createUserAccountDto(),
                title,
                content,
                hashtag,
                LocalDateTime.now(),
                "Uno",
                LocalDateTime.now(),
                "Uno");
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "uno",
                "password",
                "uno@mail.com",
                "Uno",
                "This is memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }
}