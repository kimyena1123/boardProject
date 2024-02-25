package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.config.SecurityConfiguration;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.service.ArticleService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Set;

import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfiguration.class)
@WebMvcTest(ArticleController.class) // 컨트롤러가 여러 개 있을 때 다 배제시키고 여기 입력한 컨트롤러만 읽는다.
class ArticleControllerTest {

    private final MockMvc mvc;

    @MockBean private ArticleService articleService;

    public ArticleControllerTest(@Autowired MockMvc mvc) { //@Autowired를 생략할 수 없으니까 꼭 추가해주는 거 잊으면 안된다. 
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지  - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        //given
        //검색어 없는 경우
        BDDMockito.given(articleService.searchArticles(ArgumentMatchers.eq(null), ArgumentMatchers.eq(null), ArgumentMatchers.any(Pageable.class))).willReturn(Page.empty());

        //when & then
        mvc.perform(get("/articles")) // controller 페이지의 ArticleController 파일의 articles 메소드가 있다.
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // content 내용의 type이 무엇인지. view이니까 text html
                .andExpect(MockMvcResultMatchers.view().name("articles/index")) // view name에 대한 검사: 여기에 view가 있어야 한다.
                .andExpect(MockMvcResultMatchers.model().attributeExists("articles")); // 이 view는 데이터가 있어야 한다. 게시판 페이지를 보면 이번 페이지에 보여줘야 될 게시글 목록이 떠야 한다.
//                .andExpect(MockMvcResultMatchers.model().attributeExists("searchTypes"));
                //articles 테이블의 정보를 가져와서 보여줌


        //then
        BDDMockito.then(articleService).should().searchArticles(ArgumentMatchers.eq(null), ArgumentMatchers.eq(null), ArgumentMatchers.any(Pageable.class));
    }


//    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 상세 페이지  - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        //given
        Long articleId = 1L;

        //comment를 들고 있는 게시글 페이지가 만들어짐
        BDDMockito.given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());

        //when & then
        mvc.perform(get("/articles/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.view().name("articles/detail"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("article")) // 이때는 게시글 데이터가 넘겨져야 한다.
                .andExpect(MockMvcResultMatchers.model().attributeExists("articleComments")); // 게시글 페이지는 댓글도 보여야 한다.
                //articleComment 테이블을 가져와서 정보를 보여줄 거임 그래서 articleComment 테이블이 필요한 거임

        //then
        BDDMockito.then(articleService).should().getArticle(articleId);

    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleSearchview_thenReturnsArticleSearchView() throws Exception {
        //given

        //when & then
        mvc.perform(get("/articles/sesarch"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articles/search")); // 이때는 게시글 데이터가 넘겨져야 한다.
//                .andExpect(MockMvcResultMatchers.model().attributeExists("articles")); //검색하면 아직 데이터가 없어야 한다.
    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleHasgtagSearchview_thenReturnsArticleHasgtagSearchView() throws Exception {
        //given

        //when & then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articles/search-hashtag "));

//                .andExpect(MockMvcResultMatchers.model().attributeExists("articles")); //검색하면 아직 데이터가 없어야 한다.
    }




    private ArticleWithCommentsDto createArticleWithCommentsDto(){
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

    private UserAccountDto createUserAccountDto(){
        return UserAccountDto.of(
                1L,
                "uno",
                "pw",
                "uno@gmail.com",
                "Uno",
                "memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }


}
