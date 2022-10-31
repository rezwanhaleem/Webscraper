package com.webscraper;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class FreeBrowser extends Application
{
	private WebEngine myWebEngine;
	private static int i;
	private static boolean found;
	private static String adlink;
	private static Stage adStage;

	public void start(Stage stage)
	{
		adStage = stage;

		stage.setTitle("Retrieve Banner URL");

		stage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			public void handle(WindowEvent event)
			{
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText(null);
				alert.setTitle("Warning!");
				alert.setContentText(
						"Closing this window can lead to a loss of current banner progress. Do you want to continue?");

				if (setDefaultButton(alert, ButtonType.CANCEL).showAndWait().orElse(ButtonType.OK) != ButtonType.OK)
					event.consume();
				else
					WebScraper.window.enablePane(i);

			}
		});

		Platform.setImplicitExit(false);

		final TextField addressBar = new TextField(WebScraper.window.url[i].getText());
		addressBar.setOnAction(new EventHandler<javafx.event.ActionEvent>()
		{
			public void handle(javafx.event.ActionEvent event)
			{
				myWebEngine.load(addressBar.getText());
			}
		});
		addressBar.setEditable(false);

		// Trusting all SSL certificates
		// try
		// {
		// TrustManager trm = new X509TrustManager()
		// {
		// public X509Certificate[] getAcceptedIssuers()
		// {
		// return null;
		// }
		//
		// public void checkClientTrusted(X509Certificate[] certs, String
		// authType)
		// {
		// }
		//
		// public void checkServerTrusted(X509Certificate[] certs, String
		// authType)
		// {
		// }
		// };
		//
		// SSLContext sc = SSLContext.getInstance("SSL");
		// sc.init(null, new TrustManager[] { trm }, null);
		// HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		// }
		// catch (KeyManagementException e1)
		// {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// catch (NoSuchAlgorithmException e1)
		// {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		WebView myBrowser = new WebView();
		myWebEngine = myBrowser.getEngine();
		myWebEngine.getLoadWorker().exceptionProperty().addListener(new ChangeListener<Throwable>()
		{
			@Override
			public void changed(ObservableValue<? extends Throwable> observableValue, Throwable oldException,
					Throwable exception)
			{
				System.out.println("WebView encountered an exception loading a page: " + exception);
				exception.printStackTrace();
			}
		});
		myBrowser.setPrefSize(1600, 900);

		final Button goButton = getReloadButton(myBrowser.getEngine());
		final Button backButton = getBackButton(myBrowser.getEngine());
		final Button newKeyButton = getKeyButton(myBrowser.getEngine());
		final Button retrieveButton = getRetrieveButton(myBrowser.getEngine());

		found = false;

		goButton.setGraphic(new ImageView(new Image(WebScraper.class.getResource("/resources/reload.png").toString())));
		backButton.setGraphic(new ImageView(new Image(WebScraper.class.getResource("/resources/back.png").toString())));
		retrieveButton.setDisable(true);

		HBox address = new HBox();
		address.getChildren().setAll(backButton, addressBar, goButton);

		VBox view = new VBox();
		view.getChildren().add(myBrowser);

		final Label lbl = new Label();
		lbl.setFont(new Font("Cambria", 20));
		lbl.setTextFill(Color.RED);

		HBox command = new HBox();
		command.getChildren().setAll(lbl, newKeyButton, retrieveButton);

		BorderPane root = new BorderPane();
		root.setTop(address);
		root.setCenter(view);
		root.setBottom(command);
		stage.setScene(new Scene(root));
		stage.show();

		addressBar.setPrefWidth(stage.getWidth() * 0.951);

		lbl.setPrefWidth(stage.getWidth() * 0.9135);

		myWebEngine.load(addressBar.getText());

		addressBar.textProperty().bind(myWebEngine.locationProperty());

		myWebEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) ->
		{

			if (Worker.State.SCHEDULED.equals(newValue))
			{
				String link = addressBar.getText();

				link = link.substring(0, link.indexOf("/", 8));

				link = link.contains("https") ? link.substring(8) : link.substring(7);

				if (link.contains("google") || link.contains("ad"))
				{
					File captureFile = new File(
							System.getProperty("user.home") + "\\Documents\\WebScraper\\ss" + FreeBrowser.i + ".png");
					ImageView imageView = new ImageView();
//					WritableImage image = myBrowser.snapshot(null, null);
					Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
					BufferedImage bufferedImage;
//					BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
					try
					{
						bufferedImage = new Robot().createScreenCapture(screenRect);
						ImageIO.write(bufferedImage, "png", captureFile);
						imageView.setImage(new Image(captureFile.toURI().toURL().toExternalForm()));
						System.out.println("Captured WebView to: " + captureFile.getAbsoluteFile());
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					found = true;
				}
			}

			if (Worker.State.SUCCEEDED.equals(newValue))
			{
				String link = addressBar.getText();

				link = link.substring(0, link.indexOf("/", 8));

				link = link.contains("https") ? link.substring(8) : link.substring(7);

				String[] parts = link.split("\\.");

				link = parts[parts.length - 2] + "." + parts[parts.length - 1];

				boolean conflict = false;

				try
				{
					FileInputStream file = null;
					try
					{
						String path = System.getProperty("user.home") + "\\Documents\\WebScraper\\"
								+ WebScraper.window.folder + "\\" + (i < 2 ? "techyv" : "nettv4u") + "Result.xlsx";
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

						Cell cell = row.getCell(4);

						if (cell.getStringCellValue().equals(link))
						{
							conflict = true;
							break;
						}

					}
					file.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				if (conflict)
				{
					found = false;
					lbl.setText("That banner is already recorded!");
					retrieveButton.setDisable(true);
					myWebEngine.executeScript("history.back()");
				}
				else
				{
					if (found)
					{
						adlink = addressBar.getText();
						retrieveButton.setDisable(false);
						lbl.setText("");
					}
				}
			}
		});
	}

	private Button getBackButton(final WebEngine eng)
	{
		return getButton(eng, 0);
	}

	private Button getReloadButton(final WebEngine eng)
	{
		return getButton(eng, 1);
	}

	private Button getKeyButton(final WebEngine eng)
	{
		return getButton(eng, 2);
	}

	private Button getRetrieveButton(final WebEngine eng)
	{
		return getButton(eng, 3);
	}

	private Button getButton(final WebEngine eng, int id)
	{
		String btntxt = "";

		if (id == 2)
			btntxt += "New Key";
		else if (id == 3)
			btntxt += "Retrieve";

		Button go = new Button(btntxt);
		go.setDefaultButton(true);
		go.setOnAction(new EventHandler<javafx.event.ActionEvent>()
		{
			@Override
			public void handle(javafx.event.ActionEvent event)
			{
				if (id == 0)
					eng.executeScript("history.back()");
				else if (id == 1)
					eng.reload();
				else if (id == 2)
				{
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setHeaderText(null);
					alert.setTitle("Warning!");
					alert.setContentText(
							"Changing your key can lead to loss of current keyword progress. Do you want to continue?");

					if (setDefaultButton(alert, ButtonType.CANCEL).showAndWait().orElse(ButtonType.OK) == ButtonType.OK)
					{
						WebScraper.window.retrieve[i].setBackgroundColor(java.awt.Color.DARK_GRAY,
								java.awt.Color.DARK_GRAY);
						WebScraper.window.retrieve[i].setEnabled(false);
						boolean gotIt = false;
						int pageNo = 0;
						do
						{
							String newKeyword = i < 2 ? WebScraper.window.techInput.nextLine()
									: WebScraper.window.nettvInput.nextLine();

							if (i < 2)
								WebScraper.window.counter[0]++;
							else
								WebScraper.window.counter[1]++;

							WebScraper.window.keyword[i].setText(newKeyword);

							String google = "http://www.google.com/search?q=";
							String charset = "UTF-8";
							String userAgent = "ExampleBot 1.0 (+http://example.com/bot)";

							try
							{
								Elements links = Jsoup.connect(
										google + URLEncoder.encode(newKeyword, charset) + "&num=100&start=" + pageNo)
										.userAgent(userAgent).get().select(".g>.r>a");

								for (Element link : links)
								{
									String urlTxt = link.absUrl("href");
									urlTxt = URLDecoder.decode(
											urlTxt.substring(urlTxt.indexOf('=') + 1, urlTxt.indexOf('&')), "UTF-8");

									if (urlTxt.contains(i < 2 ? "techyv.com" : "nettv4u.com"))
									{
										gotIt = true;
										WebScraper.window.url[i].setText(urlTxt);
										WebScraper.window.retrieve[i].setEnabled(true);
										WebScraper.window.retrieve[i].setBackgroundColor(
												new java.awt.Color(85, 127, 164), new java.awt.Color(35, 90, 130));
										break;
									}
								}

								if (!gotIt && links.size() > 0)
								{
									pageNo += 100;
								}
								else if (!gotIt && links.size() == 0)
								{
									String newKey = i < 2 ? WebScraper.window.techInput.nextLine()
											: WebScraper.window.nettvInput.nextLine();

									if (i < 2)
										WebScraper.window.counter[0]++;
									else
										WebScraper.window.counter[1]++;

									WebScraper.window.keyword[i].setText(newKey);
								}
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
						while (!gotIt);

						myWebEngine.load(WebScraper.window.url[i].getText());
					}
					else
					{

					}
				}
				else if (id == 3)
				{
					WebScraper.window.banner[i].setText(adlink);
					adStage.close();

					WebScraper.window.enablePane(i);

					if (i < 2)
						WebScraper.window.done[i].setBackgroundColor(new java.awt.Color(85, 127, 164),
								new java.awt.Color(35, 90, 130));
					else
						WebScraper.window.done[i].setBackgroundColor(new java.awt.Color(153, 218, 56),
								new java.awt.Color(120, 181, 25));

					WebScraper.window.done[i]
							.setIcon(new ImageIcon(WebScraper.class.getResource("/resources/done.png")));

					WebScraper.window.checkDone();

					if (WebScraper.window.autoNext && i != 4)
					{
						WebScraper.window.indicator[i].setVisible(false);
						WebScraper.window.indicator[i + 1].setVisible(true);
						WebScraper.window.moveComponents(WebScraper.window.workPane[i + 1]);
						WebScraper.window.card.show(WebScraper.window.frame.getContentPane(), "workPane" + (i + 1));
					}
				}
			}
		});

		return go;
	}

	private static Alert setDefaultButton(Alert alert, ButtonType defBtn)
	{
		DialogPane pane = alert.getDialogPane();
		for (ButtonType t : alert.getButtonTypes())
			((Button) pane.lookupButton(t)).setDefaultButton(t == defBtn);
		return alert;
	}

	public static void run(int i)
	{
		FreeBrowser.i = i;
		launch("");
	}

	public static void update(int i)
	{
		Platform.runLater(new Runnable()
		{
			public void run()
			{
				FreeBrowser.i = i;
				new FreeBrowser().start(new Stage());
			}
		});
	}

}
