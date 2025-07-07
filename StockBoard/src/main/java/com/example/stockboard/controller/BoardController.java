package com.example.stockboard.controller;

import com.example.stockboard.domain.Board;
import com.example.stockboard.domain.User;
import com.example.stockboard.service.BoardService;
import com.example.stockboard.service.CommentService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;
    
    @Autowired
    private CommentService commentService;

    @GetMapping("/write")
    public String writeForm() {
        return "board/write";
    }
    
    @PostMapping("/write")
    public String write(Board board, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        board.setWriter(user.getUserName());
        // createdAt은 DB에서 NOW()로 자동 설정
        boardService.insertBoard(board);
        return "redirect:/board";
    }

    @GetMapping("/view/{idx}")
    public String view(@PathVariable int idx, Model model) {
        Board board = boardService.getBoard(idx);
        boardService.incrementCount(idx); // 조회수 증가

        model.addAttribute("board", board);
        model.addAttribute("comments", commentService.getCommentsByBoardId(idx));  // 댓글 추가
        return "board/view";
    }

    @PostMapping("/delete/{idx}")
    public String delete(@PathVariable("idx") int idx) {
        boardService.deleteBoard(idx);
        return "redirect:/board";
    }
    
    // 수정 폼
    @GetMapping("/edit/{idx}")
    public String editForm(@PathVariable("idx") int idx, Model model, HttpSession session) {
        Board board = boardService.getBoard(idx);
        User user = (User) session.getAttribute("user");

        // 작성자만 접근 가능
        if (user == null || !user.getUserName().equals(board.getWriter())) {
            return "redirect:/board/view/" + idx;
        }

        model.addAttribute("board", board);
        return "board/edit";
    }

    // 수정 처리
    @PostMapping("/edit/{idx}")
    public String edit(@PathVariable("idx") int idx, @ModelAttribute Board updatedBoard, HttpSession session) {
        Board board = boardService.getBoard(idx);
        User user = (User) session.getAttribute("user");

        if (user == null || !user.getUserName().equals(board.getWriter())) {
            return "redirect:/board/view/" + idx;
        }

        board.setTitle(updatedBoard.getTitle());
        board.setContent(updatedBoard.getContent());
        boardService.updateBoard(board);

        return "redirect:/board/view/" + idx;
    }
    
    @GetMapping({"", "/"})
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {
        int size = 5; // 한 페이지당 게시글 수
        List<Board> boards = boardService.getBoardListWithPaging(page, size);
        int totalCount = boardService.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount / size);

        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalCount", totalCount);

        return "board/list";
    }
}
