package com.yyi.projectStudy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yyi.projectStudy.dto.UserDTO;
import com.yyi.projectStudy.dto.UserJobDTO;
import com.yyi.projectStudy.entity.KakaoProfile;
import com.yyi.projectStudy.entity.OAuthToken;
import com.yyi.projectStudy.properties.KakaoProperties;
import com.yyi.projectStudy.properties.NaverProperties;
import com.yyi.projectStudy.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final NaverProperties naverProperties;
    private final KakaoProperties kakaoProperties;

    /* 네이버 로그인 */
    @RequestMapping("/naver/login")
    public String naverLogin(@RequestParam String code, @RequestParam String state,
                             HttpSession session) throws IOException {
        // 토큰을 이용하여 네이버 회원 정보 조회
        UserDTO naverUserInfo = getNaverUserInfo(getAccessToken(code, state));
        // 조회된 네이버 id를 이용하여 회원 정보 조회
        UserDTO naverUser = userService.findByEmail(naverUserInfo.getEmail());

        UserDTO loginUser;
        if (naverUser == null) {
            // 회원 정보
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(naverUserInfo.getEmail());
            userDTO.setNickname(naverUserInfo.getNickname());

            UUID garbagePassword = UUID.randomUUID();
            userDTO.setPassword(garbagePassword.toString());

            // 회원 직업 : 기타로 일단 설정
            UserJobDTO userJobDTO = new UserJobDTO();
            userJobDTO.setJobId(10L);

            userService.save(userDTO, userJobDTO);

            loginUser = userService.socialLogin(userDTO);
        } else {
            loginUser = userService.socialLogin(naverUser);
        }

        session.setAttribute("userDTO", loginUser);
        session.setAttribute("loginType", "naver");
        return "redirect:/";
    }

    /* 토큰 얻기 */
    private String getAccessToken(String code, String state) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", naverProperties.getClientId());
        body.add("client_secret", naverProperties.getClientSecret());
        body.add("code", code);
        body.add("state", state);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                naverTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    /* 네이버 회원 정보 조회 */
    private UserDTO getNaverUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                naverUserInfoRequest,
                String.class
        );

        // HTTP 응답 받아오기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String email = jsonNode.get("response").get("email").asText();
        String nickname = jsonNode.get("response").get("name").asText();

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setNickname(nickname);

        return userDTO;
    }



    /* 카카오 로그인 */
    @RequestMapping("/kakao/login")
    public String kakaoLogin(@RequestParam String code,
                             HttpSession session) throws IOException {

        // post 방식으로 key=value 데이터 요청 (카카오 쪽으로)
        RestTemplate rt = new RestTemplate();

        // httpheader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // httpbody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.getClientId());
        params.add("redirect_uri", "http://localhost:8083/kakao/login");
        params.add("code", code);

        // httpheader와 httpbody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        // http 요청하기 : post 방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // Gson, Json Simple, ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue((String) response.getBody(), OAuthToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // post 방식으로 key=value 데이터 요청 (카카오 쪽으로)
        RestTemplate rt2 = new RestTemplate();

        // httpheader 오브젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // httpheader와 httpbody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
                new HttpEntity<>(headers2);

        // http 요청하기 : post 방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );


        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // user : username, password, email
        UUID garbagePassword = UUID.randomUUID();

        System.out.println(kakaoProfile);

        UserDTO kakaoUser =
                userService.findByEmail(kakaoProfile.getKakao_account().getEmail());

        UserDTO loginUser;
        if (kakaoUser == null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(kakaoProfile.getKakao_account().getEmail());
            userDTO.setNickname(kakaoProfile.getProperties().getNickname());
            userDTO.setPassword(garbagePassword.toString());

            UserJobDTO userJobDTO = new UserJobDTO();
            userJobDTO.setJobId(10L);
            userService.save(userDTO, userJobDTO);
            loginUser = userService.socialLogin(userDTO);
        } else {
            loginUser = userService.socialLogin(kakaoUser);
        }
        session.setAttribute("userDTO", loginUser);
        session.setAttribute("loginType", "kakao");
        return "redirect:/";
    }

}
