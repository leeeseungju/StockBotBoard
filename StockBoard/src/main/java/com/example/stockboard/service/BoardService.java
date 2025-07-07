package com.example.stockboard.service;

import java.util.List;
import com.example.stockboard.domain.Board;

public interface BoardService {
    List<Board> getBoardList();
    Board getBoard(int id);
    void insertBoard(Board board);
    void updateBoard(Board board);
    void deleteBoard(int id);
    void incrementCount(int idx);
    List<Board> getBoardListWithPaging(int page, int size);
    int getTotalCount();
}