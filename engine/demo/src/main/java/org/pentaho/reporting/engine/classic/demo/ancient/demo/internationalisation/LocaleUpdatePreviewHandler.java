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


package org.pentaho.reporting.engine.classic.demo.ancient.demo.internationalisation;

import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.modules.gui.base.PreviewDialog;
import org.pentaho.reporting.engine.classic.demo.util.AbstractDemoFrame;
import org.pentaho.reporting.engine.classic.demo.util.InternalDemoHandler;
import org.pentaho.reporting.engine.classic.demo.util.PreviewHandler;
import org.pentaho.reporting.engine.classic.demo.util.ReportDefinitionException;
import org.pentaho.reporting.libraries.designtime.swing.LibSwingUtil;

/**
 * A helper class to make this demo accessible from the DemoFrontend.
 */
public class LocaleUpdatePreviewHandler implements PreviewHandler
{
  private InternalDemoHandler handler;

  public LocaleUpdatePreviewHandler(final InternalDemoHandler handler)
  {
    this.handler = handler;
  }

  public void attemptPreview()
  {
    try
    {
      final MasterReport report = handler.createReport();

      final PreviewDialog frame = new PreviewDialog(report);
      frame.setToolbarFloatable(true);
      frame.setReportController(new LocaleSelectionReportController());
      frame.pack();
      LibSwingUtil.positionFrameRandomly(frame);
      frame.setVisible(true);
      frame.requestFocus();
    }
    catch (ReportDefinitionException e)
    {
      AbstractDemoFrame.showExceptionDialog(handler.getPresentationComponent(), "report.definitionfailure", e);
    }
  }
}
