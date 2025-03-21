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

import org.pentaho.reporting.libraries.base.util.DebugLog;
import org.pentaho.reporting.libraries.css.dom.DocumentContext;
import org.pentaho.reporting.libraries.css.dom.LayoutElement;
import org.pentaho.reporting.libraries.css.dom.LayoutOutputMetaData;
import org.pentaho.reporting.libraries.css.dom.LayoutStyle;
import org.pentaho.reporting.libraries.css.keys.font.FontFamilyValues;
import org.pentaho.reporting.libraries.css.keys.font.FontStyleKeys;
import org.pentaho.reporting.libraries.css.model.StyleKey;
import org.pentaho.reporting.libraries.css.resolver.values.computed.ConstantsResolveHandler;
import org.pentaho.reporting.libraries.css.values.CSSConstant;
import org.pentaho.reporting.libraries.css.values.CSSStringValue;
import org.pentaho.reporting.libraries.css.values.CSSValue;
import org.pentaho.reporting.libraries.css.values.CSSValueList;


/**
 * Creation-Date: 18.12.2005, 16:35:28
 *
 * @author Thomas Morgner
 */
public class FontFamilyResolveHandler extends ConstantsResolveHandler {
  public FontFamilyResolveHandler() {
    addNormalizeValue( FontFamilyValues.CURSIVE );
    addNormalizeValue( FontFamilyValues.FANTASY );
    addNormalizeValue( FontFamilyValues.MONOSPACE );
    addNormalizeValue( FontFamilyValues.SANS_SERIF );
    addNormalizeValue( FontFamilyValues.SERIF );
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more dependent is the resolver on other
   * resolvers to be complete.
   *
   * @return
   */
  public StyleKey[] getRequiredStyles() {
    return new StyleKey[] {
      FontStyleKeys.FONT_WEIGHT, FontStyleKeys.FONT_VARIANT,
      FontStyleKeys.FONT_SMOOTH, FontStyleKeys.FONT_STRETCH
    };
  }

  public void resolve( final DocumentContext process,
                       final LayoutElement currentNode,
                       final StyleKey key ) {
    //Log.debug ("Processing: " + currentNode);
    final LayoutStyle layoutContext = currentNode.getLayoutStyle();
    final LayoutOutputMetaData outputMetaData = process.getOutputMetaData();
    final CSSValue cssValue = layoutContext.getValue( key );
    if ( cssValue instanceof CSSValueList ) {

      final CSSValueList list = (CSSValueList) cssValue;
      for ( int i = 0; i < list.getLength(); i++ ) {
        final CSSValue item = list.getItem( i );
        if ( item instanceof CSSConstant ) {
          final CSSConstant c = (CSSConstant) lookupValue( (CSSConstant) item );
          final CSSValue family = outputMetaData.getNormalizedFontFamilyName( c );
          if ( family != null ) {
            layoutContext.setValue( key, family );
            return;
          }
          // Ignore, although this is not ok.
          DebugLog.log( "Invalid state after setting predefined font family." );
        } else if ( item instanceof CSSStringValue ) {
          final CSSStringValue sval = (CSSStringValue) item;
          final CSSValue value = process.getOutputMetaData().getNormalizedFontFamilyName( sval );
          if ( value != null ) {
            layoutContext.setValue( key, value );
            return;
          }
        }
      }
    } else if ( cssValue instanceof CSSConstant ) {
      if ( FontFamilyValues.NONE.equals( cssValue ) ) {
        // that means: No text at all.
        return;
      }
    }


    layoutContext.setValue( key, outputMetaData.getDefaultFontFamily() );
  }
}
