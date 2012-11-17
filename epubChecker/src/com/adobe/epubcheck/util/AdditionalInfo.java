package com.adobe.epubcheck.util;

public class AdditionalInfo {

	private String id;
	private Severity severity;
	private String message;
	private String messageShortDescription;
	private String messageLongDescription;
	private String messageMainCategory;
	private String messageSubCategory;

	public AdditionalInfo(String id, Severity severity, String message,
			String messageShortDescription, String messageLongDescription,
			String messageMainCategory, String messageSubCategory) {

		this.id = id;
		this.severity = severity;
		this.message = message;
		this.messageShortDescription = messageShortDescription;
		this.messageLongDescription = messageLongDescription;
		this.messageMainCategory = messageMainCategory;
		this.messageSubCategory = messageSubCategory;

	}

	public void setMessage(String message) {
		this.message = message;

	}

	public String getMessage() {
		return message;
	}

	public Severity getSeverity() {
		return this.severity;
	}

	public String getMessageShortDescription() {
		return this.messageShortDescription;
	}

	public String getMessageLongDescription() {
		return this.messageLongDescription;
	}

	public String getMessageMainCategory() {
		return this.messageMainCategory;
	}

	public String getMessageSubCategory() {
		return this.messageSubCategory;
	}
}
