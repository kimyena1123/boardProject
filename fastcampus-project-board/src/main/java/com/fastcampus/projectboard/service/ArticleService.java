package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor // 필수 필드에 대한 생성자를 자동으로 만들어준다.
@Transactional
@Service
public class ArticleService  {

    private final ArticleRepository articleRepository;

    //메서드가 읽기 전용 트랜잭션으로 실행되도록 설정.
    //검색 기능이므로 데이터를 변경하지 않고 읽기만 하는 트랜잭션을 사용.
    @Transactional(readOnly = true) //검색은 변경하는 게 아니라 리딩하는 것.
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {

        if(searchKeyword == null || searchKeyword.isBlank()){
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        //keyword가 있다면
        return switch (searchType){
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_Nickname(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDto::from);
        };
    }



    @Transactional(readOnly = true)//검색은 변경하는 게 아니라 리딩하는 것.
    public ArticleWithCommentsDto getArticle(Long articleId) { //단건 조회
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    public void saveArticle(ArticleDto dto) {
        articleRepository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDto dto) {
        try{
            Article article = articleRepository.getReferenceById(dto.id());

            //수정은 크게 3가지 할 수 있다. => title, content, hashtag
            //title과 content는 null이 들어가며 안된다(not null).
            // 그런데 dto에 null이 들어있었으면 문제가 된다. 그래서 앞에 방어 로직을 추가해준다.
            if(dto.title() != null) {article.setTitle(dto.title());}
            if(dto.content() != null) {article.setContent(dto.content());}
            //근데 이 중에 hashtag는 null field이다.
            article.setHashtag(dto.hashtag());

            //아래 코드가 없어도 된다.
            //왜? transactional이 끝날 때 영속성 컨텍스트는 article이 변한 것을 감지해낸다.
            articleRepository.save(article);
        }catch(EntityNotFoundException e){
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto: {}", dto);
        }

    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
