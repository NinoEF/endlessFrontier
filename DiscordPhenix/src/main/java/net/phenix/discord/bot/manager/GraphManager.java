package net.phenix.discord.bot.manager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import com.sun.javafx.application.PlatformImpl;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.LabelBuilder;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;
import javafx.stage.WindowEvent;

public class GraphManager extends Application {

	final static String austria = "Austria";
	final static String brazil = "Brazil";
	final static String france = "France";
	final static String italy = "Italy";
	final static String usa = "USA";

	public static BufferedImage getGraph() {

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart<String, Number> bc = new BarChart<String, Number>(xAxis, yAxis);

		bc.setTitle("Country Summary");
		xAxis.setLabel("Country");
		yAxis.setLabel("Value");

		XYChart.Series series1 = new XYChart.Series();
		series1.setName("2003");
		series1.getData().add(new XYChart.Data(austria, 25601.34));
		series1.getData().add(new XYChart.Data(brazil, 20148.82));
		series1.getData().add(new XYChart.Data(france, 10000));
		series1.getData().add(new XYChart.Data(italy, 35407.15));
		series1.getData().add(new XYChart.Data(usa, 12000));

		XYChart.Series series2 = new XYChart.Series();
		series2.setName("2004");
		series2.getData().add(new XYChart.Data(austria, 57401.85));
		series2.getData().add(new XYChart.Data(brazil, 41941.19));
		series2.getData().add(new XYChart.Data(france, 45263.37));
		series2.getData().add(new XYChart.Data(italy, 117320.16));
		series2.getData().add(new XYChart.Data(usa, 14845.27));

		XYChart.Series series3 = new XYChart.Series();
		series3.setName("2005");
		series3.getData().add(new XYChart.Data(austria, 45000.65));
		series3.getData().add(new XYChart.Data(brazil, 44835.76));
		series3.getData().add(new XYChart.Data(france, 18722.18));
		series3.getData().add(new XYChart.Data(italy, 17557.31));
		series3.getData().add(new XYChart.Data(usa, 92633.68));

		bc.getData().addAll(series1, series2, series3);
		WritableImage wi = new WritableImage(1000, 500);
		WritableImage snapshot = bc.snapshot(new SnapshotParameters(), wi);

		return SwingFXUtils.fromFXImage(snapshot, null);
	}

	public static void main(String[] args) {
		launch(args);
		// PlatformImpl.startup(() -> {
		// File output = new File("v:\\snapshot" + new Date().getTime() +
		// ".png");
		// try {
		// ImageIO.write(getGraph(), "png", output);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// });

	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Bar Chart Sample");
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart<String, Number> bc = new BarChart<String, Number>(xAxis, yAxis);
		bc.setTitle("Country Summary");
		xAxis.setLabel("Country");
		yAxis.setLabel("Value");

		XYChart.Series series1 = new XYChart.Series();
		series1.setName("2003");
		series1.getData().add(new XYChart.Data(austria, 25601.34));
		series1.getData().add(new XYChart.Data(brazil, 20148.82));
		series1.getData().add(new XYChart.Data(france, 10000));
		series1.getData().add(new XYChart.Data(italy, 35407.15));
		series1.getData().add(new XYChart.Data(usa, 12000));

		XYChart.Series series2 = new XYChart.Series();
		series2.setName("2004");
		series2.getData().add(new XYChart.Data(austria, 57401.85));
		series2.getData().add(new XYChart.Data(brazil, 41941.19));
		series2.getData().add(new XYChart.Data(france, 45263.37));
		series2.getData().add(new XYChart.Data(italy, 117320.16));
		series2.getData().add(new XYChart.Data(usa, 14845.27));

		XYChart.Series series3 = new XYChart.Series();
		series3.setName("2005");
		series3.getData().add(new XYChart.Data(austria, 45000.65));
		series3.getData().add(new XYChart.Data(brazil, 44835.76));
		series3.getData().add(new XYChart.Data(france, 18722.18));
		series3.getData().add(new XYChart.Data(italy, 17557.31));
		series3.getData().add(new XYChart.Data(usa, 92633.68));

		Scene scene = new Scene(bc, 800, 600);
		bc.getData().addAll(series1, series2, series3);
		WritableImage wi = new WritableImage(1000, 500);
		WritableImage snapshot = bc.snapshot(new SnapshotParameters(), wi);
		File output = new File("v:\\snapshot" + new Date().getTime() + ".png");
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stage.setScene(scene);
		stage.show();

		
	}
}
