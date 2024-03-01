<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>게시글 페이지</title>

    <#--    bootstrap cdn-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
</head>
<body>
<!-- insert나 replace 중에 쓰기. include도 있지만 권장X.  {} 안에 view 이름을 넣어주면 된다.-->

    <#include "*/include/header.ftl">

<main id="article-main" class="container">
    <header id="article-header" class="py-5 text-center">
        <h1>${articleId}번째 글</h1>
    </header>
    
        <div class="row g-5">
            <section class="col-md-5 col-lg-4 order-md-last">
                <aside>
                    <p><span id="nickname">${article.nickname()}</span></p>
                    <p><a id="email" href="mailto:djkehh@gmail.com">${article.email()}</a></p>
                    <p><time id="created-at" datetime="2022-01-01T00:00:00">${article.createdAt()}</time></p>
                    <p><span id="hashtag">${article.hashtag()?default('')}</span></p>
                </aside>
            </section>

            <article id="article-content" class="col-md-7 col-lg-8 border-end">
                <p>[본문]<br></p>
                <p>${article.content()}</p>
            </article>
        </div>

        <div class="row g-5">
            <section>
                <br>
                <form class="row g-3">
                    <div class="col-8">
                        <label for="comment-textbox" hidden>댓글</label>
                        <textarea class="form-control" id="comment-textbox" placeholder="댓글 쓰기.." rows="3"></textarea>
                    </div>
                    <div class="col-8">
                        <label for="comment-submit" hidden>댓글 쓰기</label>
                        <button class="btn btn-primary" id="comment-submit" type="submit" style="float: right;">쓰기</button>
                    </div>
                </form>

                <ul id="article-comments" class="row col-7">
                    <#list articleComments as articleComment>
                        <#if articleComment_index == 0>
                        <#-- 첫 번째 댓글은 건너뜁니다 -->
                        <#else>
                            <li>
                                <div>
                                    <small>${articleComment.createdAt()}</small>
                                    <strong>${articleComment.nickname()}</strong>
                                    <p>${articleComment.content()}</p>
                                </div>
                            </li>
                        </#if>
                    </#list>
                </ul>
            </section>
        </div>

        <div class="row g-5">
            <nav id="pagination" aria-label="Page navigation">
                <ul class="pagination">

                    <li class="page-item">
                        <#if articleId - 1 <= 0>
                            <a class="page-link disabled" href="#" aria-label="Previous">
                                <span aria-hidden="true">&laquo; prev</span>
                            </a>
                        <#else>
                            <a class="page-link" href="/articles/${articleId - 1}" aria-label="Previous">
                                <span aria-hidden="true">&laquo; prev</span>
                            </a>
                        </#if>
                    </li>

                    <li class="page-item">
                        <#if articleId + 1 gt totalCount>
                            <a class="page-link disabled" href="#" aria-label="Next">
                                <span aria-hidden="true">next &raquo;</span>
                            </a>
                        <#else>
                            <a class="page-link" href="/articles/${articleId + 1}" aria-label="Next">
                                <span aria-hidden="true">next &raquo;</span>
                            </a>
                        </#if>
                    </li>
                </ul>
            </nav>

        </div>




    </main>
    <#include "*/include/footer.ftl">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>

</body>
</html>
