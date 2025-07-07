package com.example.stockboard.domain;

public class Comment {
    private int idx;
    private int seq;            // 게시글 번호 (Board.idx 참조)
    private String context;     // 댓글 내용
    private String writer;      // 작성자
    private String createdAt;   // 작성 시간

    public Comment() {
    }

    public Comment(int idx, int seq, String context, String writer, String createdAt) {
        this.idx = idx;
        this.seq = seq;
        this.context = context;
        this.writer = writer;
        this.createdAt = createdAt;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
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
