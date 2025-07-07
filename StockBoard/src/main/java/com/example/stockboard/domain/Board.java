package com.example.stockboard.domain;

public class Board {
    private int idx;
    private String title;       // 사용자 입력
    private String content;     // 사용자 입력
    private int count;          // 기본값 0
    private String writer;      // 세션에서 가져온 사용자 이름
    private String createdAt;   // 작성 시간

    public Board() {
    }

    public Board(int idx, String title, String content, int count, String writer, String createdAt) {
        this.idx = idx;
        this.title = title;
        this.content = content;
        this.count = count;
        this.writer = writer;
        this.createdAt = createdAt;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
