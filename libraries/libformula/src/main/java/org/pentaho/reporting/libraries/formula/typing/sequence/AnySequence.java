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


package org.pentaho.reporting.libraries.formula.typing.sequence;

import org.pentaho.reporting.libraries.formula.EvaluationException;
import org.pentaho.reporting.libraries.formula.FormulaContext;
import org.pentaho.reporting.libraries.formula.lvalues.LValue;
import org.pentaho.reporting.libraries.formula.lvalues.TypeValuePair;
import org.pentaho.reporting.libraries.formula.typing.ArrayCallback;
import org.pentaho.reporting.libraries.formula.typing.Sequence;

/**
 * @author Cedric Pronzato
 */
public class AnySequence implements Sequence {
  private int rowCursor;
  private int columnCursor;
  private LValue single;
  private ArrayCallback array;
  private FormulaContext context;

  public AnySequence( final FormulaContext context ) {
    if ( context == null ) {
      throw new NullPointerException();
    }
    this.context = context;
  }

  public AnySequence( final ArrayCallback array, final FormulaContext context ) {
    if ( context == null ) {
      throw new NullPointerException();
    }
    if ( array == null ) {
      throw new NullPointerException();
    }
    this.array = array;
    this.context = context;
  }

  public AnySequence( final LValue single, final FormulaContext context ) {
    if ( context == null ) {
      throw new NullPointerException();
    }
    if ( single == null ) {
      throw new NullPointerException();
    }

    this.single = single;
    this.context = context;
  }

  protected AnySequence( final AnySequence anySequence ) {
    this.single = anySequence.single;
    this.context = anySequence.context;
    this.array = anySequence.array;
    this.rowCursor = anySequence.rowCursor;
    this.columnCursor = anySequence.columnCursor;
  }

  public boolean hasNext() throws EvaluationException {
    // empty sequence
    if ( single == null && array == null ) {
      return false;
    }
    // sequence of one object
    if ( single != null && rowCursor == 0 ) {
      return isValidNext( single );
    }

    // else array
    if ( array == null ) {
      return false;
    }

    final int rowCount = array.getRowCount();
    final int columnCount = array.getColumnCount();
    if ( rowCursor >= rowCount ) {
      return false;
    }
    if ( columnCursor >= columnCount ) {
      columnCursor = 0;
    }

    for (; rowCursor < rowCount; rowCursor++ ) {
      for (; columnCursor < columnCount; columnCursor++ ) {
        final LValue value = array.getRaw( rowCursor, columnCursor );
        if ( isValidNext( value ) ) {
          return true;
        }
      }
      columnCursor = 0; // go to start of the next row
    }
    return false;
  }

  protected boolean isValidNext( final LValue o ) throws EvaluationException {
    if ( o == null ) {
      return false;
    }
    return true;
  }

  public Object next() throws EvaluationException {
    if ( single != null && rowCursor == 0 ) {
      rowCursor++;
      final TypeValuePair pair = single.evaluate();
      return pair.getValue();
    }
    if ( array != null ) {
      final Object value = array.getValue( rowCursor, columnCursor );
      // advance
      if ( columnCursor == array.getColumnCount() - 1 ) {
        rowCursor++;
        columnCursor = 0;
      } else {
        columnCursor++;
      }
      return value;

    }
    return null;
  }

  public LValue nextRawValue() throws EvaluationException {
    if ( single != null && rowCursor == 0 ) {
      rowCursor++;
      return single;
    } else if ( array != null ) {
      final LValue raw = array.getRaw( rowCursor, columnCursor );
      // advance
      if ( columnCursor == array.getColumnCount() - 1 ) {
        rowCursor++;
        columnCursor = 0;
      } else {
        columnCursor++;
      }
      return raw;

    }
    return null;
  }

  protected int getRowCursor() {
    return rowCursor;
  }

  protected int getColumnCursor() {
    return columnCursor;
  }

  protected LValue getSingle() {
    return single;
  }

  protected ArrayCallback getArray() {
    return array;
  }

  protected FormulaContext getContext() {
    return context;
  }
}
