package com.adobe.epubcheck.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.adobe.epubcheck.tool.Checker;
import com.adobe.epubcheck.util.CheckingReport;
import com.adobe.epubcheck.util.DefaultReportImpl;

public class EpubFrame extends JFrame {
	private JTextField pathLoadText;
	private JTextField pathSaveText;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton loadFileButton;
	private JButton startButton = new JButton("Start");;
	private JButton cancelButton;
	private JButton saveFileButton = new JButton("Save");;
	private JRadioButton json;
	private JRadioButton text;
	private JTabbedPane tab;
	private String tableTitleString[] = { "Property", "Value" };
	public static String[][] tableValueString = new String[3][3];
	private JTable fileTable;
	public static JLabel status;
	private JTextField errorText;
	private JTextField warningText;
	private JTextField exceptionText;
	private JTextField infoText;
	private JFileChooser loadFileChooser = new JFileChooser();
	private JTextArea fileInfoTextArea;
	private JFileChooser saveFileChooser = new JFileChooser();
	public static JTextArea processInfoText = new JTextArea();

	public EpubFrame() {
		setTitle("EPub");
		getContentPane().setBackground(Color.WHITE);
		setBackground(Color.WHITE);
		setMaximumSize(new Dimension(1100, 700));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1100, 700);
		this.setResizable(false);
		getContentPane().setLayout(null);

		loadFileChooser.setFileFilter(new FileNameExtensionFilter(".epub",
				"epub"));
		startButton.setEnabled(false);
		saveFileButton.setEnabled(false);
		processInfoText.setEditable(false);

		JPanel filePanel = new JPanel();
		filePanel.setBounds(10, 11, 292, 650);
		filePanel.setBackground(new Color(255, 204, 153));
		getContentPane().add(filePanel);
		filePanel.setLayout(null);

		JLabel lblNewLabel_2 = new JLabel("File Info");
		lblNewLabel_2.setBounds(10, 79, 46, 14);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		filePanel.add(lblNewLabel_2);

