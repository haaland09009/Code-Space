package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.JobDTO;
import com.yyi.projectStudy.dto.UserDTO;
import com.yyi.projectStudy.dto.UserJobDTO;
import com.yyi.projectStudy.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    // 로그인 페이지 이동
    @GetMapping("/loginPage")
    public String loginPage() {
        return "user/loginPage";
    }

    // 회원가입 페이지 이동
    @GetMapping("/joinPage")
    public String joinPage(Model model) {
        List<JobDTO> jobDTOList = userService.findAllJobs();
        model.addAttribute("jobList", jobDTOList);
        return "user/joinPage";
    }

    // 회원가입 프로세스
    @PostMapping("/joinProcess")
    public String joinProcess(@ModelAttribute UserDTO userDTO,
                              @ModelAttribute UserJobDTO userJobDTO) throws IOException {
        userService.save(userDTO, userJobDTO);
        return "user/loginPage";
    }

    // 로그인 프로세스
    @PostMapping("/loginProcess")
    public @ResponseBody String loginProcess(@ModelAttribute UserDTO userDTO, HttpSession session) {
        UserDTO sessionUser = userService.login(userDTO);
        if (sessionUser != null) {
            session.setAttribute("userDTO", sessionUser);
            return "ok";
        } else {

            return "no";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    // 마이페이지
    @GetMapping("/myPage")
    public String myPage(HttpSession session, Model model) {
//        return "user/myPage";
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        if (sessionUser == null) {
            return "redirect:/user/loginPage";
        } else {
            UserDTO userDTO = userService.findById(sessionUser.getId());
            model.addAttribute("userDTO", userDTO);
            return "user/myPage";
        }
    }


    // 회원탈퇴
//    @GetMapping("/delete")
//    public String delete(HttpSession session) {
//        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
//        if (sessionUser == null) {
//            return "redirect:/user/loginPage";
//        } else {
//            session.invalidate();
//            userService.deleteById(sessionUser.getId());
//            return "redirect:/";
//        }
//    }
}