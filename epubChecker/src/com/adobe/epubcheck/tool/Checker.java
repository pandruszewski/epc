/*
 * Copyright (c) 2007 Adobe Systems Incorporated
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of
 *  this software and associated documentation files (the "Software"), to deal in
 *  the Software without restriction, including without limitation the rights to
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 *  the Software, and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *    <AdobeIP#0000474>
 */

package com.adobe.epubcheck.tool;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.adobe.epubcheck.api.EpubCheck;
import com.adobe.epubcheck.api.EpubCheckFactory;
import com.adobe.epubcheck.api.Report;
import com.adobe.epubcheck.gui.EpubFrame;
import com.adobe.epubcheck.nav.NavCheckerFactory;
import com.adobe.epubcheck.opf.DocumentValidator;
import com.adobe.epubcheck.opf.DocumentValidatorFactory;
import com.adobe.epubcheck.opf.OPFCheckerFactory;
import com.adobe.epubcheck.ops.OPSCheckerFactory;
import com.adobe.epubcheck.overlay.OverlayCheckerFactory;
import com.adobe.epubcheck.util.Archive;
import com.adobe.epubcheck.util.CheckingReport;
import com.adobe.epubcheck.util.DefaultReportImpl;
import com.adobe.epubcheck.util.EPUBVersion;
import com.adobe.epubcheck.util.FileResourceProvider;
import com.adobe.epubcheck.util.GenericResourceProvider;
import com.adobe.epubcheck.util.InvalidVersionException;
import com.adobe.epubcheck.util.Messages;
import com.adobe.epubcheck.util.OPSType;
import com.adobe.epubcheck.util.URLResourceProvider;

public class Checker {
	private static String path = null, mode = null;
	private static EPUBVersion version = EPUBVersion.VERSION_3;
	private static OPSType opsType;
	private static boolean expanded = false;
	private static boolean keep = false;
	private static HashMap<OPSType, String> modeMimeTypeMap;
	private static boolean json = false;
	private static boolean jsonOutput = false;
	private static boolean txt = false;
	private static String outputJsonPath = "";
	public static boolean error = false;
	public static boolean exception = false;
	public static boolean warning = false;
	public static boolean info = false;
	public static boolean printAll = true;
	public static boolean printSome = false;

	static {
		HashMap<OPSType, String> map = new HashMap<OPSType, String>();

		map.put(new OPSType("xhtml", EPUBVersion.VERSION_2),
				"application/xhtml+xml");
		map.put(new OPSType("xhtml", EPUBVersion.VERSION_3),
				"application/xhtml+xml");

		map.put(new OPSType("svg", EPUBVersion.VERSION_2), "image/svg+xml");
		map.put(new OPSType("svg", EPUBVersion.VERSION_3), "image/svg+xml");

		map.put(new OPSType("mo", EPUBVersion.VERSION_3),
				"application/smil+xml");
		map.put(new OPSType("nav", EPUBVersion.VERSION_3), "nav");
		modeMimeTypeMap = map;
	}

	private static HashMap<OPSType, DocumentValidatorFactory> documentValidatorFactoryMap;

	static {
		HashMap<OPSType, DocumentValidatorFactory> map = new HashMap<OPSType, DocumentValidatorFactory>();
		map.put(new OPSType(null, EPUBVersion.VERSION_2),
				EpubCheckFactory.getInstance());
		map.put(new OPSType(null, EPUBVersion.VERSION_3),
				EpubCheckFactory.getInstance());

		map.put(new OPSType("opf", EPUBVersion.VERSION_2),
				OPFCheckerFactory.getInstance());
		map.put(new OPSType("opf", EPUBVersion.VERSION_3),
				OPFCheckerFactory.getInstance());

		map.put(new OPSType("xhtml", EPUBVersion.VERSION_2),
				OPSCheckerFactory.getInstance());
		map.put(new OPSType("xhtml", EPUBVersion.VERSION_3),
				OPSCheckerFactory.getInstance());

		map.put(new OPSType("svg", EPUBVersion.VERSION_2),
				OPSCheckerFactory.getInstance());
		map.put(new OPSType("svg", EPUBVersion.VERSION_3),
				OPSCheckerFactory.getInstance());

		map.put(new OPSType("mo", EPUBVersion.VERSION_3),
				OverlayCheckerFactory.getInstance());
		map.put(new OPSType("nav", EPUBVersion.VERSION_3),
				NavCheckerFactory.getInstance());
		documentValidatorFactoryMap = map;
	}

