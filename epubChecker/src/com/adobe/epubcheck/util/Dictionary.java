package com.adobe.epubcheck.util;

import java.util.HashMap;
import java.util.Map;

public class Dictionary {

	public static final Dictionary dictionary = new Dictionary();
	private Map<String, AdditionalInfo> info = new HashMap<String, AdditionalInfo>();
	private AdditionalInfo additionalInfo = null;

	private Dictionary() {
		initInfoMap();
	}

	private void initInfoMap() {
		/*
		 * OPFChecker.java
		 */
		info.put(
				"001",
				new AdditionalInfo(
						"001",
						Severity.ERROR,
						"File listed in reference element in guide was not declared in OPF manifest: ",
						"", "", "", ""));
		info.put("002", new AdditionalInfo("002", Severity.ERROR,
				"OPF file is missing", "", "", "", ""));

		info.put(
				"003",
				new AdditionalInfo(
						"003",
						Severity.ERROR,
						"unique-identifier attribute in package element must reference an existing identifier element id",
						"", "", "", ""));
		info.put("004", new AdditionalInfo("004", Severity.ERROR,
				"Illegal argument exception", "", "", "", ""));
		info.put("005", new AdditionalInfo("005", Severity.ERROR,
				"Unable to read zip file entries.", "", "", "", ""));
		info.put("006", new AdditionalInfo("006", Severity.ERROR,
				Messages.FILENAME_ENDS_IN_DOT, "", "", "", ""));
		info.put("007", new AdditionalInfo("007", Severity.ERROR,
				Messages.FILENAME_DISALLOWED_CHARACTERS, "", "", "", ""));
		info.put("008", new AdditionalInfo("008", Severity.ERROR,
				"IO Exception", "", "", "", ""));
		info.put("009", new AdditionalInfo("009", Severity.ERROR,
				"fallback item could not be found", "", "", "", ""));
		info.put("010", new AdditionalInfo("010", Severity.ERROR,
				"fallback-style item could not be found", "", "", "", ""));
		info.put("011", new AdditionalInfo("011", Severity.ERROR,
				"'sign' is not a permissible spine media-type", "", "", "", ""));
		info.put("012", new AdditionalInfo("012", Severity.ERROR,
				"non-standard media-type with no fallback", "", "", "", ""));
		info.put(
				"013",
				new AdditionalInfo(
						"013",
						Severity.ERROR,
						"non-standard media-type with fallback to non-spine-allowed media-type",
						"", "", "", ""));
		info.put("014", new AdditionalInfo("014", Severity.ERROR,
				"circular reference in fallback chain", "", "", "", ""));
		info.put("015", new AdditionalInfo("015", Severity.ERROR,
				"circular reference in fallback chain", "", "", "", ""));

		/*
		 * XRefChecker.java
		 */
		info.put(
				"016",
				new AdditionalInfo(
						"016",
						Severity.ERROR,
						"remote resource reference not allowed; resource must be placed in the OCF",
						"", "", "", ""));
		info.put("017", new AdditionalInfo("017", Severity.ERROR,
				"referenced resource missing in the package", "", "", "", ""));
		info.put("018", new AdditionalInfo("018", Severity.ERROR,
				"referenced resource missing in the package", "", "", "", ""));
		info.put("019", new AdditionalInfo("019", Severity.ERROR,
				"fragment identifier missing in reference to", "", "", "", ""));
		info.put("020", new AdditionalInfo("020", Severity.ERROR,
				"hyperlink to non-standard resource", "", "", "", ""));
		info.put("021", new AdditionalInfo("021", Severity.ERROR,
				"non-standard image resource", "", "", "", ""));
		info.put("022", new AdditionalInfo("022", Severity.ERROR,
				"hyperlink to non-standard resource", "", "", "", ""));
		info.put("023", new AdditionalInfo("023", Severity.ERROR,
				"fragment identifier used for image resource", "", "", "", ""));
		info.put("024", new AdditionalInfo("024", Severity.ERROR,
				"fragment identifier used for stylesheet resource", "", "", "",
				""));
		info.put("025", new AdditionalInfo("025", Severity.ERROR,
				"fragment identifier is not defined in", "", "", "", ""));
		info.put("026", new AdditionalInfo("026", Severity.ERROR,
				"fragment identifier  defines incompatible resource type in",
				"", "", "", ""));
		info.put("027", new AdditionalInfo("027", Severity.ERROR,
				"fragment identifier  defines incompatible resource type in",
				"", "", "", ""));

		/*
		 * EpubCheck.java
		 */
		info.put("028", new AdditionalInfo("028", Severity.ERROR,
				Messages.CANNOT_READ_HEADER, "", "", "", ""));
		info.put("029", new AdditionalInfo("029", Severity.ERROR,
				Messages.CORRUPTED_ZIP_HEADER, "", "", "", ""));
		info.put("030", new AdditionalInfo("030", Severity.ERROR,
				Messages.LENGTH_FIRST_FILENAME, "", "", "", ""));
		info.put("031", new AdditionalInfo("031", Severity.ERROR,
				Messages.EXTRA_FIELD_LENGTH, "", "", "", ""));
		info.put("032", new AdditionalInfo("032", Severity.ERROR,
				Messages.MIMETYPE_ENTRY_MISSING, "", "", "", ""));
		info.put("033", new AdditionalInfo("033", Severity.ERROR,
				Messages.MIMETYPE_WRONG_TYPE, "", "", "", ""));
		info.put("034", new AdditionalInfo("034", Severity.ERROR,
				Messages.IO_ERROR, "", "", "", ""));

		/*
		 * CSSChecker.java
		 */

		info.put("035", new AdditionalInfo("035", Severity.ERROR,
				Messages.MISSING_FILE, "", "", "", ""));
		info.put("036", new AdditionalInfo("036", Severity.ERROR, "Exception",
				"", "", "", ""));

		/*
		 * CSSHandler.java
		 */
		info.put("037",
				new AdditionalInfo("037", Severity.ERROR,
						"Font-face reference to non-standard font type", "",
						"", "", ""));
		info.put("038", new AdditionalInfo("038", Severity.ERROR,
				Messages.NULL_REF, "", "", "", ""));
		info.put(
				"039",
				new AdditionalInfo(
						"039",
						Severity.ERROR,
						"The fixed value of the position property is not part of the EPUB 3 CSS Profile.",
						"", "", "", ""));
		info.put(
				"040",
				new AdditionalInfo(
						"040",
						Severity.ERROR,
						"The direction and unicode-bidi properties must not be included in an EPUB Style Sheet.",
						"", "", "", ""));

		
		
	}

	public void setMessage(String id, String message) {
		AdditionalInfo additionalObject = info.get(id);
		if (additionalObject != null) {
			additionalObject.setMessage(message);
		}
	}

	public String getMessage(String key) {
		additionalInfo = info.get(key);

		return additionalInfo.getMessage();
	}

	public Severity getSeverity() {
		return additionalInfo.getSeverity();
	}

	public String getMessageShortDescription() {
		return additionalInfo.getMessageShortDescription();
	}

	public String getMessageLongDescription() {
		return additionalInfo.getMessageLongDescription();
	}

	public String getMessageMainCategory() {
		return additionalInfo.getMessageMainCategory();
	}

	public String getMessageSubCategory() {
		return additionalInfo.getMessageSubCategory();
	}

}
