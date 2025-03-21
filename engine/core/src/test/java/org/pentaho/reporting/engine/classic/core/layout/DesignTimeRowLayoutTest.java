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


package org.pentaho.reporting.engine.classic.core.layout;

import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportHeader;
import org.pentaho.reporting.engine.classic.core.layout.model.LayoutNodeTypes;
import org.pentaho.reporting.engine.classic.core.layout.model.LogicalPageBox;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderNode;
import org.pentaho.reporting.engine.classic.core.layout.table.TableTestUtil;
import org.pentaho.reporting.engine.classic.core.style.BandStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.testsupport.DebugReportRunner;
import org.pentaho.reporting.engine.classic.core.testsupport.selector.MatchFactory;
import org.pentaho.reporting.engine.classic.core.util.geom.StrictGeomUtility;

public class DesignTimeRowLayoutTest {
  public DesignTimeRowLayoutTest() {
  }

  @Before
  public void setUp() throws Exception {
    ClassicEngineBoot.getInstance().start();
  }

  @Test
  public void testRowLayoutAtDesignTimeInv() throws Exception {
    LogFactory.getLog( "test" ).error( "Test" );
    MasterReport report = new MasterReport();
    ReportHeader reportHeader = report.getReportHeader();
    reportHeader.setLayout( BandStyleKeys.LAYOUT_ROW );
    reportHeader.getStyle().setStyleProperty( ElementStyleKeys.INVISIBLE_CONSUMES_SPACE, true );
    reportHeader.addElement( createElement( true ) );
    reportHeader.addElement( createElement( false ) );
    reportHeader.addElement( createElement( true ) );

    LogicalPageBox logicalPageBox = DebugReportRunner.layoutSingleBandInDesignTime( report, reportHeader );

    RenderNode[] elementsByElementType =
        MatchFactory.findElementsByNodeType( logicalPageBox, LayoutNodeTypes.TYPE_BOX_PARAGRAPH );
    Assert.assertEquals( 3, elementsByElementType.length );

    Assert.assertEquals( 0, elementsByElementType[0].getX() );
    Assert.assertEquals( StrictGeomUtility.toInternalValue( 100 ), elementsByElementType[0].getWidth() );
    Assert.assertEquals( StrictGeomUtility.toInternalValue( 100 ), elementsByElementType[1].getX() );
    Assert.assertEquals( StrictGeomUtility.toInternalValue( 100 ), elementsByElementType[1].getWidth() );
    Assert.assertEquals( StrictGeomUtility.toInternalValue( 200 ), elementsByElementType[2].getX() );
    Assert.assertEquals( StrictGeomUtility.toInternalValue( 100 ), elementsByElementType[2].getWidth() );
  }

  @Test
  public void testRowLayoutAtDesignTime() throws Exception {
    LogFactory.getLog( "test" ).error( "Test" );
    MasterReport report = new MasterReport();
    ReportHeader reportHeader = report.getReportHeader();
    reportHeader.setLayout( BandStyleKeys.LAYOUT_ROW );
    reportHeader.getStyle().setStyleProperty( ElementStyleKeys.INVISIBLE_CONSUMES_SPACE, false );
    reportHeader.addElement( createElement( true ) );
    reportHeader.addElement( createElement( false ) );
    reportHeader.addElement( createElement( true ) );

    LogicalPageBox logicalPageBox = DebugReportRunner.layoutSingleBandInDesignTime( report, reportHeader );

    RenderNode[] elementsByElementType =
        MatchFactory.findElementsByNodeType( logicalPageBox, LayoutNodeTypes.TYPE_BOX_PARAGRAPH );
    Assert.assertEquals( 3, elementsByElementType.length );

    Assert.assertEquals( 0, elementsByElementType[0].getX() );
    Assert.assertEquals( StrictGeomUtility.toInternalValue( 100 ), elementsByElementType[0].getWidth() );
    Assert.assertEquals( StrictGeomUtility.toInternalValue( 100 ), elementsByElementType[1].getX() );
    Assert.assertEquals( StrictGeomUtility.toInternalValue( 100 ), elementsByElementType[1].getWidth() );
    Assert.assertEquals( StrictGeomUtility.toInternalValue( 100 ), elementsByElementType[2].getX() );
    Assert.assertEquals( StrictGeomUtility.toInternalValue( 100 ), elementsByElementType[2].getWidth() );
  }

  @Test
  public void testRowLayoutAtRunTime() throws Exception {
    MasterReport report = new MasterReport();
    ReportHeader reportHeader = report.getReportHeader();
    reportHeader.setLayout( BandStyleKeys.LAYOUT_ROW );
    reportHeader.getStyle().setStyleProperty( ElementStyleKeys.INVISIBLE_CONSUMES_SPACE, false );
    reportHeader.addElement( createElement( true ) );
    reportHeader.addElement( createElement( false ) );
    reportHeader.addElement( createElement( true ) );

    LogicalPageBox logicalPageBox = DebugReportRunner.layoutSingleBand( report, reportHeader );

    RenderNode[] elementsByElementType =
        MatchFactory.findElementsByNodeType( logicalPageBox, LayoutNodeTypes.TYPE_BOX_PARAGRAPH );
    Assert.assertEquals( 2, elementsByElementType.length );

    Assert.assertEquals( 0, elementsByElementType[0].getX() );
    Assert.assertEquals( StrictGeomUtility.toInternalValue( 100 ), elementsByElementType[0].getWidth() );
    Assert.assertEquals( StrictGeomUtility.toInternalValue( 100 ), elementsByElementType[1].getX() );
    Assert.assertEquals( StrictGeomUtility.toInternalValue( 100 ), elementsByElementType[1].getWidth() );
  }

  private Element createElement( boolean visible ) {
    Element element = TableTestUtil.createDataItem( "Test" );
    element.setVisible( visible );
    return element;
  }
}
