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


package org.pentaho.reporting.engine.classic.core.event;

import org.pentaho.reporting.engine.classic.core.states.ReportState;

import java.util.EventObject;

/**
 * A report progress event notifies the listeners about the proceedings of the report processing. It is generated by the
 * report processor implementations.
 *
 * @author Thomas Morgner
 */
public class ReportProgressEvent extends EventObject implements Cloneable {
  /**
   * An activity constant that indicates that the current report is currently being processed. (This is a boilerplate
   * event in case none of the other events matched.)
   */
  public static final int COMPUTING_LAYOUT = 0;

  /**
   * An activity constant that indicates that the report is precomputing values. This is usually the first stage of
   * report processing.
   */
  public static final int PRECOMPUTING_VALUES = 1;

  /**
   * An activity constant that indicates that the report's page-layout is being computed. This computes where pagebreaks
   * will be inserted and how many pages are available.
   */
  public static final int PAGINATING = 2;

  /**
   * An activity constant that indicates that the report content is generated. This is the final processing step.
   */
  public static final int GENERATING_CONTENT = 3;

  private static final int DEFAULT_VALUE = -1;

  /**
   * The progress level is an indicator for the current processing level.
   */
  private int level;

  /**
   * The maximum value the progress level will get for this report.
   */
  private int maximumLevel;

  /**
   * The indicator for the current activity. One of COMPUTING_LAYOUT, PRECOMPUTING_VALUES, PAGINATING or
   * GENERATING_CONTENT.
   */
  private int activity;

  /**
   * The current row of the outermost master report.
   */
  private int row;
  /**
   * The maximum row of the outermost master report.
   */
  private int maximumRow;
  /**
   * The current physical page.
   */
  private int page;

  /**
   * Total number of pages in the report
   */
  private int totalPages;

  /**
   * Creates a new even without any properties defined. Use this to create a reusable event object.
   *
   * @param source
   *          the report processor that generated this event.
   */
  public ReportProgressEvent( final Object source ) {
    super( source );
    this.maximumLevel = DEFAULT_VALUE;
    this.level = DEFAULT_VALUE;
    this.maximumRow = DEFAULT_VALUE;
    this.page = DEFAULT_VALUE;
    this.totalPages = DEFAULT_VALUE;
    this.activity = COMPUTING_LAYOUT;
    this.row = DEFAULT_VALUE;
  }

  /**
   * Creates a new even without any properties defined. Use this to create a reusable event object.
   *
   * @param source
   *          the report processor that generated this event.
   * @deprecated Use constructor with total pages instead
   */
  @Deprecated
  public ReportProgressEvent( final Object source, final int page ) {
    super( source );
    this.maximumLevel = DEFAULT_VALUE;
    this.level = DEFAULT_VALUE;
    this.maximumRow = DEFAULT_VALUE;
    this.page = page;
    this.totalPages = DEFAULT_VALUE;
    this.activity = COMPUTING_LAYOUT;
    this.row = DEFAULT_VALUE;
  }


  /**
   * Creates a new even without any properties defined. Use this to create a reusable event object.
   *
   * @param source
   *          the report processor that generated this event.
   * @param totalPages
   *          total pages in the report
   */
  public ReportProgressEvent( final Object source, final int page, final int totalPages ) {
    this( source, page );
    this.totalPages = totalPages;
  }

  /**
   * Creates a new report-progress event.
   *
   * @param source
   *          the report processor that generated this event.
   * @param activity
   *          the current activity.
   * @param row
   *          the currently processed row.
   * @param maximumRow
   *          the number of rows in this local report.
   * @param page
   *          the current page that is being processed.
   * @param level
   *          the current processing level.
   * @param maximumLevel
   *          the maximum processing level.
   * @deprecated Use constructor with total pages instead
   */
  @Deprecated
  public ReportProgressEvent( final Object source, final int activity, final int row, final int maximumRow,
      final int page, final int level, final int maximumLevel ) {
    super( source );
    this.maximumLevel = maximumLevel;
    this.level = level;
    this.maximumRow = maximumRow;
    this.page = page;
    this.totalPages = DEFAULT_VALUE;
    this.activity = activity;
    this.row = row;
  }


