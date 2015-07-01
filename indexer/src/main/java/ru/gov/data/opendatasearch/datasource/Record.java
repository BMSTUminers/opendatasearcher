package ru.gov.data.opendatasearch.datasource;

public class Record {
    protected String json = "";
    protected String id = "";
    protected String geo = "";
    protected String phone = "";
    protected String email = "";

    public Record(String json, String id) {
        super();
        this.json = replaceNull(json);
        this.id = replaceNull(id);
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = replaceNull(json);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = replaceNull(id);
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = replaceNull(geo);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = replaceNull(phone);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = replaceNull(email);
    }

    public static String replaceNull(String str) {
        return str == null ? "" : str;
    }
}
