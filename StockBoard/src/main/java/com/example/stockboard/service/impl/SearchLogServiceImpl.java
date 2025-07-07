package com.example.stockboard.service.impl;

import com.example.stockboard.domain.SearchLog;
import com.example.stockboard.mapper.SearchLogMapper;
import com.example.stockboard.service.SearchLogService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchLogServiceImpl implements SearchLogService {

    @Autowired
    private SearchLogMapper searchLogMapper;

    @Override
    public void logSearch(String userId, String message) {
        searchLogMapper.insertSearchLog(userId, message);
    }

    @Override
    public List<SearchLog> getAllLogs() {
        return searchLogMapper.getAllLogs();
    }
}