package ru.gov.data.opendatasearch.datasource;

public class Record {
	protected String json;
	protected String id;
	protected String geo = "";

	public Record(String json, String id) {
		super();
		this.json = json;
		this.id = id;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGeo() {
		return geo;
	}

	public void setGeo(String geo) {
		this.geo = geo;
	}
}
