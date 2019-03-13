package com.xincao9.prs.api.model;

/**
 *
 * @author xincao9@gmail.com
 */
public class RawTextArticle {

    private String author;
    private String title;
    private String summary;
    private String text;

    public RawTextArticle() {
    }

    public RawTextArticle(String author, String title, String summary, String text) {
        this.author = author;
        this.title = title;
        this.summary = summary;
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
