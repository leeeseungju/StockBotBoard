package com.example.stockboard.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.stockboard.domain.SearchLog;

@Mapper
public interface SearchLogMapper {
    List<SearchLog> getAllLogs();
    void insertSearchLog(@Param("userId") String userId, @Param("message") String message);
}