package com.webscraper;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper
{

	public static WebScraper window;

	public JFrame frame;

	private JDesktopPane startPane;
	private JDesktopPane selectPane;
	private JDesktopPane validPane;
	private JDesktopPane readyPane;

	public JDesktopPane[] workPane;

	private JDesktopPane reviewPane;

	private JDesktopPane waitPane;
	
	public CardLayout card;

	// StartPane
	private JLabel logo;
	private JLabel logo_name;
	private FreeButton contProj;
	private FreeButton newProj;

	// SelectPane
	private JLabel uploadLbl;
	private FreeButton selecBack, selecNext;
	private JLabel uploadDesc;
	private JLabel uploadTech;
	private JTextField upTechText;
	private FreeButton browseTech;
	private JLabel uploadNettv;
	private JTextField upNettvText;
	private FreeButton browseNettv;
	private boolean techBool, nettvBool, autoChange;
	private JLabel correctTech, correctNettv;

	// ValidPane
	private JLabel validLbl;
	private FreeButton validBack, validNext;
	private JLabel validDesc;
	private JLabel validTech;
	private JTextArea validTechText;
	private FreeButton editTech;
	private JLabel validNettv;
	private JTextArea validNettvText;
	private FreeButton editNettv;

	// ReadyPane
	private JLabel readyLbl;
	private JLabel readyTime;
	private FreeButton startProj, readyBack;
	private JLabel freelanceLogo;

	// WorkPane
	private JLabel freelogo;
	private JLabel dayPart;
	private JLabel kwLbl;
	public JTextField[] keyword;
	public JLabel[] loader;
	private JLabel urlLbl;
	public JTextField[] url;
	private JLabel bannerLbl;
	public JTextField[] banner;
	public FreeButton[] retrieve;
	private JCheckBox autoSetting;
	private FreeButton[] workBack;
	private FreeButton[] workNext;
	public FreeButton[] done, indicator;
	public int[] id;

	// For the Viewer
	public Scanner techInput, nettvInput;
	public boolean launched;
	public int counter[];
	public String folder;
	public boolean autoNext;

	// Review Pane
	private JLabel rlogo;
	private JLabel rdayPart;
	private FreeButton[] doc;
	private FreeButton[] checked;
	private JLabel[] veriLbl;
	private FreeButton finishWork;
	private FreeButton finishProj;
	private FreeButton reviewBack;

	// Wait Pane
	private JLabel timeLbl;
	private JLabel timeLeft;
	private FreeButton exitWait;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					window = new WebScraper();
					window.frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WebScraper()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException ex)
		{
		}

		card = new CardLayout(0, 0);

		launched = false;

		final int length = 730;
		final int height = 530;

		frame = new JFrame("Web Scraper");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(WebScraper.class.getResource("/icon.png")));
		frame.setSize(length, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.WHITE);
		frame.getContentPane().setLayout(card);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent ev)
			{
				if (getCurrentCard() != "startPane" && getCurrentCard() != "waitPane")
				{
					if (getConfirmation())
						System.exit(0);
				}
				else
					System.exit(0);
			}
		});

		startPane = new JDesktopPane();
		startPane.setBackground(Color.WHITE);
		startPane.setName("startPane");
		frame.getContentPane().add(startPane, "startPane");

		logo = new JLabel();
		logo.setBounds(176, 43, 51, 51);
		logo.setIcon(new ImageIcon(WebScraper.class.getResource("/logo_icon.png")));
		startPane.add(logo);

		logo_name = new JLabel("Web Scraper");
		logo_name.setForeground(new Color(86, 86, 86));
		logo_name.setFont(new Font("Cambria", Font.PLAIN, 49));
		logo_name.setBounds(255, 43, 271, 51);
		startPane.add(logo_name);

		contProj = new FreeButton("Continue");
		contProj.setForeground(Color.WHITE);
		contProj.setFont(new Font("Calibri", Font.PLAIN, 29));
		contProj.setBounds(225, 143, 271, 51);
		contProj.setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
		contProj.setBorderPainted(false);
		contProj.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				startWork("startPane");
			}
		});
		startPane.add(contProj);

		newProj = new FreeButton("New Project");
		newProj.setForeground(Color.WHITE);
		newProj.setFont(new Font("Calibri", Font.PLAIN, 29));
		newProj.setBounds(225, 210, 271, 51);
		newProj.setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
		newProj.setBorderPainted(false);
		newProj.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				startNew();
			}
		});
		startPane.add(newProj);

		if (!new File(System.getProperty("user.home") + "\\Documents\\WebScraper\\current.txt").exists())
		{
			contProj.setVisible(false);
			newProj.setBounds(225, 143, 271, 51);
		}

		card.show(frame.getContentPane(), "startPane");
	}

	public void startNew()
	{
		selectPane = new JDesktopPane();
		selectPane.setBackground(Color.WHITE);
		selectPane.setName("selectPane");
		frame.getContentPane().add(selectPane, "selectPane");

		validPane = new JDesktopPane();
		validPane.setBackground(new Color(250, 250, 250));
		validPane.setName("validPane");
		frame.getContentPane().add(validPane, "validPane");

		readyPane = new JDesktopPane();
		readyPane.setBackground(Color.WHITE);
		readyPane.setName("readyPane");
		frame.getContentPane().add(readyPane, "readyPane");

		autoChange = false;

		selecBack = new FreeButton("Back");
		selecBack.setForeground(Color.WHITE);
		selecBack.setFont(new Font("Calibri", Font.PLAIN, 19));
		selecBack.setBounds(495, 443, 100, 41);
		selecBack.setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
		selecBack.setBorderPainted(false);
		selecBack.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				card.show(frame.getContentPane(), "startPane");
			}
		});
		selectPane.add(selecBack);

		uploadLbl = new JLabel("Upload Keyword Lists");
		uploadLbl.setFont(new Font("Cambria", Font.BOLD, 16));
		uploadLbl.setForeground(new Color(86, 86, 86));
		uploadLbl.setBounds(32, 38, 184, 24);
		selectPane.add(uploadLbl);

		uploadDesc = new JLabel("Please upload your Techvy and Nettv4u keyword lists. Single column files only.");
		uploadDesc.setFont(new Font("Cambria", Font.PLAIN, 15));
		uploadDesc.setBounds(32, 63, 578, 24);
		selectPane.add(uploadDesc);

		JSeparator separator = new JSeparator();
		separator.setBounds(375, 85, 148, 2);
		selectPane.add(separator);

		uploadTech = new JLabel("Techyv Keyword List:");
		uploadTech.setFont(new Font("Cambria", Font.BOLD, 13));
		uploadTech.setForeground(new Color(86, 86, 86));
		uploadTech.setBounds(32, 98, 171, 24);
		selectPane.add(uploadTech);

		upTechText = new JTextField();
		upTechText.setBounds(32, 133, 433, 20);
		upTechText.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e)
			{
				if (autoChange)
					autoChange = false;
				else
					warn();
			}

			public void removeUpdate(DocumentEvent e)
			{
				if (autoChange)
					autoChange = false;
				else
					warn();
			}

			public void insertUpdate(DocumentEvent e)
			{
				if (autoChange)
					autoChange = false;
				else
					warn();
			}

			public void warn()
			{
				techBool = validateInput(upTechText.getText());

				if (techBool)
				{
					correctTech.setIcon(new ImageIcon(WebScraper.class.getResource("/correct.png")));
				}
				else
				{
					correctTech.setIcon(new ImageIcon(WebScraper.class.getResource("/wrong.png")));
				}
				validateNext();
			}
		});
		selectPane.add(upTechText);

		browseTech = new FreeButton("Browse...");
		browseTech.setForeground(Color.WHITE);
		browseTech.setBounds(470, 133, 100, 20);
		browseTech.setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
		browseTech.setBorderPainted(false);
		browseTech.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(System.getProperty("user.home") + "\\Desktop"));
				chooser.setDialogTitle("Select the keyword list");
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setFileFilter(new FileFilter()
				{
					@Override
					public boolean accept(File file)
					{
						if (file.isDirectory())
						{
							return true;
						}
						else if (file.getName().lastIndexOf('.') != -1)
						{
							String ext = file.getName().substring(file.getName().lastIndexOf('.'));
							return ext.equals(".xls") || ext.equals(".xlsx");
						}
						return false;
					}

					@Override
					public String getDescription()
					{
						return "Excel File: .xls, .xlsx";
					}
				});
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				{
					techBool = validateInput(chooser.getSelectedFile().getPath());

					if (techBool)
					{
						autoChange = true;
						upTechText.setText(chooser.getSelectedFile().getPath());
						correctTech.setIcon(new ImageIcon(WebScraper.class.getResource("/correct.png")));
					}
					else
					{
						autoChange = true;
						upTechText.setText("Choose a proper input file...");
						correctTech.setIcon(new ImageIcon(WebScraper.class.getResource("/wrong.png")));
					}
					validateNext();

				}
			}
		});
		selectPane.add(browseTech);

		uploadNettv = new JLabel("Nettv4u Keyword List:");
		uploadNettv.setFont(new Font("Cambria", Font.BOLD, 13));
		uploadNettv.setForeground(new Color(86, 86, 86));
		uploadNettv.setBounds(32, 170, 171, 24);
		selectPane.add(uploadNettv);

		upNettvText = new JTextField();
		upNettvText.setBounds(32, 205, 433, 20);
		upNettvText.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e)
			{
				if (autoChange)
					autoChange = false;
				else
					warn();
			}

			public void removeUpdate(DocumentEvent e)
			{
				if (autoChange)
					autoChange = false;
				else
					warn();
			}

			public void insertUpdate(DocumentEvent e)
			{
				if (autoChange)
					autoChange = false;
				else
					warn();
			}

			public void warn()
			{
				nettvBool = validateInput(upNettvText.getText());

				if (nettvBool)
				{
					correctNettv.setIcon(new ImageIcon(WebScraper.class.getResource("/correct.png")));
				}
				else
				{
					correctNettv.setIcon(new ImageIcon(WebScraper.class.getResource("/wrong.png")));
				}
				validateNext();
			}
		});
		selectPane.add(upNettvText);

		browseNettv = new FreeButton("Browse...");
		browseNettv.setForeground(Color.WHITE);
		browseNettv.setBounds(470, 205, 100, 20);
		browseNettv.setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
		browseNettv.setBorderPainted(false);
		browseNettv.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent evt)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(System.getProperty("user.home") + "\\Desktop"));
				chooser.setDialogTitle("Select the keyword list");
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setFileFilter(new FileFilter()
				{
					@Override
					public boolean accept(File file)
					{
						if (file.isDirectory())
						{
							return true;
						}
						else if (file.getName().lastIndexOf('.') != -1)
						{
							String ext = file.getName().substring(file.getName().lastIndexOf('.'));
							return ext.equals(".xls") || ext.equals(".xlsx");
						}
						return false;
					}

					@Override
					public String getDescription()
					{
						return "Excel File: .xls, .xlsx";
					}
				});
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				{
					nettvBool = validateInput(chooser.getSelectedFile().getPath());

					if (nettvBool)
					{
						autoChange = true;
						upNettvText.setText(chooser.getSelectedFile().getPath());
						correctNettv.setIcon(new ImageIcon(WebScraper.class.getResource("/correct.png")));
					}
					else
					{
						autoChange = true;
						upNettvText.setText("Choose a proper input file...");
						correctNettv.setIcon(new ImageIcon(WebScraper.class.getResource("/wrong.png")));
					}
					validateNext();
				}
				else
				{
					System.out.println("No Selection ");
				}
			}

		});
		selectPane.add(browseNettv);

		correctTech = new JLabel("");
		correctTech.setBounds(579, 133, 20, 20);
		selectPane.add(correctTech);

		correctNettv = new JLabel("");
		correctNettv.setBounds(579, 205, 20, 20);
		selectPane.add(correctNettv);

		selecNext = new FreeButton("Next");
		selecNext.setForeground(Color.WHITE);
		selecNext.setFont(new Font("Calibri", Font.PLAIN, 19));
		selecNext.setBounds(605, 443, 100, 41);
		selecNext.setBackgroundColor(Color.WHITE.darker(), Color.GRAY);
		selecNext.setBorderPainted(false);
		selecNext.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				card.show(frame.getContentPane(), "validPane");

				fillTechData(upTechText.getText());
				fillNettvData(upNettvText.getText());
			}
		});
		selecNext.setEnabled(false);
		selectPane.add(selecNext);

		validLbl = new JLabel("Validate or Edit Keyword Data");
		validLbl.setForeground(new Color(86, 86, 86));
		validLbl.setFont(new Font("Cambria", Font.BOLD, 16));
		validLbl.setBounds(34, 11, 235, 24);
		validPane.add(validLbl);

		validBack = new FreeButton("Back");
		validBack.setForeground(Color.WHITE);
		validBack.setFont(new Font("Calibri", Font.PLAIN, 19));
		validBack.setBounds(495, 443, 100, 41);
		validBack.setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
		validBack.setBorderPainted(false);
		validBack.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				card.show(frame.getContentPane(), "selectPane");
			}
		});
		validPane.add(validBack);

		validDesc = new JLabel("Verifiy, validate or edit the keyword data as necessary.");
		validDesc.setFont(new Font("Cambria", Font.PLAIN, 15));
		validDesc.setBounds(34, 34, 578, 24);
		validPane.add(validDesc);

		validTech = new JLabel("Techyv Keyword Data:");
		validTech.setFont(new Font("Cambria", Font.BOLD, 13));
		validTech.setForeground(new Color(86, 86, 86));
		validTech.setBounds(34, 61, 171, 24);
		validPane.add(validTech);

		JPanel scrollTechPanel = new JPanel();
		scrollTechPanel.setBackground(Color.WHITE);
		scrollTechPanel.setBounds(34, 85, 450, 158);
		scrollTechPanel.setLayout(new BorderLayout());
		validPane.add(scrollTechPanel);

		validTechText = new JTextArea();
		validTechText.setBackground(Color.WHITE);
		validTechText.setSize(450, 158);
		Border borderTech = BorderFactory.createLineBorder(Color.BLACK);
		validTechText
				.setBorder(BorderFactory.createCompoundBorder(borderTech, BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		scrollTechPanel.add(validTechText);

		JScrollPane scrollTech = new JScrollPane(validTechText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollTechPanel.add(scrollTech);

		editTech = new FreeButton("Edit Source");
		editTech.setForeground(Color.WHITE);
		editTech.setFont(new Font("Tahoma", Font.BOLD, 11));
		editTech.setBounds(490, 85, 100, 30);
		editTech.setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
		editTech.setBorderPainted(false);
		editTech.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(System.getProperty("user.home") + "\\Desktop"));
				chooser.setDialogTitle("Select the keyword list");
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setFileFilter(new FileFilter()
				{
					@Override
					public boolean accept(File file)
					{
						if (file.isDirectory())
						{
							return true;
						}
						else if (file.getName().lastIndexOf('.') != -1)
						{
							String ext = file.getName().substring(file.getName().lastIndexOf('.'));
							return ext.equals(".xls") || ext.equals(".xlsx");
						}
						return false;
					}

					@Override
					public String getDescription()
					{
						return "Excel File: .xls, .xlsx";
					}
				});
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				{
					if (validateInput(chooser.getSelectedFile().getPath()))
					{
						fillTechData(chooser.getSelectedFile().getPath());
					}
					else
					{
						JOptionPane.showMessageDialog(validPane, "Improper file format! Source edit cancelled.",
								"Wrong format", JOptionPane.ERROR_MESSAGE);
					}

				}
			}
		});
		validPane.add(editTech);

		validNettv = new JLabel("Nettv4u Keyword Data:");
		validNettv.setFont(new Font("Cambria", Font.BOLD, 13));
		validNettv.setForeground(new Color(86, 86, 86));
		validNettv.setBounds(34, 250, 171, 24);
		validPane.add(validNettv);

		JPanel scrollNettvPanel = new JPanel();
		scrollNettvPanel.setBackground(Color.WHITE);
		scrollNettvPanel.setBounds(34, 274, 450, 158);
		scrollNettvPanel.setLayout(new BorderLayout());
		validPane.add(scrollNettvPanel);

		validNettvText = new JTextArea();
		validNettvText.setBackground(Color.WHITE);
		validNettvText.setSize(450, 158);
		Border borderNettv = BorderFactory.createLineBorder(Color.BLACK);
		validNettvText.setBorder(
				BorderFactory.createCompoundBorder(borderNettv, BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		scrollNettvPanel.add(validNettvText);

		JScrollPane scrollNettv = new JScrollPane(validNettvText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollNettvPanel.add(scrollNettv);

		editNettv = new FreeButton("Edit Source");
		editNettv.setForeground(Color.WHITE);
		editNettv.setFont(new Font("Tahoma", Font.BOLD, 11));
		editNettv.setBounds(490, 274, 100, 30);
		editNettv.setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
		editNettv.setBorderPainted(false);
		editNettv.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(System.getProperty("user.home") + "\\Downloads"));
				chooser.setDialogTitle("Select the keyword list");
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setFileFilter(new FileFilter()
				{
					@Override
					public boolean accept(File file)
					{
						if (file.isDirectory())
						{
							return true;
						}
						else if (file.getName().lastIndexOf('.') != -1)
						{
							String ext = file.getName().substring(file.getName().lastIndexOf('.'));
							return ext.equals(".xls") || ext.equals(".xlsx");
						}
						return false;
					}

					@Override
					public String getDescription()
					{
						return "Excel File: .xls, .xlsx";
					}
				});
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				{
					if (validateInput(chooser.getSelectedFile().getPath()))
					{
						fillNettvData(chooser.getSelectedFile().getPath());
					}
					else
					{
						JOptionPane.showMessageDialog(validPane, "Improper file format! Source edit cancelled.",
								"Wrong format", JOptionPane.ERROR_MESSAGE);
					}

				}
			}
		});
		validPane.add(editNettv);

		validNext = new FreeButton("Next");
		validNext.setForeground(Color.WHITE);
		validNext.setFont(new Font("Calibri", Font.PLAIN, 19));
		validNext.setBounds(605, 443, 100, 41);
		validNext.setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
		validNext.setBorderPainted(false);
		validNext.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				card.show(frame.getContentPane(), "readyPane");
			}
		});
		validPane.add(validNext);

		readyLbl = new JLabel("Day 1 Part 1");
		readyLbl.setForeground(new Color(85, 127, 164));
		readyLbl.setFont(new Font("Cambria", Font.PLAIN, 49));
		readyLbl.setBounds(230, 149, 257, 51);
		readyPane.add(readyLbl);

		Date today = new Date();
		SimpleDateFormat format = new SimpleDateFormat(" d MMMM YYYY");

		readyTime = new JLabel(format.format(today));
		readyTime.setForeground(new Color(86, 86, 86));
		readyTime.setFont(new Font("Cambria", Font.PLAIN, 35));
		readyTime.setBounds(199, 87, 314, 51);
		readyPane.add(readyTime);

		freelanceLogo = new JLabel("");
		freelanceLogo.setIcon(new ImageIcon(WebScraper.class.getResource("/Freelancer_logo.png")));
		freelanceLogo.setBounds(155, 222, 400, 97);
		readyPane.add(freelanceLogo);

		startProj = new FreeButton("Start Project");
		startProj.setForeground(Color.WHITE);
		startProj.setFont(new Font("Calibri", Font.PLAIN, 29));
		startProj.setBounds(220, 343, 271, 51);
		startProj.setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
		startProj.setBorderPainted(false);
		startProj.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String folder = new SimpleDateFormat("dd-MM-YYYY").format(new Date());
				File[] fol = new File[] { new File(System.getProperty("user.home") + "\\Documents"),
						new File(System.getProperty("user.home") + "\\Documents\\WebScraper"),
						new File(System.getProperty("user.home") + "\\Documents\\WebScraper\\" + folder) };

				for (File file : fol)
					if (!file.exists())
						file.mkdir();

				PrintWriter outputStream = null;
				try
				{
					outputStream = new PrintWriter(
							System.getProperty("user.home") + "\\Documents\\WebScraper\\current.txt");
				}
				catch (Exception se)
				{
					se.printStackTrace();
				}

				outputStream.println(folder);
				outputStream.println("1,1");

				outputStream.close();

				String[] name = { "techyv", "nettv4u" };
				String[][] data = { validTechText.getText().split("\\r\\n|\\n|\\r"),
						validNettvText.getText().split("\\r\\n|\\n|\\r") };

				for (int i = 0; i < 2; i++)
				{
					try
					{
						outputStream = new PrintWriter(System.getProperty("user.home") + "\\Documents\\WebScraper\\"
								+ folder + "\\" + name[i] + ".txt");
					}
					catch (Exception se)
					{
						se.printStackTrace();
					}

					for (String line : data[i])
						outputStream.println(line);

					outputStream.close();

					String filename = System.getProperty("user.home") + "\\Documents\\WebScraper\\" + folder + "\\"
							+ name[i] + "Result.xlsx";
					XSSFWorkbook workbook = new XSSFWorkbook();
					XSSFSheet sheet = workbook.createSheet("Sheet0");

					XSSFRow rowhead = sheet.createRow(0);
					rowhead.createCell(0).setCellValue("Day");
					rowhead.createCell(1).setCellValue("Part");
					rowhead.createCell(2).setCellValue("Keyword");
					rowhead.createCell(3)
							.setCellValue(name[i].substring(0, 1).toUpperCase() + name[i].substring(1) + " URL");
					rowhead.createCell(4).setCellValue("Banner Domain Name");
					rowhead.createCell(5).setCellValue("Banner URL");

					FileOutputStream fileOut = null;
					try
					{
						fileOut = new FileOutputStream(filename);
						workbook.write(fileOut);
						fileOut.close();
						workbook.close();
					}
					catch (Exception se)
					{
						se.printStackTrace();
					}
				}

				startWork("readyPane");
			}
		});
		readyPane.add(startProj);

		readyBack = new FreeButton("Back");
		readyBack.setForeground(new Color(85, 127, 164));
		readyBack.setFont(new Font("Calibri", Font.PLAIN, 19));
		readyBack.setBounds(305, 400, 100, 20);
		readyBack.setBackgroundColor(Color.WHITE, Color.WHITE);
		readyBack.setBorderPainted(false);
		readyBack.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				card.show(frame.getContentPane(), "validPane");
			}
		});
		readyPane.add(readyBack);
		
		card.show(frame.getContentPane(), "selectPane");
	}

	public void startWork(String previous)
	{
		Scanner inputStream = null;

		try
		{
			inputStream = new Scanner(
					new File(System.getProperty("user.home") + "\\Documents\\WebScraper\\current.txt"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		folder = inputStream.nextLine();
		String[] temp = inputStream.nextLine().split(",");
		id = new int[] { Integer.parseInt(temp[0]), Integer.parseInt(temp[1]) };

		if (inputStream.hasNextLine())
		{
			final LocalDateTime lastTime = LocalDateTime.parse(inputStream.nextLine());
			LocalDateTime currentTime = LocalDateTime.now();

			long hours = ChronoUnit.HOURS.between(lastTime, currentTime);

			if (hours < 12)
			{
				inputStream.close();

				Thread waiter = new Thread()
				{
					public void run()
					{
						workWait(lastTime);
					}
				};
				waiter.start();

				return;
			}
		}

		inputStream.close();

		techInput = null;
		nettvInput = null;

		try
		{
			techInput = new Scanner(
					new File(System.getProperty("user.home") + "\\Documents\\WebScraper\\" + folder + "\\techyv.txt"));
			nettvInput = new Scanner(
					new File(System.getProperty("user.home") + "\\Documents\\WebScraper\\" + folder + "\\nettv4u.txt"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		workPane = new JDesktopPane[4];
		keyword = new JTextField[4];
		url = new JTextField[4];
		loader = new JLabel[4];
		banner = new JTextField[4];
		retrieve = new FreeButton[4];
		workBack = new FreeButton[4];
		workNext = new FreeButton[4];

		counter = new int[2];
		counter[0] += 2;
		counter[1] += 2;

		for (int i = 0; i < workPane.length; i++)
		{
			workPane[i] = new JDesktopPane();
			workPane[i].setBackground(new Color(250, 250, 250));
			workPane[i].setName("workPane" + i);
			frame.getContentPane().add(workPane[i], "workPane" + i);

			keyword[i] = new JTextField(i < 2 ? techInput.nextLine() : nettvInput.nextLine());
			keyword[i].setFont(new Font("Cambria", Font.PLAIN, 18));
			keyword[i].setBackground(Color.WHITE);
			keyword[i].setBounds(20, 90, 550, 34);
			keyword[i].setEditable(false);
			workPane[i].add(keyword[i]);

			url[i] = new JTextField("");
			url[i].setFont(new Font("Cambria", Font.PLAIN, 18));
			url[i].setBackground(Color.WHITE);
			url[i].setBounds(20, 170, 550, 34);
			url[i].setEditable(false);
			workPane[i].add(url[i]);

			loader[i] = new JLabel("Loading");
			loader[i].setBounds(590, 170, 32, 32);
			loader[i].setIcon(new ImageIcon(WebScraper.class.getResource("/loading.gif")));
			workPane[i].add(loader[i]);

			banner[i] = new JTextField("");
			banner[i].setFont(new Font("Cambria", Font.PLAIN, 18));
			banner[i].setBackground(Color.WHITE);
			banner[i].setBounds(20, 250, 550, 34);
			banner[i].setEditable(false);
			workPane[i].add(banner[i]);

			retrieve[i] = new FreeButton("Retrieve");
			retrieve[i].setForeground(Color.WHITE);
			retrieve[i].setFont(new Font("Cambria", Font.PLAIN, 15));
			retrieve[i].setBounds(580, 250, 100, 34);
			retrieve[i].setBackgroundColor(Color.DARK_GRAY, Color.DARK_GRAY);
			retrieve[i].setBorderPainted(false);
			final int ii = i;
			retrieve[i].setEnabled(false);
			retrieve[i].addActionListener(new ActionListener()
			{

				public void actionPerformed(ActionEvent evt)
				{
					Thread getAd = new Thread()
					{
						@Override
						public void run()
						{
							boolean ans = true;

							if (banner[ii].getText().length() > 0)
							{
								ans = getConfirmation();
							}

							if (ans)
							{
								disablePane(ii);
								if (launched)
									FreeBrowser.update(ii);
								else
								{
									launched = true;
									FreeBrowser.run(ii);
								}

							}
						}
					};
					getAd.start();
				}
			});
			workPane[i].add(retrieve[i]);

			workNext[i] = new FreeButton(i == 3 ? "Review" : "Next");
			workNext[i].setForeground(Color.WHITE);
			workNext[i].setFont(new Font("Calibri", Font.PLAIN, 19));
			workNext[i].setBounds(605, 443, 100, 41);
			workNext[i].setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
			workNext[i].setBorderPainted(false);
			workNext[i].addActionListener(new ActionListener()
			{

				public void actionPerformed(ActionEvent e)
				{
					if (ii == 3)
					{
						try
						{
							for (int d = 0; d < 2; d++)
							{
								XWPFDocument document = new XWPFDocument();

								FileOutputStream out = new FileOutputStream(new File(System.getProperty("user.home")
										+ "\\Documents\\WebScraper\\" + (d == 0 ? "Techyv" : "Nettv4u") + "_Day_"
										+ id[0] + "_Part_" + id[1] + ".docx"));

								XWPFParagraph paragraph = document.createParagraph();

								paragraph.setAlignment(ParagraphAlignment.LEFT);
								XWPFRun run = paragraph.createRun();
								run.setText("Day No. : Day " + id[0] + " Part " + id[1]);

								for (int p = 0; p < 2; p++)
								{
									int index = (d == 0 ? p : p + 2);

									paragraph = document.createParagraph();
									paragraph.setAlignment(ParagraphAlignment.LEFT);
									run = paragraph.createRun();
									run.setText("Keyword: " + keyword[index].getText());
									run.addBreak();
									run.setText((d == 0 ? "Techyv" : "Nettv4u") + "URL: " + url[index].getText());
									run.addBreak();
									run.setText("Banner URL: " + banner[index].getText());
									run.addBreak();
									run.setText("Image screenshot: ");

									String imgFile = System.getProperty("user.home") + "\\Documents\\WebScraper\\ss"
											+ index + ".png";
									FileInputStream pic = new FileInputStream(imgFile);
									run.addBreak();
									run.addPicture(pic, XWPFDocument.PICTURE_TYPE_JPEG, imgFile, Units.toEMU(412),
											Units.toEMU(212));
									pic.close();
								}

								document.write(out);
								out.close();
							}
						}
						catch (FileNotFoundException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						catch (IOException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						catch (Exception e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						card.show(frame.getContentPane(), "reviewPane");
					}
					else
					{
						for (int k = 0; k < 4; k++)
						{
							indicator[ii].setVisible(false);
							indicator[ii + 1].setVisible(true);
						}
						moveComponents(workPane[ii + 1]);
						card.show(frame.getContentPane(), "workPane" + (ii + 1));
					}
				}
			});
			if (i == 3)
			{
				workNext[i].setEnabled(false);
				workNext[i].setBackgroundColor(Color.GRAY, Color.DARK_GRAY);
			}
			workPane[i].add(workNext[i]);

			workBack[i] = new FreeButton("Back");
			workBack[i].setForeground(Color.WHITE);
			workBack[i].setFont(new Font("Calibri", Font.PLAIN, 19));
			workBack[i].setBounds(495, 443, 100, 41);
			workBack[i].setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
			workBack[i].setBorderPainted(false);
			final String prev = previous;
			workBack[i].addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (ii == 0)
					{
						if (getConfirmation())
							card.show(frame.getContentPane(), prev);
					}
					else
					{
						for (int k = 0; k < 4; k++)
						{
							indicator[ii].setVisible(false);
							indicator[ii - 1].setVisible(true);
						}
						moveComponents(workPane[ii - 1]);
						card.show(frame.getContentPane(), "workPane" + (ii - 1));
					}
				}
			});
			workPane[i].add(workBack[i]);
		}

		freelogo = new JLabel("");
		freelogo.setBounds(10, 10, 51, 51);
		freelogo.setIcon(new ImageIcon(WebScraper.class.getResource("/freelogo.png")));
		workPane[0].add(freelogo);

		dayPart = new JLabel("Day " + id[0] + " Part " + id[1]);
		dayPart.setForeground(new Color(85, 127, 164));
		dayPart.setFont(new Font("Cambria", Font.PLAIN, 32));
		dayPart.setBounds(61, 10, 257, 41);
		workPane[0].add(dayPart);

		kwLbl = new JLabel("Keyword :");
		kwLbl.setFont(new Font("Cambria", Font.PLAIN, 18));
		kwLbl.setBounds(20, 55, 87, 34);
		workPane[0].add(kwLbl);

		// Careful
		urlLbl = new JLabel("Techyv URL :");
		urlLbl.setFont(new Font("Cambria", Font.PLAIN, 18));
		urlLbl.setBounds(20, 135, 117, 34);
		workPane[0].add(urlLbl);

		bannerLbl = new JLabel("Banner URL :");
		bannerLbl.setFont(new Font("Cambria", Font.PLAIN, 18));
		bannerLbl.setBounds(20, 215, 107, 34);
		workPane[0].add(bannerLbl);

		done = new FreeButton[4];
		indicator = new FreeButton[4];

		for (int j = 0; j < 4; j++)
		{
			done[j] = new FreeButton();
			done[j].setForeground(Color.WHITE);
			done[j].setFont(new Font("Calibri", Font.PLAIN, 19));
			done[j].setBounds(25 + (35 * j), 447, 30, 30);
			if (j < 2)
				done[j].setBackgroundColor(new Color(85, 127, 164).darker(), new Color(35, 90, 130).darker());
			else
				done[j].setBackgroundColor(new Color(153, 218, 56).darker(), new Color(120, 181, 25).darker());
			done[j].setBorderPainted(false);
			final int jj = j;
			done[j].addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					int paneNo = Integer.parseInt((getCurrentCard().split("workPane"))[1]);
					if (paneNo != jj)
					{
						indicator[paneNo].setVisible(false);
						indicator[jj].setVisible(true);
						moveComponents(workPane[jj]);
					}

					card.show(frame.getContentPane(), "workPane" + jj);
				}
			});
			workPane[0].add(done[j]);

			indicator[j] = new FreeButton();
			indicator[j].setForeground(Color.WHITE);
			indicator[j].setFont(new Font("Calibri", Font.PLAIN, 19));
			indicator[j].setBounds(25 + (35 * j), 482, 30, 3);
			if (j < 2)
				indicator[j].setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
			else
				indicator[j].setBackgroundColor(new Color(153, 218, 56), new Color(120, 181, 25));
			indicator[j].setBorderPainted(false);
			if (j > 0)
				indicator[j].setVisible(false);
			workPane[0].add(indicator[j]);
		}

		autoNext = true;

		autoSetting = new JCheckBox("Automatically go to the NEXT keyword upon banner url retrieval");
		autoSetting.setBounds(20, 300, 550, 14);
		autoSetting.setBackground(new Color(250, 250, 250));
		autoSetting.setFont(new Font("Calibri", Font.PLAIN, 15));
		autoSetting.addItemListener(new ItemListener()
		{

			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					autoNext = true;
				}
				else if (e.getStateChange() == ItemEvent.DESELECTED)
				{
					autoNext = false;
				}
			}
		});
		autoSetting.setSelected(true);
		workPane[0].add(autoSetting);

		reviewPane = new JDesktopPane();
		reviewPane.setBackground(Color.WHITE);
		reviewPane.setName("reviewPane");
		frame.getContentPane().add(reviewPane, "reviewPane");

		rlogo = new JLabel("");
		rlogo.setBounds(170, 10, 51, 51);
		rlogo.setIcon(new ImageIcon(WebScraper.class.getResource("/freelogo.png")));
		reviewPane.add(rlogo);

		rdayPart = new JLabel("Day " + id[0] + " Part " + id[1] + " - Review");
		rdayPart.setForeground(new Color(85, 127, 164));
		rdayPart.setFont(new Font("Cambria", Font.PLAIN, 32));
		rdayPart.setBounds(231, 10, 300, 41);
		reviewPane.add(rdayPart);

		doc = new FreeButton[2];
		checked = new FreeButton[2];
		veriLbl = new JLabel[2];

		for (int i = 0; i < 2; i++)
		{
			doc[i] = new FreeButton();
			doc[i].setBounds(50, 100 + (175 * i), 125, 125);
			doc[i].setBackgroundColor(Color.WHITE, Color.WHITE);
			doc[i].setIcon(new ImageIcon(WebScraper.class.getResource("/docx.png")));
			doc[i].setBorderPainted(false);
			final int ii = i;
			doc[i].addActionListener(new ActionListener()
			{

				public void actionPerformed(ActionEvent e)
				{
					try
					{
						if (Desktop.isDesktopSupported())
						{
							Desktop.getDesktop()
									.open(new File(System.getProperty("user.home") + "\\Documents\\WebScraper\\"
											+ (ii == 0 ? "Techyv" : "Nettv4u") + "_Day_" + id[0] + "_Part_" + id[1]
											+ ".docx"));
						}
					}
					catch (IOException ioe)
					{
						ioe.printStackTrace();
					}
				}
			});
			reviewPane.add(doc[i]);

			checked[i] = new FreeButton();
			checked[i].setBounds(210, 142 + (175 * i), 30, 30);
			if (i == 0)
				checked[i].setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
			else
				checked[i].setBackgroundColor(new Color(153, 218, 56), new Color(120, 181, 25));
			checked[i].addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (checked[ii].getIcon() == null)
						checked[ii].setIcon(new ImageIcon(WebScraper.class.getResource("/done.png")));
					else
						checked[ii].setIcon(null);

					if (checked[0].getIcon() == null || checked[1].getIcon() == null)
					{
						finishWork.setBackgroundColor(Color.GRAY, Color.GRAY);
						finishWork.setEnabled(false);

						if ((id[0] == 30 && id[1] == 2) || id[0] > 30)
						{
							finishProj.setBackgroundColor(Color.GRAY, Color.GRAY);
							finishProj.setEnabled(false);
						}
					}
					else
					{
						finishWork.setEnabled(true);
						finishWork.setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
						if ((id[0] == 30 && id[1] == 2) || id[0] > 30)
						{
							finishProj.setEnabled(true);
							finishProj.setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
						}
					}
				}
			});
			checked[i].setBorderPainted(false);
			reviewPane.add(checked[i]);

			veriLbl[i] = new JLabel("Verify " + (i == 0 ? "Techyv" : "Nettv4u") + " Results");
			veriLbl[i].setFont(new Font("Calibri", Font.PLAIN, 19));
			veriLbl[i].setBounds(250, 142 + (175 * i), 200, 30);
			veriLbl[i].setForeground(i == 0 ? new Color(35, 90, 130) : new Color(120, 181, 25));
			reviewPane.add(veriLbl[i]);
		}

		finishWork = new FreeButton("Finish Work");
		finishWork.setForeground(Color.WHITE);
		finishWork.setFont(new Font("Calibri", Font.PLAIN, 19));
		finishWork.setBounds(525, (((id[0] == 30 && id[1] == 2) || id[0] > 30) ? 203 : 223), 160, 41);
		finishWork.setBackgroundColor(Color.GRAY, Color.GRAY);
		finishWork.setEnabled(false);
		finishWork.setBorderPainted(false);
		finishWork.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				finishWork();
			}

		});
		reviewPane.add(finishWork);

		if ((id[0] == 30 && id[1] == 2) || id[0] > 30)
		{
			finishProj = new FreeButton("Finish Project");
			finishProj.setForeground(Color.WHITE);
			finishProj.setFont(new Font("Calibri", Font.PLAIN, 19));
			finishProj.setBounds(525, 243, 160, 41);
			finishProj.setBackgroundColor(Color.GRAY, Color.GRAY);
			finishProj.setEnabled(false);
			finishProj.setBorderPainted(false);
			finishProj.addActionListener(new ActionListener()
			{

				public void actionPerformed(ActionEvent e)
				{
					finishWork();
				}

			});
			reviewPane.add(finishProj);
		}

		reviewBack = new FreeButton("Back");
		reviewBack.setForeground(new Color(85, 127, 164));
		reviewBack.setFont(new Font("Calibri", Font.PLAIN, 19));
		reviewBack.setBounds(305, 400, 100, 20);
		reviewBack.setBackgroundColor(Color.WHITE, Color.WHITE);
		reviewBack.setBorderPainted(false);
		reviewBack.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				card.show(frame.getContentPane(), "workPane" + (workPane.length - 1));
			}
		});
		reviewPane.add(reviewBack);

		card.show(frame.getContentPane(), "workPane0");

		Thread linker = new Thread()
		{
			public void run()
			{
				getLinks();
			}
		};
		linker.start();
	}

	public void finishWork()
	{
		for (int i = 0; i < 4; i++)
			new File(System.getProperty("user.home") + "\\Documents\\WebScraper\\ss" + i + ".png").delete();

		new File(System.getProperty("user.home") + "\\Documents\\WebScraper\\" + folder + "\\Day " + id[0] + " Part "
				+ id[1]).mkdir();

		for (int d = 0; d < 2; d++)
		{
			String file = (d == 0 ? "Techyv" : "Nettv4u") + "_Day_" + id[0] + "_Part_" + id[1] + ".docx";
			try
			{
				Files.move(new File(System.getProperty("user.home") + "\\Documents\\WebScraper\\" + file).toPath(),
						new File(System.getProperty("user.home") + "\\Documents\\WebScraper\\" + folder + "\\Day "
								+ id[0] + " Part " + id[1] + "\\" + file).toPath(),
						StandardCopyOption.REPLACE_EXISTING);
			}
			catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		try
		{
			Desktop.getDesktop().open(new File(System.getProperty("user.home") + "\\Documents\\WebScraper\\" + folder
					+ "\\Day " + id[0] + " Part " + id[1]));
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Scanner inputStream = null;

		try
		{
			inputStream = new Scanner(
					new File(System.getProperty("user.home") + "\\Documents\\WebScraper\\current.txt"));
		}
		catch (Exception eee)
		{
			eee.printStackTrace();
		}

		folder = inputStream.nextLine();
		String[] id = inputStream.nextLine().split(",");
		int[] ids = { Integer.parseInt(id[0]), Integer.parseInt(id[1]) };
		if (ids[1] == 2)
		{
			ids[0]++;
			ids[1]--;
		}
		else
			ids[1]++;

		inputStream.close();

		PrintWriter outputStream = null;

		try
		{
			outputStream = new PrintWriter(System.getProperty("user.home") + "\\Documents\\WebScraper\\current.txt");
		}
		catch (Exception eee)
		{
			eee.printStackTrace();
		}

		outputStream.println(folder);
		outputStream.println(ids[0] + "," + ids[1]);
		LocalDateTime currentTime = LocalDateTime.now();
		outputStream.println(currentTime);

		outputStream.close();

		for (int d = 0; d < 2; d++)
		{
			String filePath = System.getProperty("user.home") + "\\Documents\\WebScraper\\" + folder + "\\"
					+ (d == 0 ? "techyv" : "nettv4u") + ".txt";
			try
			{
				Scanner fileScanner = new Scanner(new File(filePath));
				for (int i = 0; i < counter[d]; i++)
					fileScanner.nextLine();

				FileWriter fileStream = new FileWriter(filePath);
				BufferedWriter out = new BufferedWriter(fileStream);
				while (fileScanner.hasNextLine())
				{
					String next = fileScanner.nextLine();
					if (next.equals("\n"))
						out.newLine();
					else
						out.write(next);
					out.newLine();
				}
				fileScanner.close();
				out.close();
			}
			catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		for (int d = 0; d < 2; d++)
		{
			String filePath = System.getProperty("user.home") + "\\Documents\\WebScraper\\" + folder + "\\"
					+ (d == 0 ? "techyv" : "nettv4u") + "Result.xlsx";
			FileInputStream file;
			XSSFWorkbook workbook = null;
			try
			{
				file = new FileInputStream(new File(filePath));
				workbook = new XSSFWorkbook(file);
				file.close();
			}
			catch (Exception fne)
			{
			}

			XSSFSheet sheet = workbook.getSheetAt(0);

			for (int i = 0; i < 2; i++)
			{
				int index = (d == 0 ? i : i + 2);
				XSSFRow row = sheet.createRow(sheet.getPhysicalNumberOfRows());
				row.createCell(0).setCellValue(id[0]);
				row.createCell(1).setCellValue(id[1]);
				row.createCell(2).setCellValue(keyword[index].getText());
				row.createCell(3).setCellValue(url[index].getText());
				String dom = banner[index].getText();
				dom = dom.substring(0, dom.indexOf("/", 8));

				dom = dom.contains("https") ? dom.substring(8) : dom.substring(7);

				String[] parts = dom.split("\\.");

				dom = parts[parts.length - 2] + "." + parts[parts.length - 1];

				row.createCell(4).setCellValue(dom);
				row.createCell(5).setCellValue(banner[index].getText());
			}
			FileOutputStream fileOut = null;
			try
			{
				fileOut = new FileOutputStream(filePath);
				workbook.write(fileOut);
				fileOut.close();
				workbook.close();
			}
			catch (Exception se)
			{
				se.printStackTrace();
			}

		}

		workWait(currentTime);
	}

	public void workWait(LocalDateTime lastTime)
	{
		waitPane = new JDesktopPane();
		waitPane.setBackground(Color.WHITE);
		waitPane.setName("waitPane");
		frame.getContentPane().add(waitPane, "waitPane");

		timeLbl = new JLabel("Time left till next work period....");
		timeLbl.setForeground(new Color(85, 127, 164));
		timeLbl.setFont(new Font("Cambria", Font.PLAIN, 25));
		timeLbl.setBounds(185, 95, 350, 100);
		waitPane.add(timeLbl);

		timeLeft = new JLabel();
		timeLeft.setForeground(new Color(85, 127, 164));
		timeLeft.setFont(new Font("Cambria", Font.PLAIN, 90));
		timeLeft.setBounds(185, 130, 400, 141);
		waitPane.add(timeLeft);

		exitWait = new FreeButton("Exit");
		exitWait.setForeground(Color.WHITE);
		exitWait.setFont(new Font("Calibri", Font.PLAIN, 19));
		exitWait.setBounds(280, 255, 160, 41);
		exitWait.setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
		exitWait.setBorderPainted(false);
		exitWait.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		waitPane.add(exitWait);

		final LocalDateTime nextTime = lastTime.plusHours(12);

		Thread clock = new Thread()
		{
			public void run()
			{
				while (true)
				{
					LocalDateTime currentTime = LocalDateTime.now();
					long hours = ChronoUnit.HOURS.between(currentTime, nextTime);
					long mins = ChronoUnit.MINUTES.between(currentTime, nextTime);
					long secs = ChronoUnit.SECONDS.between(currentTime, nextTime) - (mins * 60);
					mins -= hours * 60;
					if (secs <= 0)
						break;
					timeLeft.setText(hours + ":" + mins + ":" + secs);
					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException ex)
					{
						Thread.currentThread().interrupt();
					}
				}
				startWork("startPane");
			}
		};
		clock.start();
		card.show(frame.getContentPane(), "waitPane");
	}
	
	public void checkDone()
	{
		for (int i = 0; i < 4; i++)
			if (banner[i].getText().length() == 0)
				return;

		workNext[3].setEnabled(true);
		workNext[3].setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
	}

	public boolean validateInput(String path)
	{
		String ext = path.substring(path.lastIndexOf('.'));

		if (ext.equals(".xlsx"))
		{
			try
			{
				FileInputStream file;
				try
				{
					file = new FileInputStream(new File(path));
				}
				catch (FileNotFoundException e)
				{
					return false;
				}
				// Create Workbook instance holding reference to .xlsx file
				XSSFWorkbook workbook = new XSSFWorkbook(file);

				// Get first/desired sheet from the workbook
				XSSFSheet sheet = workbook.getSheetAt(0);

				// Iterate through each rows one by one
				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext())
				{
					Row row = rowIterator.next();
					// For each row, iterate through all the columns
					Iterator<Cell> cellIterator = row.cellIterator();
					int column = 0;
					while (cellIterator.hasNext())
					{
						Cell cell = cellIterator.next();
						// Check the cell type and format accordingly
						switch (cell.getCellType())
						{
							case NUMERIC:
								column++;
								break;
							case STRING:
								column++;
								break;
						}
					}

					if (column > 1)
					{
						file.close();
						return false;
					}
				}
				file.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
			return true;
		}
		else if (ext.equals(".xls"))
		{
			try
			{
				FileInputStream file;
				try
				{
					file = new FileInputStream(new File(path));
				}
				catch (FileNotFoundException e)
				{
					return false;
				}
				// Create Workbook instance holding reference to .xls file
				HSSFWorkbook workbook = new HSSFWorkbook(file);

				// Get first/desired sheet from the workbook
				HSSFSheet sheet = workbook.getSheetAt(0);

				// Iterate through each rows one by one
				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext())
				{
					Row row = rowIterator.next();
					// For each row, iterate through all the columns
					Iterator<Cell> cellIterator = row.cellIterator();
					int column = 0;
					while (cellIterator.hasNext())
					{
						Cell cell = cellIterator.next();
						// Check the cell type and format accordingly
						switch (cell.getCellType())
						{
							case NUMERIC:
								column++;
								break;
							case STRING:
								column++;
								break;
						}
					}

					if (column > 1)
					{
						file.close();
						return false;
					}
				}
				file.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
			return true;
		}

		return false;
	}

	public void validateNext()
	{
		if (techBool && nettvBool)
		{
			selecNext.setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
			selecNext.setEnabled(true);
		}
		else
		{
			selecNext.setBackgroundColor(Color.WHITE.darker(), Color.GRAY);
			selecNext.setEnabled(false);
		}
	}

	public void fillTechData(String path)
	{
		validTechText.setText(getData(path));
	}

	public void fillNettvData(String path)
	{
		validNettvText.setText(getData(path));
	}

	public String getData(String path)
	{
		String ext = path.substring(path.lastIndexOf('.'));
		String output = "";
		if (ext.equals(".xlsx"))
		{
			try
			{
				FileInputStream file = null;
				try
				{
					file = new FileInputStream(new File(path));
				}
				catch (FileNotFoundException e)
				{
				}
				// Create Workbook instance holding reference to .xlsx file
				XSSFWorkbook workbook = new XSSFWorkbook(file);

				// Get first/desired sheet from the workbook
				XSSFSheet sheet = workbook.getSheetAt(0);

				// Iterate through each rows one by one
				Iterator<Row> rowIterator = sheet.iterator();
				rowIterator.next();
				while (rowIterator.hasNext())
				{
					Row row = rowIterator.next();
					// For each row, iterate through all the columns
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext())
					{
						Cell cell = cellIterator.next();
						// Check the cell type and format accordingly
						switch (cell.getCellType())
						{
							case NUMERIC:
								output += cell.getStringCellValue() + "\n";
								break;
							case STRING:
								output += cell.getStringCellValue() + "\n";
								break;
						}
					}
				}
				file.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (ext.equals(".xls"))
		{
			try
			{
				FileInputStream file = null;
				try
				{
					file = new FileInputStream(new File(path));
				}
				catch (FileNotFoundException e)
				{
				}
				// Create Workbook instance holding reference to .xls file
				HSSFWorkbook workbook = new HSSFWorkbook(file);

				// Get first/desired sheet from the workbook
				HSSFSheet sheet = workbook.getSheetAt(0);

				// Iterate through each rows one by one
				Iterator<Row> rowIterator = sheet.iterator();
				rowIterator.next();
				while (rowIterator.hasNext())
				{
					Row row = rowIterator.next();
					// For each row, iterate through all the columns
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext())
					{
						Cell cell = cellIterator.next();
						// Check the cell type and format accordingly
						switch (cell.getCellType())
						{
							case NUMERIC:
								output += cell.getStringCellValue() + "\n";
								break;
							case STRING:
								output += cell.getStringCellValue() + "\n";
								break;
						}
					}
				}
				file.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return output;
	}

	public void getLinks()
	{
		for (int i = 0; i < workPane.length; i++)
		{
			try
			{
				Thread.sleep(new Random().nextInt(2000) + 3000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			final int ii = i;
			Thread search = new Thread()
			{
				@Override
				public void run()
				{
					boolean gotIt = false;
					int pageNo = 0;
					do
					{
						String google = "http://www.google.com/search?client=firefox-b-1-d&q=";
						String searchTxt = keyword[ii].getText();
						String charset = "UTF-8";
						String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:106.0) Gecko/20100101 Firefox/106.0";

						try
						{
							// + "&num=100&start=" + pageNo
							Elements links = Jsoup
									.connect(google + URLEncoder.encode(searchTxt, charset))
									.userAgent(userAgent).timeout(5000).get().select(".g>.r>a");

							for (Element link : links)
							{
								String urlTxt = link.absUrl("href");
								System.out.println(urlTxt);
								urlTxt = URLDecoder.decode(
										urlTxt.substring(urlTxt.indexOf('=') + 1, urlTxt.indexOf('&')), "UTF-8");

								if (urlTxt.contains(ii < 2 ? "techyv.com" : "nettv4u.com"))
								{
									gotIt = true;
									url[ii].setText(urlTxt);
									retrieve[ii].setEnabled(true);
									retrieve[ii].setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
									loader[ii].setIcon(
											new ImageIcon(WebScraper.class.getResource("/correct.png")));
									loader[ii].setBounds(590, 176, 20, 20);
									break;
								}
							}

							if (!gotIt && links.size() > 0)
							{
								pageNo += 100;
							}
							else if (!gotIt && links.size() == 0)
							{
								String newKeyword = ii < 2 ? techInput.nextLine() : nettvInput.nextLine();

								if (ii < 2)
									counter[0]++;
								else
									counter[1]++;

								keyword[ii].setText(newKeyword);
								pageNo = 0;
							}

						}
						catch (Exception e)
						{
							e.printStackTrace();
						}

						if (!gotIt)
						{
							try
							{
								Thread.sleep(new Random().nextInt(2000) + 3000);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
					}
					while (!gotIt);
				}
			};
			search.start();

			try
			{
				Thread.sleep(new Random().nextInt(2000) + 3000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public String getCurrentCard()
	{
		for (Component comp : frame.getContentPane().getComponents())
		{
			if (comp.isVisible())
			{
				return comp.getName();
			}
		}

		return "No Pane Found";
	}

	public void moveComponents(JDesktopPane pane)
	{
		pane.add(freelogo);

		pane.add(dayPart);

		pane.add(kwLbl);

		urlLbl.setText((Integer.parseInt((pane.getName().split("workPane"))[1]) < 2 ? "Techyv" : "Nettv4u") + " URL: ");
		pane.add(urlLbl);

		pane.add(bannerLbl);

		for (int j = 0; j < 4; j++)
		{
			pane.add(done[j]);
			pane.add(indicator[j]);
		}

		pane.add(autoSetting);
	}

	public void disablePane(int i)
	{
		changePane(i, false);
	}

	public void enablePane(int i)
	{
		changePane(i, true);
	}

	public void changePane(int i, boolean bool)
	{
		for (Component component : WebScraper.window.workPane[i].getComponents())
		{
			if (!(component instanceof FreeButton && ((FreeButton) component).getText().equals("Review")))
				component.setEnabled(bool);
		}

		if (bool)
		{
			retrieve[i].setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
			if (!workNext[i].getText().equals("Review"))
				workNext[i].setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
			workBack[i].setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
			for (int j = 0; j < 4; j++)
			{
				if (j < 2)
				{
					done[j].setBackgroundColor(new Color(85, 127, 164).darker(), new Color(35, 90, 130).darker());
					indicator[j].setBackgroundColor(new Color(85, 127, 164), new Color(35, 90, 130));
				}
				else
				{
					done[j].setBackgroundColor(new Color(153, 218, 56).darker(), new Color(120, 181, 25).darker());
					indicator[j].setBackgroundColor(new Color(153, 218, 56), new Color(120, 181, 25));
				}
			}
		}
		else
		{
			retrieve[i].setBackgroundColor(Color.WHITE.darker(), Color.GRAY);
			if (!workNext[i].getText().equals("Review"))
				workNext[i].setBackgroundColor(Color.WHITE.darker(), Color.GRAY);
			workBack[i].setBackgroundColor(Color.WHITE.darker(), Color.GRAY);
			for (int j = 0; j < 4; j++)
			{
				done[j].setBackgroundColor(Color.WHITE.darker(), Color.GRAY);
				indicator[j].setBackgroundColor(Color.WHITE.darker(), Color.GRAY);
			}
		}
		workPane[i].repaint();

	}

	public boolean getConfirmation()
	{
		AWTEventListener keystopper = new AWTEventListener()
		{

			@Override
			public void eventDispatched(AWTEvent event)
			{
				if (event instanceof KeyEvent)
				{
					((KeyEvent) event).consume();
				}
			}
		};
		Toolkit.getDefaultToolkit().addAWTEventListener(keystopper, AWTEvent.KEY_EVENT_MASK);

		int answer = JOptionPane.showOptionDialog(WebScraper.window.frame,
				"Exiting the program may result in a loss of progress. Do you want to continue?", "Warning!",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[] { "Yes", "Cancel" },
				"Cancel");

		Toolkit.getDefaultToolkit().removeAWTEventListener(keystopper);

		if (answer == 0)
			return true;
		else
			return false;
	}
}