	public static int validateFile(GenericResourceProvider resourceProvider,
			String fileName, String mimeType, EPUBVersion version, Report report) {

		opsType = new OPSType(mode, version);

		DocumentValidatorFactory factory = (DocumentValidatorFactory) documentValidatorFactoryMap
				.get(opsType);

		if (factory == null) {
			System.out.println(Messages.DISPLAY_HELP);

			report.exception(
					fileName,
					new RuntimeException(String.format(
							Messages.MODE_VERSION_NOT_SUPPORTED, mode, version)));

			throw new RuntimeException(String.format(
					Messages.MODE_VERSION_NOT_SUPPORTED, mode, version));
		}

		DocumentValidator check = factory.newInstance(report, path,
				resourceProvider, (String) modeMimeTypeMap.get(opsType),
				version);

		if (check.validate()) {
			// System.out.println(Messages.NO_ERRORS__OR_WARNINGS);
			// JOptionPane.showMessageDialog(null,
			// Messages.NO_ERRORS__OR_WARNINGS, "Info",
			// JOptionPane.INFORMATION_MESSAGE);
			EpubFrame.processInfoText.append(Messages.NO_ERRORS__OR_WARNINGS);
			return 0;
		}
		// System.err.println(Messages.THERE_WERE_ERRORS);
		EpubFrame.processInfoText.append(Messages.THERE_WERE_ERRORS);
		return 1;
	}

	public static int validateFile(String path, String mimeType,
			EPUBVersion version, Report report) {

		GenericResourceProvider resourceProvider;

		if (path.startsWith("http://") || path.startsWith("https://"))
			resourceProvider = new URLResourceProvider(path);
		else
			resourceProvider = new FileResourceProvider(path);

		opsType = new OPSType(mode, version);

		DocumentValidatorFactory factory = (DocumentValidatorFactory) documentValidatorFactoryMap
				.get(opsType);

		if (factory == null) {
			System.out.println(Messages.DISPLAY_HELP);
			report.exception(
					path,
					new RuntimeException(String.format(
							Messages.MODE_VERSION_NOT_SUPPORTED, mode, version)));

			throw new RuntimeException(String.format(
					Messages.MODE_VERSION_NOT_SUPPORTED, mode, version));
		}

		DocumentValidator check = factory.newInstance(report, path,
				resourceProvider, (String) modeMimeTypeMap.get(opsType),
				version);

		if (check.validate()) {
			// System.out.println(Messages.NO_ERRORS__OR_WARNINGS);
			// JOptionPane.showMessageDialog(null,
			// Messages.NO_ERRORS__OR_WARNINGS, "Info",
			// JOptionPane.INFORMATION_MESSAGE);
			EpubFrame.processInfoText.append(Messages.NO_ERRORS__OR_WARNINGS);
			return 0;
		}
		// System.err.println(Messages.THERE_WERE_ERRORS);
		EpubFrame.processInfoText.append(Messages.THERE_WERE_ERRORS);
		return 1;

	}

	public static void main(String[] args) {

		// EpubFrame ePubWindow = new EpubFrame();
		// ePubWindow.show();

		
		  String[] ar = { "C:/Users/user/Desktop/bezdomni.epub", "--output",
		  "C:/Users/user/Desktop/LALA.json", "--warn", "-txt" };
		 
		CheckingReport.checkingReport.setStartDate(new Date());
		System.exit(run(ar));
	}

