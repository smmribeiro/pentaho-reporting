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


package org.pentaho.reporting.designer.core.settings;


public class Document {

  private String fileName;
  private String reportName;

  public Document( final String file, final String name ) {
    fileName = file;
    reportName = name;
  }

  public String getFileName() {
    return fileName;
  }

  public String getReportName() {
    return reportName;
  }
}
