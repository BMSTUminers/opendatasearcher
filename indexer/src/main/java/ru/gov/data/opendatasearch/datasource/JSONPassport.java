package ru.gov.data.opendatasearch.datasource;

public class JSONPassport {
    // {
    // "identifier": "7710349494-mfclist",
    // "title": "Список МФЦ",
    // "description":
    // "Список отделений многофункциональных центров на территории Российской Федерации с указанием адреса, полного наименования МФЦ, ФИО руководителя, интернет-сайта МФЦ, контактного телефона, адреса электронной почты, географических координат местонахождения.",
    // "creator": "Министерство экономического развития Российской Федерации",
    // "created": "20121201T000000",
    // "modified": "20150624T133758",
    // "format": "csv",
    // "subject": "Список МФЦ,многофункциональные центры,реестр МФЦ,адрес МФЦ"
    // }
    protected String identifier;
    protected String title;
    protected String description;
    protected String creator;
    protected String created;
    protected String modified;
    protected String format;
    protected String subject;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
