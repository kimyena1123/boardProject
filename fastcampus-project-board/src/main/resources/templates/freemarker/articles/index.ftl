<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>게시판 페이지</title>

<#--    bootstrap cdn-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <link rel="stylesheet" href="/css/search-bar.css">
</head>
<body>
    <#include "*/include/header.ftl">


    <main class="container" style="padding-top: 40px;">
<#--bootdey 템플릿 사용-->
        <div class="row">
            <div class="col-lg-12 card-margin">
                <div class="card search-form">
                    <div class="card-body p-0">
                        <form id="search-form">
                            <div class="row">
                                <div class="col-12">
                                    <div class="row no-gutters">
                                        <div class="col-lg-3 col-md-3 col-sm-12 p-0">
                                            <select class="form-control" id="exampleFormControlSelect1">
                                                <option>제목</option>
                                                <option>본문</option>
                                                <option>ID</option>
                                                <option>닉네임</option>
                                                <option>해시태그</option>
                                            </select>
                                        </div>
                                        <div class="col-lg-8 col-md-6 col-sm-12 p-0">
                                            <input type="text" placeholder="Search..." class="form-control" id="search" name="search" style="border-left:1px solid #bbb;">
                                        </div>
                                        <div class="col-lg-1 col-md-3 col-sm-12 p-0">
                                            <button type="submit" class="btn btn-base">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-search"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <table class="table" id="article-table">
            <thead class="table-light">
            <tr>
                <th class="title col-6">제목</th>
                <th class="hashtag col-2">해시태그</th>
                <th class="user-id col">작성자</th>
                <th class="created-at col">작성일</th>
            </tr>
            </thead>
            <tbody>
            <#list articles.getContent() as article>
            <tr>
                <td class="title"><a href="/articles/${article.id()}">${article.title()}</a></td>
                <td class="hashtag">${article.hashtag()?default('')}</td>
                <td class="user-id">${article.nickname()}</td>
                <td class="created-at"><time>${article.createdAt()}</time></td>
            </tr>
            </#list>
            </tbody>
        </table>


        <nav id="pagination" aria-label="Page navigation example">
            <ul class="pagination justify-content-center">

                <#if articles.number - 1 gt 0>
                    <li class="page-item">
                        <a class="page-link" href="/articles?page=${articles.number - 1}">
                            Previous
                        </a>
                    </li>
                <#else>
                    <li class="page-item disabled">
                        <a class="page-link" href="#">Previous</a>
                    </li>
                </#if>


                <#list paginationBarNumbers as pageNumber>
                    <li class="page-item">
                        <#if pageNumber == articles.number>
                            <a class="page-link disabled" href="#">${pageNumber + 1}</a>
                        <#else>
                            <a class="page-link" href="/articles?page=${pageNumber}">
                                ${pageNumber + 1}
                            </a>
                        </#if>
                    </li>
                </#list>

                <#if articles.number + 1 < articles.totalPages>
                    <li class="page-item">
                        <a class="page-link" href="/articles?page=${articles.number + 1}">
                            Next
                        </a>
                    </li>
                <#else>
                    <li class="page-item disabled">
                        <a class="page-link" href="#">Next</a>
                    </li>
                </#if>            </ul>
        </nav>

    </main>


    <#include "*/include/footer.ftl">

<#--bootstrap script-->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>

</body>

</html>
