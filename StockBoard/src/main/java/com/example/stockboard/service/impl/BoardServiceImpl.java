package com.example.stockboard.service.impl;

import com.example.stockboard.domain.Board;
import com.example.stockboard.mapper.BoardMapper;
import com.example.stockboard.service.BoardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardMapper boardMapper;

    @Override
    public List<Board> getBoardList() {
        return boardMapper.getBoardList();
    }

    @Override
    public Board getBoard(int id) {
        return boardMapper.getBoardById(id);
    }

    @Override
    public void insertBoard(Board board) {
        boardMapper.insertBoard(board);
    }

    @Override
    public void updateBoard(Board board) {
        boardMapper.updateBoard(board);
    }

    @Override
    public void deleteBoard(int id) {
        boardMapper.deleteBoard(id);
    }
    
    @Override
    public void incrementCount(int idx) {
        boardMapper.incrementCount(idx);
    }
    
    @Override
    public List<Board> getBoardListWithPaging(int page, int size) {
        int offset = (page - 1) * size;
        return boardMapper.getBoardListWithPaging(offset, size);
    }

    @Override
    public int getTotalCount() {
        return boardMapper.getTotalCount();
    }
}
