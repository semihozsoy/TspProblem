import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.ScatterRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.Dataset;

class Chart extends ApplicationFrame {

    private int[][] citiesArray;
    private ArrayList<Integer> tour;

    public Chart(String title, int[][] cities, ArrayList<Integer> tour) throws FileNotFoundException {
        super(title);

        this.citiesArray = cities;
        this.tour = tour;

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                title ,
                "X-Coordinate" ,
                "Y-Coordinate" ,
                createDataset() ,
                PlotOrientation.VERTICAL ,
                true , true , false);

        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        final XYPlot plot = xylineChart.getXYPlot( );


        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(){//to assign red colur to starting city and last line


            @Override
            public Paint getItemPaint(int row, int column) {
                double y = this.getPlot().getDataset().getYValue(row, column);

                if(y == citiesArray[tour.get(0)][1])
                    return Color.red;
                return super.getItemPaint(row, column);
            }


        };

        renderer.setSeriesPaint( 0 , Color.BLUE );//sets graph color
        renderer.setSeriesStroke( 0 , new BasicStroke( 2.0f ) );//sets width of lines

        plot.setRenderer( renderer );
        setContentPane( chartPanel );

    }


    private XYDataset createDataset() throws FileNotFoundException {
        final XYSeries citiesSeries = new XYSeries("Cities",false); //create XYSeries of cities
        for (int i=0; i < citiesArray.length+1; i++ ){
            citiesSeries.add(citiesArray[tour.get(i)][0],citiesArray[tour.get(i)][1]);//pas our cities to XYSeries

        }
        final XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries( citiesSeries ); //create datset from XYSeries
        return dataset;
    }



}