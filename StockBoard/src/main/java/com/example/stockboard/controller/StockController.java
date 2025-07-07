package com.example.stockboard.controller;

import com.example.stockboard.client.FastApiClient;
import com.example.stockboard.domain.User;
import com.example.stockboard.service.SearchLogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class StockController {

    @Autowired
    private FastApiClient fastApiClient;

    @Autowired
    private SearchLogService searchLogService;

    // 사용자가 질문 전송 시 실행되는 POST 요청 처리
    @PostMapping("/stock")
    public String postHome(@RequestParam("message") String message, Model model, HttpSession session) {
        // 로그인 여부 확인
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "home";
        }

        // 로그인 사용자일 경우 검색 로그 저장
        searchLogService.logSearch(user.getUserId(), message);

        // FastAPI로 리포트 요청 및 결과 모델에 전달
        Map<String, Object> response = fastApiClient.requestReport(message);

        if (response.containsKey("error")) {
            model.addAttribute("error", response.get("error"));
        } else {
            System.out.println("✅ summaryChart = " + response.get("summaryChart"));
            System.out.println("✅ techChart = " + response.get("techChart"));

            model.addAttribute("stockName", response.get("name"));
            model.addAttribute("stockSymbol", response.get("symbol"));
            model.addAttribute("price", response.get("price"));
            model.addAttribute("summary", response.get("summary"));
            model.addAttribute("summaryChart", response.get("summaryChart"));
            model.addAttribute("report", response.get("report"));
            model.addAttribute("techChart", response.get("techChart"));
            model.addAttribute("links", response.get("links"));
        }

        return "home";
    }

    // 사용자가 URL에 ?message=삼성전자 입력할 때도 같은 처리
    @GetMapping("/report")
    public String getReport(@RequestParam("message") String message, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "home";
        }

        return postHome(message, model, session);
    }
}
