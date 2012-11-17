package com.adobe.epubcheck.util;

public class CheckMessage {

	public Severity severity;
	private String messageUI;
	private String messageShortDescription;
	private String messageLongDescription;
	private String sourceErroneousFileName;
	private String sourceErroneousLineNumber;
	private String sourceErroneousColumnNumber;
	private String sourceLineText;
	private String messageMainCategory;
	private String messageSubCategory;
	private static int idMaker = 0;

	public CheckMessage(String file, String lineNumber,
			String columnNumber,String... messageUI) {

		if (messageUI.length > 0){
			this.messageUI = messageUI[0];
			this.sourceLineText = Dictionary.dictionary.getMessage(this.messageUI);
			this.severity = Dictionary.dictionary.getSeverity();
		}
		//this.severity = messageType;
		this.sourceErroneousFileName = file;
		this.sourceErroneousLineNumber = lineNumber;
		this.sourceErroneousColumnNumber = columnNumber;
		//this.sourceLineText = message;

	}

	public CheckMessage(Severity messageType, String file, String message) {

		this.sourceErroneousFileName = file;
		this.sourceLineText = message;

	}

	public String toString() {
		String text = "";
		text = "ID: " + messageUI + System.getProperty("line.separator")
				+ "TYPE: " + severity + System.getProperty("line.separator")
				+ System.getProperty("line.separator") + "Error File: "
				+ sourceErroneousFileName
				+ System.getProperty("line.separator");
		if (sourceErroneousLineNumber.length() > 0
				&& sourceErroneousColumnNumber.length() > 0)
			text += "Number of line: " + sourceErroneousLineNumber
					+ System.getProperty("line.separator")
					+ "Number of column: " + sourceErroneousColumnNumber
					+ System.getProperty("line.separator");
		text += "Description: " + sourceLineText;
		text += System.getProperty("line.separator")
				+ "=========================================================================================================================="
				+ System.getProperty("line.separator");

		return text;
	}

	public String toString(boolean printAll, boolean printSome) {
		String text = "";

		if (printSome) {
			text += System.getProperty("line.separator") + "ID: " + messageUI
					+ System.getProperty("line.separator") + "Description: "
					+ sourceLineText;
			text += System.getProperty("line.separator")
					+ "=========================================================================================================================="
					+ System.getProperty("line.separator");

		} else if (printAll) {
			text = System.getProperty("line.separator") + "ID: " + messageUI
					+ System.getProperty("line.separator")
					+ System.getProperty("line.separator") + "Error File: "
					+ sourceErroneousFileName
					+ System.getProperty("line.separator");
			if (sourceErroneousLineNumber.length() > 0
					&& sourceErroneousColumnNumber.length() > 0)
				text += "Number of line: " + sourceErroneousLineNumber
						+ System.getProperty("line.separator")
						+ "Number of column: " + sourceErroneousColumnNumber
						+ System.getProperty("line.separator");
			text += "Description: " + sourceLineText;
			text += System.getProperty("line.separator")
					+ "=========================================================================================================================="
					+ System.getProperty("line.separator");
		}

		return text;
	}

	public String toStringFilter(boolean error, boolean exception,
			boolean warning, boolean info, boolean printAll, boolean printSome) {
		String text = "";
		if ((error || exception || warning || info) && severity != null) {
			switch (severity) {
			case ERROR:
				if (error)
					text += "TYPE: " + severity + toString(printAll, printSome);
				break;
			case EXCEPTION:
				if (exception)
					text += "TYPE: " + severity + toString(printAll, printSome);
				break;
			case WARNING:
				if (warning)
					text += "TYPE: " + severity + toString(printAll, printSome);
				break;
			case INFO:
				if (info)
					text += "TYPE: " + severity + toString(printAll, printSome);
			}
		} else {
			text += "TYPE: " + severity + toString(printAll, printSome);
		}
		return text;
	}

}
