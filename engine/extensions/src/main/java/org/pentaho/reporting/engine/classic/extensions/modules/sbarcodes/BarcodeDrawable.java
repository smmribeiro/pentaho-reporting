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


package org.pentaho.reporting.engine.classic.extensions.modules.sbarcodes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.output.java2d.Java2DCanvasProvider;
import org.pentaho.reporting.engine.classic.core.ResourceBundleFactory;
import org.pentaho.reporting.engine.classic.core.imagemap.ImageMap;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.StyleSheet;
import org.pentaho.reporting.engine.classic.core.style.TextStyleKeys;
import org.pentaho.reporting.engine.classic.core.util.ReportDrawable;
import org.pentaho.reporting.libraries.base.config.Configuration;

public class BarcodeDrawable implements ReportDrawable {
  private BarcodeGenerator generator;
  private String message;
  private BarcodeDimension barcodeDimension;
  private Font font;
  private Color backgroundColor;
  private Color foregroundColor;

  public BarcodeDrawable( final BarcodeGenerator generator, final String message ) throws IllegalArgumentException {
    this.generator = generator;
    this.message = message;
    this.barcodeDimension = this.generator.calcDimensions( message );
  }

  public BarcodeGenerator getGenerator() {
    return generator;
  }

  protected BarcodeDimension getBarcodeDimension() {
    return barcodeDimension;
  }

  public void draw( final Graphics2D g2d, final Rectangle2D bounds ) {
    final double horzScale = bounds.getWidth() / getBarcodeDimension().getWidthPlusQuiet();
    final double vertScale = bounds.getHeight() / getBarcodeDimension().getHeightPlusQuiet();
    final double scale;
    double dx = 0;
    double dy = 0;
    if ( horzScale < vertScale ) {
      scale = horzScale;
      dy = ( ( bounds.getHeight() / scale ) - getBarcodeDimension().getHeightPlusQuiet() ) / 2;
    } else {
      scale = vertScale;
      dx = ( ( bounds.getWidth() / scale ) - getBarcodeDimension().getWidthPlusQuiet() ) / 2;
    }
    g2d.scale( scale, scale ); // scale for mm to screen pixels
    g2d.translate( dx, dy ); // center
    g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
    g2d.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON );

    if ( font != null ) {
      g2d.setFont( font );
    }
    if ( backgroundColor != null ) {
      g2d.setColor( backgroundColor );
      g2d.fill( bounds );
    }

    if ( foregroundColor != null ) {
      g2d.setColor( foregroundColor );
    }

    final Java2DCanvasProvider canvas = new Java2DCanvasProvider( g2d, 0 );

    // now paint the barcode
    generator.generateBarcode( canvas, message );
  }

  public void setConfiguration( final Configuration config ) {

  }

  public void setStyleSheet( final StyleSheet style ) {
    if ( style != null ) {
      final String fontName = (String) style.getStyleProperty( TextStyleKeys.FONT );
      final int fontSize = style.getIntStyleProperty( TextStyleKeys.FONTSIZE, 0 );
      final boolean bold = style.getBooleanStyleProperty( TextStyleKeys.BOLD );
      final boolean italics = style.getBooleanStyleProperty( TextStyleKeys.ITALIC );
      final Color foregroundColor = (Color) style.getStyleProperty( ElementStyleKeys.PAINT );
      final Color backgroundColor = (Color) style.getStyleProperty( ElementStyleKeys.BACKGROUND_COLOR );
      if ( fontName != null && fontSize > 0 ) {
        int fontstyle = Font.PLAIN;
        if ( bold ) {
          fontstyle |= Font.BOLD;
        }
        if ( italics ) {
          fontstyle |= Font.ITALIC;
        }

        this.font = ( new Font( fontName, fontstyle, fontSize ) );
      }
      if ( foregroundColor != null ) {
        this.foregroundColor = foregroundColor;
      }
      if ( backgroundColor != null ) {
        this.backgroundColor = backgroundColor;
      }
    }
  }

  public void setResourceBundleFactory( final ResourceBundleFactory bundleFactory ) {

  }

  public ImageMap getImageMap( final Rectangle2D bounds ) {
    return null;
  }
}