	public static int run(String[] args) {
		Report report;
		try {
			processArguments(args);

			if (expanded) {
				Archive epub = new Archive(path, keep);
				report = new DefaultReportImpl(epub.getEpubName());
				epub.createArchive();

				EpubCheck check = new EpubCheck(epub.getEpubFile(), report);
				if (check.validate()) {
					System.out.println(Messages.NO_ERRORS__OR_WARNINGS);
					// JOptionPane.showMessageDialog(null,
					// Messages.NO_ERRORS__OR_WARNINGS, "Info",
					// JOptionPane.INFORMATION_MESSAGE);
					EpubFrame.processInfoText
							.append(Messages.NO_ERRORS__OR_WARNINGS);
					return 0;
				}
				// System.err.println(Messages.THERE_WERE_ERRORS);
				EpubFrame.processInfoText.append(Messages.THERE_WERE_ERRORS);
				if ((report.getErrorCount() > 0 || report.getExceptionCount() > 0)
						&& keep) {
					// keep if valid or only warnings
					System.err.println(Messages.DELETING_ARCHIVE);
					epub.deleteEpubFile();
				}

				return 1;
			}

			if (mode != null) {
				report = new DefaultReportImpl(path, String.format(
						Messages.SINGLE_FILE, mode, version.toString()));
			} else {
				report = new DefaultReportImpl(path);
			}

			return validateFile(path, mode, version, report);

		} catch (Throwable e) {
			e.printStackTrace();
			return 1;
		} finally {
			CheckingReport.checkingReport.setParameters();
			Pattern p = Pattern.compile("[.][eE][pP][uU][bB]");
			Matcher m = p.matcher(path);
			if (json == true) {
				try {
					CheckingReport.checkingReport.getJsonReport(m
							.replaceAll(".check.json"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				json = false;
			} else if (jsonOutput == true) {
				jsonOutput = false;
				if (outputJsonPath.matches(".+[.][jJ][sS][oO][nN]")) {
					try {
						CheckingReport.checkingReport
								.getJsonReport(outputJsonPath);
					} catch (IOException e) {
						System.out.println("Incorrect path to save JsonFile.");
					}
				}
			}

			CheckingReport.checkingReport.setStopDate(new Date());
			if (txt == true) {
				CheckingReport.checkingReport
						.getTxtReport(m.replaceAll(".txt"));
				txt = false;
			}
			long duration = CheckingReport.checkingReport.processDuration();
			System.out.println("Proces duration: " + (duration / (60 * 60))
					+ "h:" + (duration / 60) + "m:" + duration + "s");
		}

	}

	/**
	 * This method iterates through all of the arguments passed to main to find
	 * accepted flags and the name of the file to check. This method returns the
	 * last argument that ends with ".epub" (which is assumed to be the file to
	 * check) Here are the currently accepted flags: <br>
	 * <br>
	 * -? or -help = display usage instructions <br>
	 * -v or -version = display tool version number
	 * 
	 * @param args
	 *            String[] containing arguments passed to main
	 * @return the name of the file to check
	 */
	public static void processArguments(String[] args) {
		// Exit if there are no arguments passed to main
		displayVersion();
		if (args.length < 1) {

			System.err.println(Messages.ARGUMENT_NEEDED);
			System.exit(1);
		}

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-version") || args[i].equals("-v"))
				if (i + 1 < args.length) {
					++i;
					if (args[i].equals("2.0") || args[i].equals("2"))
						version = EPUBVersion.VERSION_2;
					else if (args[i].equals("3.0") || args[i].equals("3"))
						version = EPUBVersion.VERSION_3;
					else {
						System.out.println(Messages.DISPLAY_HELP);
						throw new RuntimeException(new InvalidVersionException(
								InvalidVersionException.UNSUPPORTED_VERSION));
					}
					continue;
				} else {
					System.out.println(Messages.DISPLAY_HELP);
					throw new RuntimeException(String.format(
							Messages.AFTER_ARGUMENT_EXPECTED, "-v or -version",
							"version"));
				}
			else if (args[i].equals("-o")) {
				json = true;
			} else if (args[i].equals("--output")) {
				jsonOutput = true;

			} else if (args[i].equals("-f") || args[i].equals("--fatal")) {
				error = true;
			} else if (args[i].equals("-e") || args[i].equals("--error")) {
				error = true;
				exception = true;
			} else if (args[i].equals("-w") || args[i].equals("--warn")) {
				error = true;
				exception = true;
				warning = true;
			} else if (args[i].equals("-i") || args[i].equals("--info")) {
				info = true;
			} else if (args[i].matches(".+[.][jJ][sS][oO][nN]") && jsonOutput) {
				outputJsonPath = args[i];
			} else if (args[i].equals("-txt"))
				txt = true;
			else if (args[i].equals("-verbose")) {
				printAll = true;
				printSome = false;
			} else if (args[i].equals("-terse")) {
				printAll = false;
				printSome = true;
			} else if (args[i].equals("-mode"))
				if (i + 1 < args.length) {
					mode = args[++i];
					if (mode.equals("exp")) {
						expanded = true;
					} else {
						expanded = false;
					}
					continue;
				} else {
					System.out.println(Messages.DISPLAY_HELP);
					throw new RuntimeException(String.format(
							Messages.AFTER_ARGUMENT_EXPECTED, "-mode", "type"));
				}
			else if (args[i].equals("-save")) {
				keep = true;
				continue;
			} else if (args[i].equals("-help") || args[i].equals("-?"))
				displayHelp(); // display help message
			else
				path = args[i];
		}

		if (path != null) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < path.length(); i++)
				if (path.charAt(i) == '\\')
					sb.append('/');
				else
					sb.append(path.charAt(i));
			path = sb.toString();
		}

		if (path == null) {
			System.err.println(Messages.NO_FILE_SPECIFIED);
			System.err.println(Messages.END_OF_EXECUTION);
			System.exit(1);
		} else if (path.matches(".+\\.[Ee][Pp][Uu][Bb]")) {
			if (mode != null || version != EPUBVersion.VERSION_3) {
				System.err.println(Messages.MODE_VERSION_IGNORED);
				mode = null;
			}
		} else if (mode == null)
			throw new RuntimeException(Messages.MODE_REQUIRED);

	}

