package com.example.stockboard.service;

import java.util.List;

import com.example.stockboard.domain.SearchLog;

public interface SearchLogService {
    void logSearch(String userId, String message);
    List<SearchLog> getAllLogs();
}
