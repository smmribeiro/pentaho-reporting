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


package org.pentaho.reporting.engine.classic.core.filter;

import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.function.ExpressionRuntime;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;

import java.text.DateFormat;
import java.text.Format;
import java.util.Date;
import java.util.TimeZone;

/**
 * A filter that creates string from dates. This filter will format java.util. Date objects using a java.text.DateFormat
 * to create the string representation for the date obtained from the datasource.
 * <p/>
 * If the object read from the datasource is no date, the NullValue defined by setNullValue(Object) is returned.
 *
 * @author Thomas Morgner
 * @see java.text.DateFormat
 */
public class DateFormatFilter extends FormatFilter {
  private transient TimeZone timeZone;

  /**
   * Default constructor. Creates a new filter using the default date format for the current locale.
   */
  public DateFormatFilter() {
    setFormatter( DateFormat.getInstance() );
  }

  /**
   * Returns the date format object.
   *
   * @return The date format object.
   */
  public DateFormat getDateFormat() {
    return (DateFormat) getFormatter();
  }

  /**
   * Sets the date format for the filter.
   *
   * @param format
   *          The format.
   * @throws NullPointerException
   *           if the format given is null
   */
  public void setDateFormat( final DateFormat format ) {
    super.setFormatter( format );
  }

  /**
   * Sets the formatter.
   *
   * @param format
   *          The format.
   * @throws ClassCastException
   *           if the format given is no DateFormat
   * @throws NullPointerException
   *           if the format given is null
   */
  public void setFormatter( final Format format ) {
    final DateFormat dfmt = (DateFormat) format;
    super.setFormatter( dfmt );
  }

  public Object getRawValue( final ExpressionRuntime runtime, final ReportElement element ) {
    final Object value = super.getRawValue( runtime, element );
    if ( value instanceof Number ) {
      // Automagically fix numbers into dates
      final Number number = (Number) value;
      return new Date( number.longValue() );
    }
    return value;
  }

  public Object getValue( final ExpressionRuntime runtime, final ReportElement element ) {
    final TimeZone timeZone = runtime.getResourceBundleFactory().getTimeZone();
    if ( ObjectUtilities.equal( timeZone, this.timeZone ) == false ) {
      this.timeZone = timeZone;
      getDateFormat().setTimeZone( timeZone );
    }
    return super.getValue( runtime, element );
  }

  public FormatSpecification getFormatString( final ExpressionRuntime runtime, final ReportElement element,
      FormatSpecification formatSpecification ) {
    final DataSource source = getDataSource();
    if ( source instanceof RawDataSource ) {
      final RawDataSource rds = (RawDataSource) source;
      return rds.getFormatString( runtime, element, formatSpecification );
    }
    if ( formatSpecification == null ) {
      formatSpecification = new FormatSpecification();
    }
    formatSpecification.redefine( FormatSpecification.TYPE_UNDEFINED, null );
    return formatSpecification;
  }

}