	/**
	 * This method displays a short help message that describes the command-line
	 * usage of this tool
	 */
	public static void displayHelp() {
		displayVersion();

		System.out.println("When running this tool, the first argument "
				+ "should be the name (with the path) of the file to check.");
		System.out
				.println("If checking a non-epub "
						+ "file, the epub version of the file must be specified using -v "
						+ "and the type of the file using -mode.");
		System.out.println("The default version is: 3.0.");
		System.out.println("Modes and versions supported: ");
		System.out.println("-mode opf -v 2.0");
		System.out.println("-mode opf -v 3.0");

		System.out.println("-mode xhtml -v 2.0");
		System.out.println("-mode xhtml -v 3.0");

		System.out.println("-mode svg -v 2.0");
		System.out.println("-mode svg -v 3.0");
		System.out.println("-mode nav -v 3.0");
		System.out.println("-mode mo  -v 3.0 // For Media Overlays validation");
		System.out.println("-mode exp  // For expanded EPUB archives");

		System.out.println("This tool also accepts the following flags:");
		System.out
				.println("-save 	= saves the epub created from the expended epub");
		System.out.println("-? or -help 	= displays this help message");
	}

	public static void displayVersion() {
		System.out.println("Epubcheck Version " + EpubCheck.VERSION + "\n");
	}
}
