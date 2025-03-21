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


package org.pentaho.reporting.libraries.css.resolver.values.computed.fonts;

import org.pentaho.reporting.libraries.css.LibCssBoot;
import org.pentaho.reporting.libraries.css.StyleSheetUtility;
import org.pentaho.reporting.libraries.css.dom.DocumentContext;
import org.pentaho.reporting.libraries.css.dom.LayoutElement;
import org.pentaho.reporting.libraries.css.dom.LayoutOutputMetaData;
import org.pentaho.reporting.libraries.css.dom.LayoutStyle;
import org.pentaho.reporting.libraries.css.dom.OutputProcessorFeature;
import org.pentaho.reporting.libraries.css.keys.font.FontSizeConstant;
import org.pentaho.reporting.libraries.css.keys.font.FontStyleKeys;
import org.pentaho.reporting.libraries.css.keys.font.RelativeFontSize;
import org.pentaho.reporting.libraries.css.model.StyleKey;
import org.pentaho.reporting.libraries.css.resolver.values.computed.ConstantsResolveHandler;
import org.pentaho.reporting.libraries.css.values.CSSConstant;
import org.pentaho.reporting.libraries.css.values.CSSNumericType;
import org.pentaho.reporting.libraries.css.values.CSSNumericValue;
import org.pentaho.reporting.libraries.css.values.CSSValue;

// todo apply built in defaults instead ...

/**
 * Creation-Date: 21.12.2005, 12:47:43
 *
 * @author Thomas Morgner
 */
public class MinMaxFontSizeResolveHandler extends ConstantsResolveHandler {
  private static final String SIZE_FACTOR_PREFIX =
    "org.jfree.layouting.defaults.FontSizeFactor.";
  private double fontSize;
  private CSSNumericValue[] predefinedSizes;
  private double[] predefinedScalingFactors;

  public MinMaxFontSizeResolveHandler() {
    fontSize = parseDouble( "org.jfree.layouting.defaults.FontSize", 12 );
    predefinedSizes = new CSSNumericValue[ 7 ];
    predefinedSizes[ 0 ] = computePredefinedSize( FontSizeConstant.XX_SMALL );
    predefinedSizes[ 1 ] = computePredefinedSize( FontSizeConstant.X_SMALL );
    predefinedSizes[ 2 ] = computePredefinedSize( FontSizeConstant.SMALL );
    predefinedSizes[ 3 ] = computePredefinedSize( FontSizeConstant.MEDIUM );
    predefinedSizes[ 4 ] = computePredefinedSize( FontSizeConstant.LARGE );
    predefinedSizes[ 5 ] = computePredefinedSize( FontSizeConstant.X_LARGE );
    predefinedSizes[ 6 ] = computePredefinedSize( FontSizeConstant.XX_LARGE );

    predefinedScalingFactors = new double[ 7 ];
    predefinedScalingFactors[ 0 ] = computePredefinedScalingFactor( FontSizeConstant.XX_SMALL );
    predefinedScalingFactors[ 1 ] = computePredefinedScalingFactor( FontSizeConstant.X_SMALL );
    predefinedScalingFactors[ 2 ] = computePredefinedScalingFactor( FontSizeConstant.SMALL );
    predefinedScalingFactors[ 3 ] = computePredefinedScalingFactor( FontSizeConstant.MEDIUM );
    predefinedScalingFactors[ 4 ] = computePredefinedScalingFactor( FontSizeConstant.LARGE );
    predefinedScalingFactors[ 5 ] = computePredefinedScalingFactor( FontSizeConstant.X_LARGE );
    predefinedScalingFactors[ 6 ] = computePredefinedScalingFactor( FontSizeConstant.XX_LARGE );

    addValue( FontSizeConstant.XX_SMALL, predefinedSizes[ 0 ] );
    addValue( FontSizeConstant.X_SMALL, predefinedSizes[ 1 ] );
    addValue( FontSizeConstant.SMALL, predefinedSizes[ 2 ] );
    addValue( FontSizeConstant.MEDIUM, predefinedSizes[ 3 ] );
    addValue( FontSizeConstant.LARGE, predefinedSizes[ 4 ] );
    addValue( FontSizeConstant.X_LARGE, predefinedSizes[ 5 ] );
    addValue( FontSizeConstant.XX_LARGE, predefinedSizes[ 6 ] );
  }

