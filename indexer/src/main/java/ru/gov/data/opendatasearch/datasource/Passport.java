package ru.gov.data.opendatasearch.datasource;

public class Passport {
	protected String url;
	protected String creator;
	protected String subject;
	protected String title;
	protected String description;

	public Passport(String url, String creator, String subject,
			String title, String description) {
		super();
		this.url = url;
		this.creator = creator;
		this.subject = subject;
		this.title = title;
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
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
}
