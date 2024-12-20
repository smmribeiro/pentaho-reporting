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


package org.pentaho.reporting.designer.core.actions.report;

import org.pentaho.reporting.designer.core.actions.AbstractElementSelectionAction;
import org.pentaho.reporting.designer.core.actions.ActionMessages;
import org.pentaho.reporting.designer.core.editor.ReportDocumentContext;
import org.pentaho.reporting.designer.core.editor.structuretree.ReportQueryNode;
import org.pentaho.reporting.designer.core.model.selection.DocumentContextSelectionModel;
import org.pentaho.reporting.designer.core.util.Anonymizer;
import org.pentaho.reporting.designer.core.util.IconLoader;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.event.ReportModelEvent;
import org.pentaho.reporting.engine.classic.core.metadata.DataFactoryMetaData;
import org.pentaho.reporting.engine.classic.core.util.beans.BeanException;
import org.pentaho.reporting.libraries.designtime.swing.background.BackgroundCancellableProcessHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AnonymizeDataAction extends AbstractElementSelectionAction {
  private static class AnonymizeDataSourceTask extends ConvertDataSourceAction.ConvertDataSourceTask {
    private Anonymizer anonymizer;

    private AnonymizeDataSourceTask( final ReportDocumentContext activeContext ) {
      super( activeContext );
      this.anonymizer = new Anonymizer();
    }

    protected Object process( final Object o ) throws BeanException {
      return anonymizer.anonymize( o );
    }
  }

  public AnonymizeDataAction() {
    putValue( Action.SMALL_ICON, IconLoader.getInstance().getLayoutBandsIcon() );
    putValue( Action.NAME, ActionMessages.getString( "AnonymizeDataAction.Text" ) );
    putValue( Action.DEFAULT, ActionMessages.getString( "AnonymizeDataAction.Description" ) );
    putValue( Action.MNEMONIC_KEY, ActionMessages.getOptionalMnemonic( "AnonymizeDataAction.Mnemonic" ) );
    putValue( Action.ACCELERATOR_KEY, ActionMessages.getOptionalKeyStroke( "AnonymizeDataAction.Accelerator" ) );
  }

  protected void selectedElementPropertiesChanged( final ReportModelEvent event ) {
  }

  protected void updateSelection() {
    final DocumentContextSelectionModel model = getSelectionModel();
    if ( model == null ) {
      setEnabled( false );
      return;
    }

    final Object[] selectedObjects = model.getSelectedElements();
    for ( int i = 0; i < selectedObjects.length; i++ ) {
      final Object selectedObject = selectedObjects[ i ];
      if ( selectedObject instanceof ReportQueryNode == false ) {
        continue;
      }
      final ReportQueryNode queryNode = (ReportQueryNode) selectedObject;
      final DataFactory dataFactory = queryNode.getDataFactory();
      final DataFactoryMetaData metadata = dataFactory.getMetaData();
      if ( metadata.isEditable() ) {
        setEnabled( true );
        return;
      }
    }

    setEnabled( false );
  }

  public void actionPerformed( final ActionEvent e ) {
    final ReportDocumentContext activeContext = getActiveContext();
    if ( activeContext == null ) {
      return;
    }

    final Thread thread = new Thread( new AnonymizeDataSourceTask( getActiveContext() ) );
    thread.setName( "AnonymizeDataSource-Worker" );
    thread.setDaemon( true );
    BackgroundCancellableProcessHelper.executeProcessWithCancelDialog( thread, null,
      getReportDesignerContext().getView().getParent(), ActionMessages.getString( "AnonymizeDataAction.TaskTitle" ) );
  }
}