  private CSSNumericValue computePredefinedSize( final CSSConstant c ) {
    final String key = SIZE_FACTOR_PREFIX + c.getCSSText();
    final double scaling = parseDouble( key, 100 );
    return CSSNumericValue.createValue( CSSNumericType.PT, fontSize * scaling / 100d );
  }

  private double computePredefinedScalingFactor( final CSSConstant c ) {
    final String key = SIZE_FACTOR_PREFIX + c.getCSSText();
    return parseDouble( key, 100 );
  }

  private double parseDouble( final String configKey, final double defaultValue ) {
    final String value = LibCssBoot.getInstance().getGlobalConfig().getConfigProperty( configKey );
    if ( value == null ) {
      return defaultValue;
    }
    try {
      return Double.parseDouble( value );
    } catch ( NumberFormatException nfe ) {
      return defaultValue;
    }
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more dependent is the resolver on other
   * resolvers to be complete.
   *
   * @return
   */
  public StyleKey[] getRequiredStyles() {
    return new StyleKey[] {
      FontStyleKeys.FONT_SIZE
    };
  }

  public void resolve( final DocumentContext process,
                       final LayoutElement currentNode,
                       final StyleKey key ) {
    final LayoutStyle layoutContext = currentNode.getLayoutStyle();
    final CSSValue value = layoutContext.getValue( key );
    if ( value instanceof CSSConstant == false ) {
      // fine, we're done here ...
      return;
    }
    final CSSConstant constant = (CSSConstant) value;
    // parent font size has been resolved already. Parent is the resolved font size of this element.


    final LayoutOutputMetaData metaData = process.getOutputMetaData();
    final int resolution = (int) metaData.getNumericFeatureValue( OutputProcessorFeature.DEVICE_RESOLUTION );
    final CSSValue fontSizeValue = currentNode.getLayoutStyle().getValue( FontStyleKeys.FONT_SIZE );
    final double fontSize = StyleSheetUtility.convertLengthToDouble( fontSizeValue, resolution );
    if ( RelativeFontSize.LARGER.equals( value ) ) {
      final double scaleFactor = getScaleLargerFactor( fontSize );
      layoutContext.setValue( key, CSSNumericValue.createValue( CSSNumericType.PERCENTAGE, scaleFactor ) );
    } else if ( RelativeFontSize.SMALLER.equals( value ) ) {
      final double scaleFactor = getScaleSmallerFactor( fontSize );
      layoutContext.setValue( key, CSSNumericValue.createValue( CSSNumericType.PERCENTAGE, scaleFactor ) );
    }

    final CSSValue resolvedValue = lookupValue( constant );
    if ( resolvedValue != null ) {
      layoutContext.setValue( key, resolvedValue );
      return;
    }
    if ( key.equals( FontStyleKeys.MAX_FONT_SIZE ) ) {
      // there is no upper limit if the value is invalid
      layoutContext.setValue( key, CSSNumericValue.createValue( CSSNumericType.PT, Short.MAX_VALUE ) );
    } else {
      // there is no lower limit if the value is invalid
      layoutContext.setValue( key, CSSNumericValue.createValue( CSSNumericType.PT, 0 ) );
    }
  }

  public double getScaleLargerFactor( final double parentSize ) {
    for ( int i = 0; i < predefinedSizes.length; i++ ) {
      final CSSNumericValue size = predefinedSizes[ i ];
      if ( parentSize < size.getValue() ) {
        return predefinedScalingFactors[ i ];
      }
    }
    return predefinedScalingFactors[ 6 ];
  }

  public double getScaleSmallerFactor( final double parentSize ) {
    for ( int i = predefinedSizes.length; i >= 0; i-- ) {
      final CSSNumericValue size = predefinedSizes[ i ];
      if ( parentSize > size.getValue() ) {
        return predefinedScalingFactors[ i ];
      }
    }
    return predefinedScalingFactors[ 0 ];
  }

}
