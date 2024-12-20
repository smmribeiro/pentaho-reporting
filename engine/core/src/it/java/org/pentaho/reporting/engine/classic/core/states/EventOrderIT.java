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


package org.pentaho.reporting.engine.classic.core.states;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.ElementAlignment;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.elementfactory.LabelElementFactory;
import org.pentaho.reporting.engine.classic.core.function.EventMonitorFunction;
import org.pentaho.reporting.engine.classic.core.testsupport.DebugReportRunner;
import org.pentaho.reporting.engine.classic.core.testsupport.EventOrderFunction;

import javax.swing.table.DefaultTableModel;
import java.awt.geom.Rectangle2D;

/**
 * This test relies on old and now invalid behaviour. It assumes that page-events are fired before any other
 * state-processing takes place. As with 0.8.9, page events are fired in the prepare-events, and the page-count from the
 * page-function is not in sync with the pages from the state.
 */
public class EventOrderIT extends TestCase {
  private static final Log logger = LogFactory.getLog( EventOrderIT.class );

  public EventOrderIT() {
  }

  public EventOrderIT( final String s ) {
    super( s );
  }

  protected void setUp() throws Exception {
    ClassicEngineBoot.getInstance().start();
  }

  private MasterReport getReport() throws Exception {
    final MasterReport report = new MasterReport();
    report.getReportHeader().addElement(
        LabelElementFactory.createLabelElement( null, new Rectangle2D.Float( 0, 0, 150, 20 ), null,
            ElementAlignment.LEFT, null, "Text" ) );

    report.getReportFooter().addElement(
        LabelElementFactory.createLabelElement( null, new Rectangle2D.Float( 0, 0, 150, 20 ), null,
            ElementAlignment.LEFT, null, "Text" ) );

    report.getPageHeader().addElement(
        LabelElementFactory.createLabelElement( null, new Rectangle2D.Float( 0, 0, 150, 20 ), null,
            ElementAlignment.LEFT, null, "Text" ) );

    report.getPageFooter().addElement(
        LabelElementFactory.createLabelElement( null, new Rectangle2D.Float( 0, 0, 150, 20 ), null,
            ElementAlignment.LEFT, null, "Text" ) );

    report.getItemBand().addElement(
        LabelElementFactory.createLabelElement( null, new Rectangle2D.Float( 0, 0, 150, 20 ), null,
            ElementAlignment.LEFT, null, "Text" ) );

    report.getRelationalGroup( 0 ).getHeader().addElement(
        LabelElementFactory.createLabelElement( null, new Rectangle2D.Float( 0, 0, 150, 20 ), null,
            ElementAlignment.LEFT, null, "Text" ) );

    report.getRelationalGroup( 0 ).getFooter().addElement(
        LabelElementFactory.createLabelElement( null, new Rectangle2D.Float( 0, 0, 150, 20 ), null,
            ElementAlignment.LEFT, null, "Text" ) );

    report.addExpression( new EventOrderFunction( "event-order" ) );
    report.addExpression( new EventMonitorFunction( "event-monitor" ) );
    return report;
  }

  public void testEventOrder() throws Exception {
    final MasterReport report = getReport();
    final DefaultTableModel model = new DefaultTableModel( 2, 1 );
    model.setValueAt( "0-0", 0, 0 );
    model.setValueAt( "0-1", 1, 0 );
    report.setDataFactory( new TableDataFactory( "default", model ) );

    DebugReportRunner.executeAll( report );
  }

}
