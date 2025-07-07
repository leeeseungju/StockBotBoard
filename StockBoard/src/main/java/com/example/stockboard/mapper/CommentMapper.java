package com.example.stockboard.mapper;

import com.example.stockboard.domain.Comment;
import java.util.List;

public interface CommentMapper {
    void insertComment(Comment comment);
    List<Comment> getCommentsByBoardId(int boardId);
    Comment getCommentById(int idx);
    void updateComment(Comment comment);
    void deleteComment(int idx);
}
