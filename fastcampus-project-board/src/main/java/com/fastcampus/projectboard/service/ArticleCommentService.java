package com.fastcampus.projectboard.service;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.repository.ArticleCommentRepository;
import com.fastcampus.projectboard.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
//다양한 로깅 프레임 워크에 대한 추상화(인터페이스) 역할을 하는 라이브러리
@Slf4j
//Lombok으로 스프링에서 DI(의존성 주입)의 방법 중에 "생성자 주입"을 임의의 코드없이 자동으로 설정해주는 어노테이션
//초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성해줌
//새로운 필드를 추가할 때 다시 생성자를 만들어서 관리해야하는 번거로움을 없애준다. (@Autowired를 사용하지 않고 의존성 주입)
@RequiredArgsConstructor
//스프링에서 트랜잭션 처리를 위해 선언적 트랜잭션을 사용
//CheckedException or 예외가 없을 때는 Commit
//UncheckedException이 발생하면 Rollback
//클래스 메서드에 선언된 트랜잭션의 우선순위가 가장 높고, 인터페이스에 선언된 트랜잭션의 우선순위가 가장 낮다 : 클래스 메소드 -> 클래스 -> 인터페이스 메소드 -> 인터페이스
@Transactional
@Service
public class ArticleCommentService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;


    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComments(Long articleId) {
        return articleCommentRepository.findByArticle_Id(articleId)
                .stream()
                .map(ArticleCommentDto::from)
                .toList();
    }

    public void saveArticleComment(ArticleCommentDto dto) {
        try{
            //게시글의 id를 가져와 그 게시글에 새로운 댓글을 추가(create)
            articleCommentRepository.save(dto.toEntity(articleRepository.getReferenceById(dto.articleId())));
        }catch (EntityNotFoundException e){
            log.warn("댓글 저장 실패. 댓글의 게시글을 찾을 수 없습니다. - dto: {}", dto);
        }
    }

    public void updateArticleComment(ArticleCommentDto dto) {
        try{
            //댓글의 id를 가져와 id에 해당하는 정보를 rticleComment에 저장. 댓글의 id를 못찾으면 로그로 에러 출력
            //그 후 setContent로 내용 변경(update)
            ArticleComment articleComment = articleCommentRepository.getReferenceById(dto.id());
            if(dto.content() != null){
                articleComment.setContent(dto.content());
            }
        }catch (EntityNotFoundException e){
            log.warn("댓글 업데이트 실패, 댓글을 찾을 수 없습니다. = dto: {}", dto);
        }
    }

    public void deleteArticleComment(Long articleCommentId) {
        //댓글의 id를 찾아 그대로 해당 댓글 삭제
        articleCommentRepository.deleteById(articleCommentId);
    }
}
