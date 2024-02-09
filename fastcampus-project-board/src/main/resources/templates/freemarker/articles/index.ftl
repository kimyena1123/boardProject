<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>게시판 페이지</title>
</head>
<body>
<!-- insert나 replace 중에 쓰기. include도 있지만 권장X.  {} 안에 view 이름을 넣어주면 된다.-->

    <#include "*/include/header.ftl">
    <main>
        <form> <!-- 검색창 -->
            <label for="search-type" hidden>유형</label>
            <select id="search-type" name=search-type""> <!-- article을 검색할 때 필터로 5가지를 주고 있다. 필터: 제목, 본문, id, 닉네임, 해시태그 -->
                <option>제목</option>
                <option>본문</option>
                <option>ID</option>
                <option>닉네임</option>
                <option>해시태그</option>
            </select>

            <label for="search-value" hidden>검색어</label>
            <input type="search" id="search-value" name="search-value" placeholder="검색어..">

            <button type="submit">검색</button>
        </form>

        <table>
            <thead>
            <tr>
                <th>제목</th>
                <th>해시태그</th>
                <th>작성자</th>
                <th>작성일</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>첫번째 글</td>
                <td>#java</td>
                <td>Uno</td>
                <td>2024-02-02</td>
            </tr>

            <tr>
                <td>두번째 글</td>
                <td>#spring</td>
                <td>Uno</td>
                <td>2024-02-03</td>
            </tr>

            <tr>
                <td>세번째 글</td>
                <td>#Database</td>
                <td>Uno</td>
                <td>2024-02-04</td>
            </tr>
            </tbody>
        </table>


        <!--        테이블은 한번에 모든 게시글을 보여주지 않음. 한 번에 15~20개씩 잘라서 보여주고 이걸 이동할 수 있는 내비게이션 바가 필요하다.-->
        <nav>
            <table>
                <tr>
                    <td>previous</td>
                    <td>1</td>
                    <td>2</td>
                    <td>3</td>
                    <td>4</td>
                    <td>next</td>
                </tr>
            </table>
        </nav>
    </main>
    <#include "*/include/footer.ftl">

</body>
</html>
