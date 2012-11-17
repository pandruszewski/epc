package com.adobe.epubcheck.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import com.cedarsoftware.util.io.JsonWriter;

public class CheckingReport {
	private String epubFileName;
	private Date processStartDateTime;
	private Date processEndDateTime;
	private String epubFileSizeCompressed;
	private String epubFileSizeUncompressed;
	private String numberOfFiles;
	private String checkedWithEpubVersion;
	private List<CheckMessage> listOfMessage = new ArrayList<CheckMessage>();
	private int numberOfFatal;
	private int numberOfWarnings;
	private int numberOfException;
	private int numberOfInfos;

	public static volatile CheckingReport checkingReport = new CheckingReport();

	private CheckingReport() {
	};

	public void setParameters() {
		this.epubFileName = DefaultReportImpl.ePubName;
		this.checkedWithEpubVersion = DefaultReportImpl.ePubVersion;
		File f = new File(epubFileName);
		double size = f.length();
		f = null;
		size = size / (1024 * 1024);
		size = Math.round(size * 100);
		size /= 100;
		this.epubFileSizeCompressed = String.valueOf(size);

	}

	public long processDuration() {
		long duration = processEndDateTime.getTime()
				- processStartDateTime.getTime();
		duration /= 1000;
		return duration;
	}

	public void setStartDate(Date d) {
		processStartDateTime = d;
	}

	public void setStopDate(Date d) {
		processEndDateTime = d;
	}

	public void getInformation() {
		for (CheckMessage cm : listOfMessage) {
			switch (cm.severity) {
			case ERROR:
				numberOfFatal++;
				break;
			case EXCEPTION:
				numberOfException++;
				break;
			case INFO:
				numberOfInfos++;
				break;
			case WARNING:
				numberOfWarnings++;
				break;

			}
		}
	}

	public int getnumberOfFatal() {
		return numberOfFatal;
	}

	public int getnumberOfWarnings() {
		return numberOfWarnings;
	}

	public int getnumberOfException() {
		return numberOfException;
	}

	public int getnumberOfInfos() {
		return numberOfInfos;
	}

	public static void addCheckMessage(String file, String lineNumber,
			String columnNumber, boolean error, boolean exception,
			boolean warning, boolean info, boolean printAll, boolean printSome,
			String... messageUI) {

		CheckMessage cm = new CheckMessage(file, lineNumber, columnNumber,
				messageUI);
		checkingReport.listOfMessage.add(cm);
		System.out.println(cm.toStringFilter(error, exception, warning, info,
				printAll, printSome));

	}

	public void getJsonReport(String path) throws IOException {
		FileOutputStream out = new FileOutputStream(path);
		JsonWriter jw = new JsonWriter(out);
		jw.write(this);

		jw.close();
	}

	public void getTxtReport(String path) {
		if (listOfMessage.size() > 0) {
			try {
				BufferedWriter file = new BufferedWriter(new FileWriter(path));

				file.write("@HEADER"
						+ System.getProperty("line.separator")
						+ "@type:"
						+ checkingReport.getClass().toString()
						+ System.getProperty("line.separator")
						+ "Epub File Name: "
						+ checkingReport.epubFileName
						+ System.getProperty("line.separator")
						+ "Process Start Date Time: "
						+ checkingReport.processStartDateTime.toString()
						+ System.getProperty("line.separator")
						+ "Process End Date Time: "
						+ checkingReport.processEndDateTime.toString()
						+ System.getProperty("line.separator")
						+ "Epub File size: "
						+ checkingReport.epubFileSizeCompressed
						+ " MB"
						+ System.getProperty("line.separator")
						+ "Checked With EpubVersion: "
						+ checkingReport.checkedWithEpubVersion
						+ System.getProperty("line.separator")
						+ "@END OF HEADER"
						+ System.getProperty("line.separator")
						+ "===================================================="
						+ System.getProperty("line.separator"));
				for (CheckMessage cm : listOfMessage) {
					file.write(cm.toString());

				}
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void readAdditionalInfos() {

	}

	private void addAdditionalInfo() {

	}

	public void setEndDate(Date end) {
		this.processEndDateTime = end;

	}

	public String toStringFilter(boolean error, boolean exception,
			boolean warning) {
		String text = "";
		for (CheckMessage cm : listOfMessage) {
			switch (cm.severity) {
			case ERROR:
				if (error)
					text += cm.toString();
				break;
			case EXCEPTION:
				if (exception)
					text += cm.toString();
				break;
			case WARNING:
				if (warning)
					text += cm.toString();
				break;
			}
		}

		return text;
	}

}