		JLabel lblFile = new JLabel("File catalog structure");
		lblFile.setBounds(10, 327, 135, 14);
		filePanel.add(lblFile);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 352, 272, 287);
		filePanel.add(scrollPane_1);

		JPanel fileInfoPanel = new JPanel();
		fileInfoPanel.setBounds(10, 104, 272, 192);
		filePanel.add(fileInfoPanel);
		fileInfoPanel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 272, 192);
		fileInfoPanel.add(scrollPane);

		tableValueString[0][0] = "Filename";
		tableValueString[1][0] = "File Size compressed";
		tableValueString[2][0] = "File Size uncompressed";
		fileTable = new JTable(tableValueString, tableTitleString);
		fileTable.setEnabled(false);
		fileTable.setFont(new Font("Arial", Font.PLAIN, 11));
		scrollPane.setViewportView(fileTable);

		JPanel pathPanel = new JPanel();
		pathPanel.setBounds(312, 11, 772, 57);
		pathPanel.setBackground(new Color(255, 204, 153));
		getContentPane().add(pathPanel);
		pathPanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Path to file");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel.setBounds(10, 0, 80, 14);
		pathPanel.add(lblNewLabel);

		pathLoadText = new JTextField();
		pathLoadText.setEditable(false);
		pathLoadText.setBounds(10, 25, 653, 20);
		pathPanel.add(pathLoadText);
		pathLoadText.setColumns(10);

		loadFileButton = new JButton("Open");
		loadFileButton.setBounds(673, 24, 89, 23);
		pathPanel.add(loadFileButton);

		JPanel checkingPanel = new JPanel();
		checkingPanel.setBackground(new Color(255, 204, 153));
		checkingPanel.setBounds(312, 79, 772, 385);
		getContentPane().add(checkingPanel);
		checkingPanel.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Checking process");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 11, 110, 14);
		checkingPanel.add(lblNewLabel_1);

		startButton.setBounds(10, 36, 89, 23);
		checkingPanel.add(startButton);

		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(109, 36, 89, 23);
		checkingPanel.add(cancelButton);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(254, 36, 508, 79);
		checkingPanel.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel_3 = new JLabel("Status:");
		lblNewLabel_3.setFont(new Font("Arial", Font.PLAIN, 15));
		lblNewLabel_3.setBounds(10, 30, 46, 14);
		panel.add(lblNewLabel_3);

		status = new JLabel("Not started");
		status.setFont(new Font("Arial", Font.PLAIN, 15));
		status.setBounds(101, 31, 168, 13);
		panel.add(status);

		tab = new JTabbedPane(JTabbedPane.TOP);
		tab.setBounds(10, 160, 752, 214);
		checkingPanel.add(tab);

		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.WHITE);
		tab.addTab("Summary", null, panel_1, null);
		tab.setBackgroundAt(0, Color.WHITE);
		panel_1.setLayout(null);

		JLabel lblFatal = new JLabel("Fatal");
		lblFatal.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblFatal.setBounds(10, 58, 56, 14);
		panel_1.add(lblFatal);

		JLabel lblWarning = new JLabel("Warning");
		lblWarning.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblWarning.setBounds(10, 83, 56, 14);
		panel_1.add(lblWarning);

		JLabel lblException = new JLabel("Exception");
		lblException.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblException.setBounds(10, 108, 56, 14);
		panel_1.add(lblException);

		JLabel lblInfo = new JLabel("Info");
		lblInfo.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblInfo.setBounds(10, 133, 56, 14);
		panel_1.add(lblInfo);

		errorText = new JTextField();
		errorText.setEditable(false);
		errorText.setBounds(95, 56, 86, 20);
		panel_1.add(errorText);
		errorText.setColumns(10);

		warningText = new JTextField();
		warningText.setEditable(false);
		warningText.setBounds(95, 81, 86, 20);
		panel_1.add(warningText);
		warningText.setColumns(10);

		exceptionText = new JTextField();
		exceptionText.setEditable(false);
		exceptionText.setBounds(95, 106, 86, 20);
		panel_1.add(exceptionText);
		exceptionText.setColumns(10);

		infoText = new JTextField();
		infoText.setEditable(false);
		infoText.setBounds(95, 131, 86, 20);
		panel_1.add(infoText);
		infoText.setColumns(10);

		JLabel lblNoOfMessages = new JLabel("No of messages");
		lblNoOfMessages.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNoOfMessages.setBounds(10, 11, 123, 14);
		panel_1.add(lblNoOfMessages);

		JLabel lblProcessInfo = new JLabel("Process Info");
		lblProcessInfo.setBounds(325, 13, 94, 14);
		panel_1.add(lblProcessInfo);

		processInfoText.setBounds(325, 37, 412, 138);
		panel_1.add(processInfoText);

		JPanel panel_2 = new JPanel();
		tab.addTab("Messages", null, panel_2, null);
		panel_2.setLayout(null);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 11, 727, 164);
		panel_2.add(scrollPane_2);

		fileInfoTextArea = new JTextArea();
		scrollPane_2.setViewportView(fileInfoTextArea);

		JPanel outputPanel = new JPanel();
		outputPanel.setBackground(new Color(255, 204, 153));
		outputPanel.setBounds(312, 475, 772, 186);
		getContentPane().add(outputPanel);
		outputPanel.setLayout(null);

		JLabel lblOutput = new JLabel("Output");
		lblOutput.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblOutput.setBounds(10, 11, 46, 14);
		outputPanel.add(lblOutput);

		JLabel lblFolder = new JLabel("Folder");
		lblFolder.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblFolder.setBounds(20, 43, 46, 14);
		outputPanel.add(lblFolder);

		pathSaveText = new JTextField();
		pathSaveText.setEditable(false);
		pathSaveText.setBounds(64, 40, 599, 20);
		outputPanel.add(pathSaveText);
		pathSaveText.setColumns(10);

		saveFileButton.setBounds(673, 40, 89, 23);
		outputPanel.add(saveFileButton);

		JLabel lblFormat = new JLabel("Format");
		lblFormat.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblFormat.setBounds(20, 84, 46, 14);
		outputPanel.add(lblFormat);

		json = new JRadioButton("JSON");
		buttonGroup.add(json);
		json.setBounds(87, 81, 90, 23);
		json.setBackground(new Color(255, 204, 153));
		outputPanel.add(json);

		text = new JRadioButton("Text");
		buttonGroup.add(text);
		text.setBounds(186, 81, 109, 23);
		text.setBackground(new Color(255, 204, 153));
		outputPanel.add(text);

		initButtonEvent();
	}

	public void initButtonEvent() {
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (pathLoadText.getText().length() > 0
						&& pathLoadText.getText().matches(
								".+\\.[eE][pP][uU][bB]")) {
					// JOptionPane.showMessageDialog(null,
					// pathLoadText.getText());

					processInfoText.setText("");
					status.setText("Working");
					saveFileButton.setEnabled(false);
					String[] table = { loadFileChooser.getSelectedFile()
							.toString() };
					startButton.setEnabled(false);
					Checker.run(table);
					// new StartingThread(table).start();


					saveFileButton.setEnabled(true);
					startButton.setEnabled(false);
					CheckingReport.checkingReport.getInformation();
					errorText.setText(String
							.valueOf(CheckingReport.checkingReport
									.getnumberOfFatal()));
					exceptionText.setText(String
							.valueOf(CheckingReport.checkingReport
									.getnumberOfException()));
					infoText.setText(String.valueOf(CheckingReport.checkingReport
							.getnumberOfInfos()));
					warningText.setText(String
							.valueOf(CheckingReport.checkingReport
									.getnumberOfWarnings()));
					fileInfoTextArea.setText(CheckingReport.checkingReport
							.toString());
					status.setText("Done");
					CheckingReport.checkingReport.setEndDate(new Date());
				}
				// run(table);

			}

		});

		saveFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int choose = -2;
				String format = "";

				if (json.isSelected()) {
					saveFileChooser = new JFileChooser();
					format = ".json";
					saveFileChooser.setFileFilter(new FileNameExtensionFilter(
							format, format));
				} else if (text.isSelected()) {
					format = ".txt";
					saveFileChooser = new JFileChooser();
					saveFileChooser.setFileFilter(new FileNameExtensionFilter(
							format, "txt"));
				}

				if (json.isSelected() || text.isSelected()) {
					choose = saveFileChooser.showSaveDialog(null);

				} else {
					JOptionPane.showMessageDialog(null,
							"Please Check Format of File.");
				}
				if (choose == JFileChooser.APPROVE_OPTION) {
					System.out.println(saveFileChooser.getSelectedFile());
					pathSaveText.setText(saveFileChooser.getSelectedFile()
							.toString() + format);
					if (text.isSelected())
						CheckingReport.checkingReport.getTxtReport(pathSaveText
								.getText());
					else if (json.isSelected())
						try {
							CheckingReport.checkingReport
									.getJsonReport(pathSaveText.getText());
						} catch (IOException e) {
							e.printStackTrace();
						}

				}
			}

		});

		loadFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int choose = loadFileChooser.showDialog(null, "Load");
				if (choose == JFileChooser.APPROVE_OPTION
						&& loadFileChooser.getSelectedFile().toString()
								.matches(".+\\.[eE][pP][uU][bB]")) {

					File f = new File(loadFileChooser.getSelectedFile()
							.toString());
					float a = f.length();
					a /= 1048576;
					BigDecimal bd = new BigDecimal(a);
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits(2);
					/*CheckingReport.checkingReport.setParameters(f.getName(),
							new Date(), String.valueOf(a),
							DefaultReportImpl.ePubVersion);*/
					tableValueString[1][1] = nf.format(a) + " MB";
					tableValueString[0][1] = f.getName();
					fileTable.repaint();

					String[] table = { loadFileChooser.getSelectedFile()
							.toString() };
					pathLoadText.setText(loadFileChooser.getSelectedFile()
							.toString());
					startButton.setEnabled(true);

					// Checker.run(table);
					// run(table);
				}

			}

		});

	}
}
