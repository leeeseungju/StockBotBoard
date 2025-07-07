package com.example.stockboard.service.impl;

import com.example.stockboard.domain.Comment;
import com.example.stockboard.mapper.CommentMapper;
import com.example.stockboard.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public void insertComment(Comment comment) {
        commentMapper.insertComment(comment);
    }

    @Override
    public List<Comment> getCommentsByBoardId(int boardId) {
        return commentMapper.getCommentsByBoardId(boardId);
    }

    @Override
    public Comment getCommentById(int idx) {
        return commentMapper.getCommentById(idx);
    }

    @Override
    public void updateComment(Comment comment) {
        commentMapper.updateComment(comment);
    }

    @Override
    public void deleteComment(int idx) {
        commentMapper.deleteComment(idx);
    }
}
