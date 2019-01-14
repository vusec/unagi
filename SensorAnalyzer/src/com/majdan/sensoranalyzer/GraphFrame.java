package com.majdan.sensoranalyzer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.majdan.sensordynamics.FixedKeystrokeContainer2;
import com.majdan.sensordynamics.KeyChange;
import com.majdan.sensordynamics.KeyDownChange;
import com.majdan.sensordynamics.KeyUpChange;
import com.majdan.sensordynamics.SensorReading;

public class GraphFrame extends ApplicationFrame {
	

	public static void main(String[] args) throws Exception {
		
		LinkedHashMap<String, FixedKeystrokeContainer2> map = new LinkedHashMap<String, FixedKeystrokeContainer2>();
//		FixedKeystrokeContainer2 tmp1 = deserialize("D:\\Programowanie\\kdroid\\trunk_kamil\\input_data\\a");
//		FixedKeystrokeContainer2 tmp2 = deserialize("D:\\Programowanie\\kdroid\\trunk_kamil\\input_data\\b");
//		
		int x = 1;
		//System.out.println(x);
		
		//map.put("User 1", deserialize("S:\\mgr\\out\\y"));
		
		//String path = "D:\\Programowanie\\kdroid\\trunk_kamil\\input_data\\internet01\\1_katia\\";
		
		String path = "/home/student/workspace/kdroid/results/internet01/1_katia/";
		map.put("User 1", new DataReader().deserialize(path+"2013_06_20__16_28_36__176__7b75adb0-13d7-4ace-9a0a-90e533fd82a1__katia__internet"));
		//map.put("Current file", deserialize(args[0]));
		
		
		//map.put("Kamil 2", deserialize(path+" (2)"));
		//map.put("Kamil 3", deserialize(path+" (3)"));
		
		//String path = "D:\\Programowanie\\kdroid\\trunk_kamil\\input_data\\g";
		//map.put("Giuseppe 1", deserialize(path+" (1)"));
		//map.put("Giuseppe 2", deserialize(path+" (2)"));
		//map.put("Giuseppe 3", deserialize(path+" (3)"));
		
		saveAsPDF(4, "/home/student/workspace/kdroid/results/internet01/1_katia/katia_gyroscopey.pdf", map, 800, 400);
		
//		GraphFrame gf = new GraphFrame(map);
//		gf.pack();
//		
//		RefineryUtilities.centerFrameOnScreen(gf);
//		gf.setVisible(true);
	}

	private static void saveAsPDF(int nr, String path, Map<? extends Object, FixedKeystrokeContainer2> obj, int width, int height) throws Exception {
		Map<String, XYDataset> dss = createDatasets(obj);
		
		int i=0;
		for(Entry<String, XYDataset> e : dss.entrySet()) {
			JFreeChart chart = createChart(e.getValue(), e.getKey());
			
			if(i == nr) {
				saveChartToPDF(chart, path, width, height);
			}
			i++;
		}
	}
	
	public GraphFrame(Map<? extends Object, FixedKeystrokeContainer2> obj) {
		super("Fixed Analysis");
		
		Map<String, XYDataset> dss = createDatasets(obj);
		
		this.getContentPane().setLayout(
                //new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
				new GridLayout((dss.size()+1)/2, 2));
		
		for(Entry<String, XYDataset> e : dss.entrySet()) {
			JFreeChart chart = createChart(e.getValue(), e.getKey());
			ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
			chartPanel.setMouseZoomable(true, false);
			
			getContentPane().add(chartPanel);
		}
		

	}

	private static JFreeChart createChart(XYDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				null, // title
				"Time (minute:second:milisecond)", // x-axis label
				"Value", // y-axis label
				dataset, // data
				true, // create legend?
				true, // generate tooltips?
				true // generate URLs?
				);
		
		chart.setBackgroundPaint(Color.white);
		chart.setBorderPaint(Color.black);
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinePaint(Color.black);
//		plot.setDomainCrosshairPaint(Color.black);
//		plot.setDomainGridlinePaint(Color.black);
//		plot.setDomainMinorGridlinePaint(Color.black);
//		plot.setDomainTickBandPaint(Color.black);
//		plot.setDomainZeroBaselinePaint(Color.black);
//		//plot.setQuadrantPaint(int, Color.black);
//		plot.setRangeCrosshairPaint(Color.black);
//		plot.setRangeGridlinePaint(Color.black);
//		plot.setRangeMinorGridlinePaint(Color.black);
//		plot.setRangeTickBandPaint(Color.black);
//		plot.setRangeZeroBaselinePaint(Color.black);
		
		LegendTitle sl = chart.getLegend();
		sl.setItemPaint(Color.black);
		sl.setFrame(BlockBorder.NONE);
		