  /**
   * Creates a new report-progress event.
   *
   * @param source
   *          the report processor that generated this event.
   * @param activity
   *          the current activity.
   * @param row
   *          the currently processed row.
   * @param maximumRow
   *          the number of rows in this local report.
   * @param page
   *          the current page that is being processed.
   * @param totalPages
   *          total pages in the report
   * @param level
   *          the current processing level.
   * @param maximumLevel
   *          the maximum processing level.
   */
  public ReportProgressEvent( final Object source, final int activity, final int row, final int maximumRow,
                              final int page, final int totalPages, final int level, final int maximumLevel ) {
    this( source, activity, row, maximumRow, page, level, maximumLevel );
    this.totalPages = totalPages;
  }

  /**
   * Returns a string representation of this object.
   *
   * @return a string representing the state of this object.
   */
  public String toString() {
    return "ReportProgressEvent[activity=" + activity //$NON-NLS-1$
        + ", row=" + row //$NON-NLS-1$
        + ", maximumRow=" + maximumRow //$NON-NLS-1$
        + ", page=" + page //$NON-NLS-1$
        + ", totalPages=" + totalPages //$NON-NLS-1$
        + ", level=" + level //$NON-NLS-1$
        + ", maximumLevel=" + maximumLevel + ']'; //$NON-NLS-1$ //$NON-NLS-2$
  }

  /**
   * Returns the current row.
   *
   * @return the row.
   */
  public int getRow() {
    return row;
  }

  /**
   * Returns the current activity (one of COMPUTING_LAYOUT, PRECOMPUTING_VALUES, PAGINATING or GENERATING_CONTENT).
   *
   * @return the activity constant.
   * @see ReportProgressEvent#COMPUTING_LAYOUT
   * @see ReportProgressEvent#PRECOMPUTING_VALUES
   * @see ReportProgressEvent#PAGINATING
   * @see ReportProgressEvent#GENERATING_CONTENT
   */
  public int getActivity() {
    return activity;
  }

  /**
   * Returns the current page number.
   *
   * @return the current page.
   */
  public int getPage() {
    return page;
  }

  /**
   * Returns total number pages in the report
   *
   * @return total number pages in the report
   */
  public int getTotalPages() {
    return totalPages;
  }

  /**
   * Returns the total number of rows contained in this report's datasource.
   *
   * @return the number of rows.
   */
  public int getMaximumRow() {
    return maximumRow;
  }

  /**
   * Returns the maximum level the report processing can reach for the report that generated the event.
   *
   * @return the maximum level.
   */
  public int getMaximumLevel() {
    return maximumLevel;
  }

  /**
   * Returns the current processing level. Report processing is a multi-pass process, the level provides a linear
   * measurement of the current progress.
   *
   * @return the level.
   */
  public int getLevel() {
    return level;
  }

  public void reuse( final int activity, final ReportState rawState, final int pageCount ) {
    reuse( activity, rawState, pageCount, this.totalPages );
  }


  public void reuse( final int activity, final ReportState rawState, final int pageCount, final int totalPages ) {
    ReportState state = rawState;
    while ( state.getParentState() != null ) {
      state = state.getParentState();
    }
    reuse( activity, state.getCurrentRow(), state.getNumberOfRows(), pageCount, totalPages, state.getProgressLevel(), state
      .getProgressLevelCount() );
  }

  /**
   * Reuses the report event by updating the internal properties. This is used as simple mean to reduce the number of
   * objects generated in the system and should not be used elsewhere.
   *
   * @param activity
   *          the activity as constant.
   * @param row
   *          the current row.
   * @param maximumRow
   *          the total rows in the datasource.
   * @param page
   *          the current page.
   * @param level
   *          the current processing level.
   * @param maximumLevel
   *          the maximum processing level.
   */
  public void reuse( final int activity, final int row, final int maximumRow, final int page, final int level,
      final int maximumLevel ) {
    this.maximumRow = maximumRow;
    this.page = page;
    this.activity = activity;
    this.row = row;
    this.maximumLevel = maximumLevel;
    this.level = level;
  }


