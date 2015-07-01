package ru.gov.data.opendatasearch.datasource;

public class Passport {
    protected String url = "";
    protected String creator = "";
    protected String subject = "";
    protected String title = "";
    protected String description = "";

    public Passport(String url, String creator, String subject, String title,
            String description) {
        super();
        this.url = replaceNull(url);
        this.creator = replaceNull(creator);
        this.subject = replaceNull(subject);
        this.title = replaceNull(title);
        this.description = replaceNull(description);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = replaceNull(url);
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = replaceNull(creator);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = replaceNull(subject);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = replaceNull(title);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = replaceNull(description);
    }

    public static String replaceNull(String str) {
        return str == null ? "" : str;
    }
}
