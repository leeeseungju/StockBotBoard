package com.example.stockboard.controller;
import com.example.stockboard.domain.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.stockboard.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
    private UserService userService;
	
	@GetMapping("/join")
    public String join() {
        return "join";
    }
	
	@PostMapping("/join")
	public String joinAction(
	        @RequestParam String userId,
	        @RequestParam String password,
	        @RequestParam String confirmPassword,
	        @RequestParam String userName,
	        Model model
	) {
	    // 비밀번호 재확인 로직
	    if (!password.equals(confirmPassword)) {
	        model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
	        return "join";
	    }

	    // 사용자 객체 생성 및 전달
	    User user = new User();
	    user.setUserId(userId);
	    user.setPassword(password);
	    user.setUserName(userName);

	    if (userService.join(user)) {
	        return "redirect:/login";
	    } else {
	        model.addAttribute("error", "이미 존재하는 아이디입니다.");
	        return "join";
	    }
	}
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @PostMapping("/login")
    public String loginAction(@RequestParam String userId, @RequestParam String password, HttpSession session, Model model) {
        User user = userService.login(userId, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/";
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/?logout";
    }
    
    @GetMapping("/list")
    public String userList(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getUserType())) {
            return "redirect:/";
        }
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user_list";
    }
    
    @GetMapping("/mypage")
    public String myPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        return "mypage";
    }

    @PostMapping("/mypage")
    public String updateMyPage(@RequestParam String password,
                               @RequestParam String userName,
                               HttpSession session,
                               Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        user.setPassword(password);
        user.setUserName(userName);
        userService.updateUser(user);

        // ✅ 여기가 중요! user 모델 재등록
        session.setAttribute("user", user);
        model.addAttribute("user", user); // 이거 없으면 Thymeleaf에서 null 에러 발생
        model.addAttribute("message", "수정 완료!");

        return "mypage";
    }

}