  /**
   * Reuses the report event by updating the internal properties. This is used as simple mean to reduce the number of
   * objects generated in the system and should not be used elsewhere.
   *
   * @param activity
   *          the activity as constant.
   * @param row
   *          the current row.
   * @param maximumRow
   *          the total rows in the datasource.
   * @param page
   *          the current page.
   * @param totalPages
   *          total pages in the report
   * @param level
   *          the current processing level.
   * @param maximumLevel
   *          the maximum processing level.
   */
  public void reuse( final int activity, final int row, final int maximumRow, final int page, final int totalPages, final int level,
                     final int maximumLevel ) {
    reuse( activity, row, maximumRow, page, level, maximumLevel );
    this.totalPages = totalPages;
  }

  /**
   * Creats a copy of the current instance of this object.
   *
   * @return a copy of the current object as a new object.
   */
  public Object clone() {
    try {
      return super.clone();
    } catch ( final CloneNotSupportedException e ) {
      throw new IllegalStateException( "Cloning not successful." );
    }
  }

  /**
   * Computes the percentage complete (on a scale from 0.0 to 100.0) based on the information found in the report
   * progress event.
   *
   * @param event
   *          the data used to calculate the percentage complete
   * @param onlyPagination
   *          true, if the processing stops after pagination, or false, if a full export is done.
   * @return the calculated percentage complete
   */
  public static double computePercentageComplete( final ReportProgressEvent event, final boolean onlyPagination ) {
    final double levelPercentage; // the amount of work already done in previous levels;
    final double levelSizePercentage; // the amount of work we complete this time ..
    if ( event.getLevel() == Integer.MAX_VALUE ) {
      // reserve 10% for structural computations
      levelPercentage = 0;
      levelSizePercentage = 0.1;
    } else if ( onlyPagination ) {
      // reserve 10% for the structure (and assume it has been done already)
      // and assume the layouting takes at least 5 times as much effort than a single level.
      final int dataLevels = Math.max( 0, event.getMaximumLevel() );
      final int layoutWeight = 5;

      if ( event.getLevel() == ( event.getMaximumLevel() - 1 ) ) {
        // layouter stage
        levelPercentage = 0.1 + ( 0.9 * ( dataLevels / ( dataLevels + layoutWeight ) ) );
        levelSizePercentage = 1.0 - levelPercentage;
      } else {
        // data stage
        levelPercentage = 0.1 + ( 0.9 * ( event.getLevel() / ( dataLevels + layoutWeight ) ) );
        levelSizePercentage = 0.1 + ( 0.9 * ( 1.0 / ( dataLevels + layoutWeight ) ) );
      }
    } else {
      final int dataLevels = Math.max( 0, event.getMaximumLevel() );
      final int layoutWeight = 5;

      if ( event.getLevel() == ( event.getMaximumLevel() - 1 ) ) {
        if ( event.getActivity() == ReportProgressEvent.GENERATING_CONTENT ) {
          levelPercentage = 0.1 + ( 0.9 * ( event.getLevel() / dataLevels + 2 * layoutWeight ) );
          levelSizePercentage = 0.1 + ( 0.9 * ( ( dataLevels + layoutWeight ) / ( dataLevels + 2 * layoutWeight ) ) );
        } else {
          levelPercentage = 0.1 + ( 0.9 * ( event.getLevel() / ( dataLevels + 2 * layoutWeight ) ) );
          levelSizePercentage = 0.1 + ( 0.9 * ( dataLevels / ( dataLevels + 2 * layoutWeight ) ) );
        }
      } else {
        // data stage
        levelPercentage = 0.1 + ( 0.9 * ( event.getLevel() / ( dataLevels + 2 * layoutWeight ) ) );
        levelSizePercentage = 0.1 + ( 0.9 * ( 1.0 / ( dataLevels + 2 * layoutWeight ) ) );
      }
    }

    final double subPercentage = levelSizePercentage * ( event.getRow() / (double) event.getMaximumRow() );
    final double percentage = 100.0 * ( levelPercentage + subPercentage );
    return Math.max( 0.0, Math.min( 100.0, percentage ) );
  }
}
