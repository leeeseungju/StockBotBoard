package com.example.stockboard.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.stockboard.domain.Board;

@Mapper
public interface BoardMapper {
    List<Board> getBoardList();
    Board getBoardById(int id);
    void insertBoard(Board board);
    void updateBoard(Board board);
    void deleteBoard(int id);
    void incrementCount(int idx);
    List<Board> getBoardListWithPaging(@Param("offset") int offset, @Param("size") int size);
    int getTotalCount();
}
