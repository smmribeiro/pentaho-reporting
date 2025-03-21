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


package org.pentaho.reporting.engine.classic.core.modules.parser.bundle.layout;

import org.pentaho.reporting.engine.classic.core.style.BandStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleSheet;
import org.pentaho.reporting.engine.classic.core.style.TableLayout;
import org.pentaho.reporting.engine.classic.core.util.beans.BeanException;
import org.pentaho.reporting.engine.classic.core.util.beans.ConverterRegistry;
import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.ParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Todo: Document Me
 *
 * @author Thomas Morgner
 */
public class BandStyleReadHandler extends AbstractXmlReadHandler implements StyleReadHandler {
  private ElementStyleSheet styleSheet;

  public BandStyleReadHandler() {
  }

  public void setStyleSheet( final ElementStyleSheet styleSheet ) {
    this.styleSheet = styleSheet;
  }

  public ElementStyleSheet getStyleSheet() {
    return styleSheet;
  }

  /**
   * Starts parsing.
   *
   * @param attrs
   *          the attributes.
   * @throws SAXException
   *           if there is a parsing error.
   */
  protected void startParsing( final Attributes attrs ) throws SAXException {
    final String computedSheetName = attrs.getValue( getUri(), "computed-sheetname" );
    if ( computedSheetName != null ) {
      styleSheet.setStyleProperty( BandStyleKeys.COMPUTED_SHEETNAME, computedSheetName );
    }

    final String bookmark = attrs.getValue( getUri(), "bookmark" );
    if ( bookmark != null ) {
      styleSheet.setStyleProperty( BandStyleKeys.BOOKMARK, bookmark );
    }

    final String pagebreakBefore = attrs.getValue( getUri(), "pagebreak-before" );
    if ( pagebreakBefore != null ) {
      styleSheet.setBooleanStyleProperty( BandStyleKeys.PAGEBREAK_BEFORE, "true".equals( pagebreakBefore ) );
    }

    final String pagebreakAfter = attrs.getValue( getUri(), "pagebreak-after" );
    if ( pagebreakAfter != null ) {
      styleSheet.setBooleanStyleProperty( BandStyleKeys.PAGEBREAK_AFTER, "true".equals( pagebreakAfter ) );
    }

    final String layout = attrs.getValue( getUri(), "layout" );
    if ( layout != null ) {
      styleSheet.setStyleProperty( BandStyleKeys.LAYOUT, layout );
    }

    final String tableLayout = attrs.getValue( getUri(), "table-layout" );
    if ( tableLayout != null ) {
      try {
        final TableLayout o = (TableLayout) ConverterRegistry.toPropertyValue( tableLayout, TableLayout.class );
        styleSheet.setStyleProperty( BandStyleKeys.TABLE_LAYOUT, o );
      } catch ( BeanException e ) {
        throw new ParseException( "Attribute 'table-layout' is not of an expected value.", getLocator() );
      }
    }
  }

  /**
   * Returns the object for this element or null, if this element does not create an object.
   *
   * @return the object.
   * @throws SAXException
   *           if an parser error occured.
   */
  public Object getObject() throws SAXException {
    return styleSheet;
  }
}
