package com.yyi.projectStudy.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieService {
    private final ProjectService projectService;
    private final QnaService qnaService;

   /* 조회수 증가 이전에 쿠키 확인 */
    public void checkCookieForReadCount(HttpServletRequest request, HttpServletResponse response,
                              String type, Long boardId) {


        /* 이전에 저장된 쿠키를 찾기 위한 변수 선언 */
        Cookie oldCookie = null;
        /* 요청으로부터 모든 쿠키 가져오기 */
        Cookie[] cookies = request.getCookies();

        /* 쿠키가 존재하는지 확인 (반복문을 활용하여 이전의 모든 쿠키를 탐색) */
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(type + "_cookieView")) {
                    oldCookie = cookie;
                    break;
                }
            }
        }

        /* 이전 쿠키가 존재하지 않는 경우 또는 쿠키 값에 현재 게시물 PK가 포함되어 있지 않은 경우 */
        if (oldCookie == null || !oldCookie.getValue().contains("(" + boardId.toString() + ")")) {
            /* 게시물 유형에 따라 조회수 증가 */
            if (type.equals("project")) {
                projectService.updateReadCount(boardId);
            } else if (type.equals("qna")) {
                qnaService.updateReadCount(boardId);
            }

            /* 쿠키 값에 현재 게시물 PK 추가 */
            String newValue = (oldCookie != null ? oldCookie.getValue() : "") + "(" + boardId + ")";

            /* 새로운 쿠키 생성 및 설정 */
            Cookie newCookie = new Cookie(type + "_cookieView", newValue);
            newCookie.setPath("/");
            /* 쿠키의 유효시간 설정 (24시간) */
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }
    }
}
