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


package org.pentaho.reporting.engine.classic.demo.elements.sparklines;

import java.net.URL;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;

import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.demo.util.AbstractXmlDemoHandler;
import org.pentaho.reporting.engine.classic.demo.util.ReportDefinitionException;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;

/**
 * This demo shows different usages of Sparkline module available in pentaho reporting extension project.
 *
 * @author Cedric Pronzato
 */
public class SparklineXMLDemo extends AbstractXmlDemoHandler
{
  private DefaultTableModel data;
  private final Random random;

  public SparklineXMLDemo()
  {
    random = new Random();
    data = createData();
  }

  /**
   * Creates data of sales per months.
   *
   * @return The tabular report data.
   */
  private DefaultTableModel createData()
  {
    final String[] columnNames = {"January", "February", "March", "April", "May", "June", "July", "August",
        "September", "October", "November", "December"};
    final DefaultTableModel data = new DefaultTableModel(columnNames, 10);

    for (int r = 0; r < 10; r++)
    {
      for (int i = 0; i < 12 && r < 10; i++)
      {
        final Integer value = new Integer(random.nextInt(30));
        data.setValueAt(value, r, i);
      }
    }

    return data;
  }

  /**
   * Returns the display name of the demo.
   *
   * @return the name.
   */
  public String getDemoName()
  {
    return "Sparkline bar-graph demo (Simple-XML)";
  }

  /**
   * Creates the report. For XML reports, this will most likely call the ReportGenerator, while API reports may use this
   * function to build and return a new, fully initialized report object.
   *
   * @return the fully initialized JFreeReport object.
   * @throws org.pentaho.reporting.engine.classic.demo.util.ReportDefinitionException
   *          if an error occured preventing the report definition.
   */
  public MasterReport createReport() throws ReportDefinitionException
  {
    final MasterReport report = parseReport();
    report.setDataFactory(new TableDataFactory
        ("default", data));
    return report;
  }

  /**
   * Returns the URL of the HTML document describing this demo.
   *
   * @return the demo description.
   */
  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
        ("sparkline-simple.html", SparklineXMLDemo.class);
  }

  /**
   * Returns the presentation component for this demo. This component is shown before the real report generation is
   * started. Ususally it contains a JTable with the demo data and/or input components, which allow to configure the
   * report.
   *
   * @return the presentation component, never null.
   */
  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  /**
   * Returns the URL of the XML definition for this report.
   *
   * @return the URL of the report definition.
   */
  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
        ("sparkline-simple.xml", SparklineXMLDemo.class);
  }
}
