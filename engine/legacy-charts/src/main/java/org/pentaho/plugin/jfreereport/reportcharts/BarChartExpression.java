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
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.pentaho.plugin.jfreereport.reportcharts.backport.FormattedCategoryAxis;
import org.pentaho.plugin.jfreereport.reportcharts.backport.FormattedCategoryAxis3D;

import java.awt.*;

/**
 * This class is for backward compatibility with previous build of the expression. Instead of using this class, use
 * CategoricalChartExpression.
 *
 * @author mbatchel
 */
public class BarChartExpression extends StackedCategoricalChartExpression {
  private static final long serialVersionUID = 7082583397390897215L;
  private Double maxBarWidth;
  private boolean stackedBarRenderPercentages;
  private Double itemMargin;
  private boolean shadowVisible;
  private Color shadowColor;
  private int shadowXOffset;
  private int shadowYOffset;

  public BarChartExpression() {
  }

  public Double getItemMargin() {
    return itemMargin;
  }

  public void setItemMargin( final Double itemMargin ) {
    this.itemMargin = itemMargin;
  }

  public Double getMaxBarWidth() {
    return maxBarWidth;
  }

  public void setMaxBarWidth( final Double value ) {
    maxBarWidth = value;
  }

  public boolean isStackedBarRenderPercentages() {
    return stackedBarRenderPercentages;
  }

  public void setStackedBarRenderPercentages( final boolean value ) {
    stackedBarRenderPercentages = value;
  }

  public JFreeChart computeCategoryChart( final CategoryDataset categoryDataset ) {
    final PlotOrientation orientation = computePlotOrientation();

    final JFreeChart chart;
    if ( isThreeD() ) {
      if ( isStacked() ) {
        chart = ChartFactory.createStackedBarChart3D( computeTitle(),
          getCategoryAxisLabel(), getValueAxisLabel(),
          categoryDataset, orientation, isShowLegend(),
          false, false );
      } else {
        chart = ChartFactory.createBarChart3D( computeTitle(),
          getCategoryAxisLabel(), getValueAxisLabel(), categoryDataset,
          orientation, isShowLegend(), false, false );
      }
      chart.getCategoryPlot().setDomainAxis( new FormattedCategoryAxis3D( getCategoryAxisLabel(),
        getCategoricalAxisMessageFormat(), getRuntime().getResourceBundleFactory().getLocale() ) );
    } else {
      if ( isStacked() ) {
        chart = ChartFactory.createStackedBarChart( computeTitle(),
          getCategoryAxisLabel(), getValueAxisLabel(),
          categoryDataset, orientation, isShowLegend(),
          false, false );
      } else {
        chart = ChartFactory.createBarChart( computeTitle(),
          getCategoryAxisLabel(), getValueAxisLabel(), categoryDataset,
          orientation, isShowLegend(), false, false );
      }
      chart.getCategoryPlot().setDomainAxis( new FormattedCategoryAxis( getCategoryAxisLabel(),
        getCategoricalAxisMessageFormat(), getRuntime().getResourceBundleFactory().getLocale() ) );
    }

    final CategoryPlot plot = (CategoryPlot) chart.getPlot();
    configureLogarithmicAxis( plot );

    return chart;
  }

  protected void configureChart( final JFreeChart chart ) {
    final CategoryPlot cpl = chart.getCategoryPlot();
    final CategoryItemRenderer renderer = cpl.getRenderer();
    final BarRenderer br = (BarRenderer) renderer;
    if ( isAutoRange() ) {
      br.setIncludeBaseInRange( false );
    }
    super.configureChart( chart );
    br.setDrawBarOutline( isChartSectionOutline() );
    if ( maxBarWidth != null ) {
      br.setMaximumBarWidth( maxBarWidth.doubleValue() );
    }

    if ( itemMargin != null ) {
      br.setItemMargin( itemMargin.doubleValue() );
    }

    if ( ( isStacked() ) && stackedBarRenderPercentages && ( br instanceof StackedBarRenderer ) ) {
      final StackedBarRenderer sbr = (StackedBarRenderer) br;
      sbr.setRenderAsPercentages( true );
    }

    br.setShadowVisible( shadowVisible );
    if ( shadowColor != null ) {
      br.setShadowPaint( shadowColor );
    }
    br.setShadowXOffset( shadowXOffset );
    br.setShadowYOffset( shadowYOffset );
  }

  public boolean isShadowVisible() {
    return shadowVisible;
  }

  public void setShadowVisible( final boolean shadowVisible ) {
    this.shadowVisible = shadowVisible;
  }

  public Color getShadowColor() {
    return shadowColor;
  }

  public void setShadowColor( final Color shadowColor ) {
    this.shadowColor = shadowColor;
  }

  public int getShadowXOffset() {
    return shadowXOffset;
  }

  public void setShadowXOffset( final int shadowXOffset ) {
    this.shadowXOffset = shadowXOffset;
  }

  public int getShadowYOffset() {
    return shadowYOffset;
  }

  public void setShadowYOffset( final int shadowYOffset ) {
    this.shadowYOffset = shadowYOffset;
  }

  /**
   * @return
   * @deprecated Use the property chartSectionOutline instead.
   */
  public boolean isDrawBarOutline() {
    return isChartSectionOutline();
  }

  /**
   * @param value
   * @deprecated Use the property chartSectionOutline instead.
   */
  public void setDrawBarOutline( final boolean value ) {
    setChartSectionOutline( value );
  }
}
