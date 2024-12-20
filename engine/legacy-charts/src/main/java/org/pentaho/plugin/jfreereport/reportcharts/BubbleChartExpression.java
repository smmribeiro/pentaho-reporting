/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.plugin.jfreereport.reportcharts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;

public class BubbleChartExpression extends XYChartExpression {
  private static final long serialVersionUID = 7934486966108227105L;

  private double maxBubbleSize;
  private String bubbleLabelContent; //$NON-NLS-1$
  private String xFormat; //$NON-NLS-1$
  private String yFormat; //$NON-NLS-1$
  private String zFormat; //$NON-NLS-1$

  public BubbleChartExpression() {
    bubbleLabelContent = "{0}: ({1}, {2}, {3})";
    zFormat = "";
    yFormat = "";
    xFormat = "";
    maxBubbleSize = 0;
  }

  protected JFreeChart computeXYChart( final XYDataset dataset ) {

    // Make sure we have a proper dataset
    final XYZDataset xyzDataset;
    final double maxZValue;
    if ( dataset instanceof ExtendedXYZDataset ) {
      final ExtendedXYZDataset exyzDataset = (ExtendedXYZDataset) dataset;
      xyzDataset = exyzDataset;
      maxZValue = exyzDataset.getMaxZValue();
    } else if ( dataset instanceof XYZDataset ) {
      xyzDataset = (XYZDataset) dataset;
      maxZValue = precomputeMaxZ( xyzDataset );
    } else {
      xyzDataset = null;
      maxZValue = 0;
    }

    final JFreeChart rtn = ChartFactory.createBubbleChart
      ( computeTitle(), getDomainTitle(), getRangeTitle(), xyzDataset,
        computePlotOrientation(), isShowLegend(), false, false );

    final BubbleRenderer renderer = new BubbleRenderer();
    renderer.setMaxSize( getMaxBubbleSize() );
    renderer.setMaxZ( maxZValue );
    rtn.getXYPlot().setRenderer( renderer );
    configureLogarithmicAxis( rtn.getXYPlot() );
    return rtn;
  }

  private double precomputeMaxZ( final XYZDataset dataset ) {
    double retval = Double.MIN_VALUE;
    for ( int series = 0; series < dataset.getSeriesCount(); series++ ) {
      final int itemcount = dataset.getItemCount( series );
      for ( int item = 0; item < itemcount; item++ ) {
        final double value = dataset.getZValue( series, item );
        if ( retval < value ) {
          retval = value;
        }
      }
    }
    return retval;
  }

  protected void configureChart( final JFreeChart chart ) {
    super.configureChart( chart );
    /* 
     * We don't handle these in reports yet ...
     renderer.setToolTipGenerator(new StandardXYZToolTipGenerator(getBubbleLabelContent(),
     new DecimalFormat(getXFormat()), 
     new DecimalFormat(getYFormat()), 
     new DecimalFormat(getZFormat())));
     
     renderer.setURLGenerator(new StandardBubbleURLGenerator());
     */
  }

  public double getMaxBubbleSize() {
    return maxBubbleSize;
  }

  public void setMaxBubbleSize( final double maxBubbleSize ) {
    this.maxBubbleSize = maxBubbleSize;
  }

  public String getBubbleLabelContent() {
    return bubbleLabelContent;
  }

  /**
   * @param bubbleLabelContent Definition of the bubble labels' content. e.g. "{0}: ({1}, {2}, {3})" {0} = series name
   *                           {1} = x value {2} = y value {3} = z value
   */
  public void setBubbleLabelContent( final String bubbleLabelContent ) {
    this.bubbleLabelContent = bubbleLabelContent;
  }

  public String getXFormat() {
    return xFormat;
  }

  /**
   * @param format Definition of the bubble labels' x value number format. e.g. "#,##0.0#" or "#,##0.00 EUR" or "##0.00
   *               %"
   */
  public void setXFormat( final String format ) {
    xFormat = format;
  }

  public String getYFormat() {
    return yFormat;
  }

  /**
   * @param format Definition of the bubble labels' y value number format. e.g. "#,##0.0#" or "#,##0.00 EUR" or "##0.00
   *               %"
   */
  public void setYFormat( final String format ) {
    yFormat = format;
  }

  public String getZFormat() {
    return zFormat;
  }

  /**
   * @param format Definition of the bubble labels' z value number format. e.g. "#,##0.0#" or "#,##0.00 EUR" or "##0.00
   *               %"
   */
  public void setZFormat( final String format ) {
    zFormat = format;
  }

  /**
   * @return
   * @deprecated Not used anywhere.
   */
  public double getMaxZValue() {
    return 0;
  }

}
