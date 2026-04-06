package com.example.bladder_frontend;

import java.io.Serializable;

public class HelpArticle implements Serializable {
    private String title;
    private String content;

    public HelpArticle(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
