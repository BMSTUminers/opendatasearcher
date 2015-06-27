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
		if (url != null)
			this.url = url;
		if (creator != null)
			this.creator = creator;
		if (subject != null)
			this.subject = subject;
		if (title != null)
			this.title = title;
		if (description != null)
			this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if (url != null)
			this.url = url;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		if (creator != null)
			this.creator = creator;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		if (subject != null)
			this.subject = subject;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title != null)
			this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description != null)
			this.description = description;
	}
}
