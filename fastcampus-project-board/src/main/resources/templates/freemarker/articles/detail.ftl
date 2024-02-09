<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>게시글 페이지</title>
</head>
<body>
<!-- insert나 replace 중에 쓰기. include도 있지만 권장X.  {} 안에 view 이름을 넣어주면 된다.-->

    <#include "*/include/header.ftl">
    <main>
        <header><h1>첫번째 글</h1></header>

        <aside>
            <p><span class="nick-name">Uno</span></p>
            <p><a class="u-url" rel="me" href="kimyena1123@naver.com">kimyena1123@naver.com</a></p>
            <p><time datetime="2024-02-09Too:oo:oo">2024-02-09</time> </p>
            <p>#java</p>
        </aside>

        <section>
            <p>본문<br><br></p>

            <div>
                <form>
                    <label for="articleComment" hidden>댓글</label>
                    <textarea id="articleComment"placeholder="댓글쓰기.." rows="3"></textarea>
                    <button type="submit">쓰기</button>
                </form>

                <ul>
                    <li>
                        <div>
                            <time><small>2024-02-09</small></time>
                            <strong>Uno</strong>
                            <p>
                                adklfja;ldkfj;alskdjf;lasdjfl;sakdjf
                                a;lsdkf;alsdkjfla;ksdjfal;s
                            </p>
                        </div>
                    </li>

                    <li>
                        <div>
                            <time><small>2024-02-10</small></time>
                            <strong>Uno</strong>
                            <p>
                                a;ldkfja;lkdsfj
                                adlkjfdkfj;asdlkfj;asdk
                            </p>
                        </div>
                    </li>
                </ul>
            </div>

            <nav>
                <a href="#">이전글</a>
                <a href="#">다음글</a>
            </nav>
        </section>
    </main>
    <#include "*/include/footer.ftl">

</body>
</html>
