package ru.gov.data.opendatasearch.datasource;

public class Record {
    protected String json = "";
    protected String id = "";
    protected String geo = "";
    protected String phone = "";
    protected String email = "";

    public Record(String json, String id) {
        super();
        if (json != null)
            this.json = json;
        if (id != null)
            this.id = id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        if (json != null)
            this.json = json;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id != null)
            this.id = id;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        if (geo != null)
            this.geo = geo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone != null)
            this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null)
            this.email = email;
    }
}
