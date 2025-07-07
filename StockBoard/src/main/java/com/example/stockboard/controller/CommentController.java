package com.example.stockboard.controller;

import com.example.stockboard.domain.Comment;
import com.example.stockboard.domain.User;
import com.example.stockboard.service.CommentService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 댓글 작성
    @PostMapping("/write")
    public String writeComment(@ModelAttribute Comment comment, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        comment.setWriter(user.getUserName());
        comment.setCreatedAt(LocalDateTime.now().toString());

        System.out.println("댓글 작성 - 세션 사용자: " + user.getUserName());
        System.out.println("댓글 작성 - 저장될 writer: " + comment.getWriter());

        commentService.insertComment(comment);
        return "redirect:/board/view/" + comment.getSeq();
    }

    // 댓글 수정 폼
    @GetMapping("/edit/{idx}")
    public String editForm(@PathVariable int idx, Model model, HttpSession session) {
        Comment comment = commentService.getCommentById(idx);
        User user = (User) session.getAttribute("user");

        if (user == null || comment == null || comment.getWriter() == null ||
                !comment.getWriter().equalsIgnoreCase(user.getUserName())) {
            System.out.println("수정 권한 없음");
            System.out.println("세션 사용자: " + (user != null ? user.getUserName() : "null"));
            System.out.println("댓글 작성자: " + (comment != null ? comment.getWriter() : "null"));
            return "redirect:/";
        }

        model.addAttribute("comment", comment);
        return "board/comment";
    }

    // 댓글 수정 처리
    @PostMapping("/edit/{idx}")
    public String edit(@PathVariable int idx, @ModelAttribute Comment updatedComment, HttpSession session) {
        Comment original = commentService.getCommentById(idx);
        User user = (User) session.getAttribute("user");

        if (user == null || original == null || original.getWriter() == null ||
                !original.getWriter().equalsIgnoreCase(user.getUserName())) {
            System.out.println("수정 처리 권한 없음");
            return "redirect:/";
        }

        original.setContext(updatedComment.getContext());
        commentService.updateComment(original);
        return "redirect:/board/view/" + original.getSeq();
    }

    // 댓글 삭제 처리
    @PostMapping("/delete/{idx}")
    public String delete(@PathVariable int idx, @RequestParam int seq, HttpSession session) {
        Comment comment = commentService.getCommentById(idx);
        User user = (User) session.getAttribute("user");

        if (user == null || comment == null || comment.getWriter() == null ||
                !comment.getWriter().equalsIgnoreCase(user.getUserName())) {
            System.out.println("삭제 권한 없음");
            return "redirect:/";
        }

        commentService.deleteComment(idx);
        return "redirect:/board/view/" + seq;
    }
}