		ValueAxis axis1 = plot.getDomainAxis();
		axis1.setTickMarkPaint(Color.black);
		axis1.setAxisLinePaint(Color.black);
		axis1.setLabelPaint(Color.black);
		axis1.setTickLabelPaint(Color.black);
		axis1.setTickMarkPaint(Color.black);
		//axis1.setTickLabelFont(Font.)
		
		ValueAxis axis2 = plot.getRangeAxis();
		axis2.setTickMarkPaint(Color.black);
		axis2.setAxisLinePaint(Color.black);
		axis2.setLabelPaint(Color.black);
		axis2.setTickLabelPaint(Color.black);
		axis2.setTickMarkPaint(Color.black);
		
		
		
		//plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		//plot.setDomainCrosshairVisible(true);
		//plot.setRangeCrosshairVisible(true);
		XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(true);
			renderer.setBaseShapesFilled(true);
		}
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("m:s:S"));
		return chart;
	}
	
	private static Map<String, XYDataset> createDatasets(Map<? extends Object, FixedKeystrokeContainer2> data) {
		LinkedHashMap<String, XYDataset> result = new LinkedHashMap<String, XYDataset>();
		TimeSeriesCollection dataset;
		String sTitle;
		
		sTitle = "Acceleration X";
		dataset = new TimeSeriesCollection();
		for(Entry<? extends Object, FixedKeystrokeContainer2> e : data.entrySet()) {
			Object userName = e.getKey();
			FixedKeystrokeContainer2 fkc = e.getValue();
			
			LinkedList<SensorReading> [] acc = fkc.getKeystroke().getAcc(); //
			LinkedList<KeyUpChange> keyUps = fkc.getKeyUps();
			LinkedList<KeyDownChange> keyDowns = fkc.getKeyDowns();
			
			TimeSeries ts = new TimeSeries(userName.toString(), Millisecond.class);
			TimeSeries tsU = new TimeSeries(userName+" KeyUps", Millisecond.class);
			TimeSeries tsD = new TimeSeries(userName+" KeyDowns", Millisecond.class);
			for(SensorReading sr : acc[0]) { //
				ts.addOrUpdate(new Millisecond(new Date(sr.getTime())), sr.getValue());
			}
			for(KeyChange kc : keyUps) {
				tsU.add(new Millisecond(new Date(kc.getTime())), 0);
			}
			for(KeyChange kc : keyDowns) {
				tsD.add(new Millisecond(new Date(kc.getTime())), 0);
			}
			dataset.addSeries(ts);
			dataset.addSeries(tsD);
			dataset.addSeries(tsU);
		}
		result.put(sTitle, dataset);
		
		sTitle = "Acceleration Y";
		dataset = new TimeSeriesCollection();
		for(Entry<? extends Object, FixedKeystrokeContainer2> e : data.entrySet()) {
			String userName = e.getKey().toString();
			FixedKeystrokeContainer2 fkc = e.getValue();
			
			LinkedList<SensorReading> [] acc = fkc.getKeystroke().getAcc(); //
			LinkedList<KeyUpChange> keyUps = fkc.getKeyUps();
			LinkedList<KeyDownChange> keyDowns = fkc.getKeyDowns();
			
			TimeSeries ts = new TimeSeries(userName, Millisecond.class);
			TimeSeries tsU = new TimeSeries(userName+" KeyUps", Millisecond.class);
			TimeSeries tsD = new TimeSeries(userName+" KeyDowns", Millisecond.class);
			for(SensorReading sr : acc[1]) { //
				ts.addOrUpdate(new Millisecond(new Date(sr.getTime())), sr.getValue());
			}
			for(KeyChange kc : keyUps) {
				tsU.add(new Millisecond(new Date(kc.getTime())), 0);
			}
			for(KeyChange kc : keyDowns) {
				tsD.add(new Millisecond(new Date(kc.getTime())), 0);
			}
			dataset.addSeries(ts);
			dataset.addSeries(tsD);
			dataset.addSeries(tsU);
		}
		result.put(sTitle, dataset);
		
		sTitle = "Acceleration Z";
		dataset = new TimeSeriesCollection();
		for(Entry<? extends Object, FixedKeystrokeContainer2> e : data.entrySet()) {
			String userName = e.getKey().toString();
			FixedKeystrokeContainer2 fkc = e.getValue();
			
			LinkedList<SensorReading> [] acc = fkc.getKeystroke().getAcc(); //
			LinkedList<KeyUpChange> keyUps = fkc.getKeyUps();
			LinkedList<KeyDownChange> keyDowns = fkc.getKeyDowns();
			
			TimeSeries ts = new TimeSeries(userName, Millisecond.class);
			TimeSeries tsU = new TimeSeries(userName+" KeyUps", Millisecond.class);
			TimeSeries tsD = new TimeSeries(userName+" KeyDowns", Millisecond.class);
			for(SensorReading sr : acc[2]) { //
				ts.addOrUpdate(new Millisecond(new Date(sr.getTime())), sr.getValue());
			}
			for(KeyChange kc : keyUps) {
				tsU.add(new Millisecond(new Date(kc.getTime())), 0);
			}
			for(KeyChange kc : keyDowns) {
				tsD.add(new Millisecond(new Date(kc.getTime())), 0);
			}
			dataset.addSeries(ts);
			dataset.addSeries(tsD);
			dataset.addSeries(tsU);
		}
		result.put(sTitle, dataset);
		
		sTitle = "Gyroscope X";
		dataset = new TimeSeriesCollection();
		for(Entry<? extends Object, FixedKeystrokeContainer2> e : data.entrySet()) {
			String userName = e.getKey().toString();
			FixedKeystrokeContainer2 fkc = e.getValue();
			
			LinkedList<SensorReading> [] gy = fkc.getKeystroke().getGy(); //
			LinkedList<KeyUpChange> keyUps = fkc.getKeyUps();
			LinkedList<KeyDownChange> keyDowns = fkc.getKeyDowns();
			
			TimeSeries ts = new TimeSeries(userName, Millisecond.class);
			TimeSeries tsU = new TimeSeries(userName+" KeyUps", Millisecond.class);
			TimeSeries tsD = new TimeSeries(userName+" KeyDowns", Millisecond.class);
			for(SensorReading sr : gy[0]) { //
				ts.addOrUpdate(new Millisecond(new Date(sr.getTime())), sr.getValue());
			}
			for(KeyChange kc : keyUps) {
				tsU.add(new Millisecond(new Date(kc.getTime())), 0);
			}
			for(KeyChange kc : keyDowns) {
				tsD.add(new Millisecond(new Date(kc.getTime())), 0);
			}
			dataset.addSeries(ts);
			dataset.addSeries(tsD);
			dataset.addSeries(tsU);
		}
		result.put(sTitle, dataset);
		
		sTitle = "Gyroscope Y";
		dataset = new TimeSeriesCollection();
		for(Entry<? extends Object, FixedKeystrokeContainer2> e : data.entrySet()) {
			String userName = e.getKey().toString();
			FixedKeystrokeContainer2 fkc = e.getValue();
			
			LinkedList<SensorReading> [] gy = fkc.getKeystroke().getGy(); //
			LinkedList<KeyUpChange> keyUps = fkc.getKeyUps();
			LinkedList<KeyDownChange> keyDowns = fkc.getKeyDowns();
			
			TimeSeries ts = new TimeSeries(userName, Millisecond.class);
			TimeSeries tsU = new TimeSeries(userName+" KeyUps", Millisecond.class);
			TimeSeries tsD = new TimeSeries(userName+" KeyDowns", Millisecond.class);
			for(SensorReading sr : gy[1]) { //
				ts.addOrUpdate(new Millisecond(new Date(sr.getTime())), sr.getValue());
			}
			for(KeyChange kc : keyUps) {
				tsU.add(new Millisecond(new Date(kc.getTime())), 0);
			}
			for(KeyChange kc : keyDowns) {
				tsD.add(new Millisecond(new Date(kc.getTime())), 0);
			}
			dataset.addSeries(ts);
			dataset.addSeries(tsD);
			dataset.addSeries(tsU);
		}
		result.put(sTitle, dataset);
		
		sTitle = "Gyroscope Z";
		dataset = new TimeSeriesCollection();
		for(Entry<? extends Object, FixedKeystrokeContainer2> e : data.entrySet()) {
			String userName = e.getKey().toString();
			FixedKeystrokeContainer2 fkc = e.getValue();
			
			LinkedList<SensorReading> [] gy = fkc.getKeystroke().getGy(); //
			LinkedList<KeyUpChange> keyUps = fkc.getKeyUps();
			LinkedList<KeyDownChange> keyDowns = fkc.getKeyDowns();
			
			TimeSeries ts = new TimeSeries(userName, Millisecond.class);
			TimeSeries tsU = new TimeSeries(userName+" KeyUps", Millisecond.class);
			TimeSeries tsD = new TimeSeries(userName+" KeyDowns", Millisecond.class);
			for(SensorReading sr : gy[2]) { //
				ts.addOrUpdate(new Millisecond(new Date(sr.getTime())), sr.getValue());
			}
			for(KeyChange kc : keyUps) {
				tsU.add(new Millisecond(new Date(kc.getTime())), 0);
			}
			for(KeyChange kc : keyDowns) {
				tsD.add(new Millisecond(new Date(kc.getTime())), 0);
			}
			dataset.addSeries(ts);
			dataset.addSeries(tsD);
			dataset.addSeries(tsU);
		}
		result.put(sTitle, dataset);

		
		dataset.setDomainIsPointsInTime(true);
		
		return result;
		
//		
//		
//		for(Entry<String, FixedKeystrokeContainer2> e : data.entrySet()) {
//			
//			FixedKeystrokeContainer2 fkc = e.getValue();
//			
//			LinkedList<SensorReading> [] acc = fkc.getKeystroke().getAcc();
//			LinkedList<SensorReading> [] gy = fkc.getKeystroke().getGy();
//			LinkedList<KeyUpChange> keyUps = fkc.getKeyUps();
//			LinkedList<KeyDownChange> keyDowns = fkc.getKeyDowns();
//
//			
//			TimeSeries sAccX = new TimeSeries("Acceleration X", Millisecond.class);
//			for(SensorReading sr : acc[0]) {
//				sAccX.add(new Millisecond(new Date(sr.getTime())), sr.getValue());
//			}
//			dataset.addSeries(sAccX);
//			TimeSeries sAccY = new TimeSeries("Acceleration Y", Millisecond.class);
//			for(SensorReading sr : acc[1]) {
//				sAccY.add(new Millisecond(new Date(sr.getTime())), sr.getValue());
//			}
//			dataset.addSeries(sAccY);
//			TimeSeries sAccZ = new TimeSeries("Acceleration Z", Millisecond.class);
//			for(SensorReading sr : acc[2]) {
//				sAccZ.add(new Millisecond(new Date(sr.getTime())), sr.getValue());
//			}
//			dataset.addSeries(sAccZ);
//			
//			TimeSeries sGyX = new TimeSeries("Gyroscope X", Millisecond.class);
//			for(SensorReading sr : acc[0]) {
//				sGyX.add(new Millisecond(new Date(sr.getTime())), sr.getValue());
//			}
//			dataset.addSeries(sGyX);
//			TimeSeries sGyY = new TimeSeries("Gyroscope Y", Millisecond.class);
//			for(SensorReading sr : acc[1]) {
//				sGyY.add(new Millisecond(new Date(sr.getTime())), sr.getValue());
//			}
//			dataset.addSeries(sGyY);
//			TimeSeries sGyZ = new TimeSeries("Gyroscope Z", Millisecond.class);
//			for(SensorReading sr : acc[2]) {
//				sGyZ.add(new Millisecond(new Date(sr.getTime())), sr.getValue());
//			}
//			dataset.addSeries(sGyZ);
//
//			TimeSeries sKeyUps = new TimeSeries("Key", Millisecond.class);
//			for(KeyChange kc : keyUps) {
//				sKeyUps.add(new Millisecond(new Date(kc.getTime())), 0);
//			}
//			dataset.addSeries(sKeyUps);
//			TimeSeries sKeyDowns = new TimeSeries("", Millisecond.class);
//			for(KeyChange kc : keyDowns) {
//				sKeyDowns.add(new Millisecond(new Date(kc.getTime())), 0);
//			}
//			dataset.addSeries(sKeyDowns);
			
	}
	
	/**
	* Save chart as PDF file. Requires iText library.
	* 
	* @param chart JFreeChart to save.
	* @param fileName Name of file to save chart in.
	* @param width Width of chart graphic.
	* @param height Height of chart graphic.
	* @throws Exception if failed.
	* @see <a href="http://www.lowagie.com/iText">iText</a>
	*/
	public static void saveChartToPDF(JFreeChart chart, String fileName, int width, int height) throws Exception {
	    if (chart != null) {
	        BufferedOutputStream out = null;
	        try {
	            out = new BufferedOutputStream(new FileOutputStream(fileName)); 
	                
	            //convert chart to PDF with iText:
	            Rectangle pagesize = new Rectangle(width, height); 
	            com.lowagie.text.Document document = new com.lowagie.text.Document(pagesize, 50, 50, 50, 50); 
	            try { 
	                PdfWriter writer = PdfWriter.getInstance(document, out); 
	                document.addAuthor("JFreeChart"); 
	                document.open(); 
	        
	                PdfContentByte cb = writer.getDirectContent(); 
	                PdfTemplate tp = cb.createTemplate(width, height); 
	                Graphics2D g2 = tp.createGraphics(width, height, new DefaultFontMapper()); 
	        
	                Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height); 
	                chart.draw(g2, r2D, null); 
	                g2.dispose(); 
	                cb.addTemplate(tp, 0, 0); 
	            } finally {
	                document.close(); 
	            }
	        } finally {
	            if (out != null) {
	                out.close();
	            }
	        }
	    }//else: input values not availabel
	}//saveChartToPDF()
}
