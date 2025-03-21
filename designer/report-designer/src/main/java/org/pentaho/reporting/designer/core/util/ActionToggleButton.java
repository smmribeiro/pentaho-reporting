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


package org.pentaho.reporting.designer.core.util;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Todo: Document me!
 * <p/>
 * Date: 24.06.2009 Time: 13:48:21
 *
 * @author Thomas Morgner.
 */
public class ActionToggleButton extends JToggleButton {
  private class SelectedHandler implements PropertyChangeListener {
    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source and the property that has changed.
     */
    public void propertyChange( final PropertyChangeEvent evt ) {
      if ( "selected".equals( evt.getPropertyName() ) ) // NON-NLS
      {
        setSelected( Boolean.TRUE.equals( evt.getNewValue() ) );
      }
      if ( Action.ACCELERATOR_KEY.equals( evt.getPropertyName() ) ) {
        final KeyStroke ks = (KeyStroke) evt.getNewValue();
        setAccelerator( ks );
      }
    }
  }

  private SelectedHandler selectedHandler;
  private KeyStroke accelerator;

  /**
   * Creates an initially unselected toggle button without setting the text or image.
   */
  public ActionToggleButton() {
  }

  /**
   * Creates a toggle button where properties are taken from the Action supplied.
   *
   * @param a the action
   * @since 1.3
   */
  public ActionToggleButton( final Action a ) {
    super( a );
  }

  /**
   * Sets the <code>Action</code> for the <code>ActionEvent</code> source. The new <code>Action</code> replaces any
   * previously set <code>Action</code> but does not affect <code>ActionListeners</code> independently added with
   * <code>addActionListener</code>. If the <code>Action</code> is already a registered <code>ActionListener</code> for
   * the button, it is not re-registered.
   * <p/>
   * A side-effect of setting the <code>Action</code> is that the <code>ActionEvent</code> source's properties  are
   * immediately set from the values in the <code>Action</code> (performed by the method
   * <code>configurePropertiesFromAction</code>) and subsequently updated as the <code>Action</code>'s properties change
   * (via a <code>PropertyChangeListener</code> created by the method <code>createActionPropertyChangeListener</code>.
   *
   * @param a the <code>Action</code> for the <code>AbstractButton</code>, or <code>null</code>
   * @beaninfo bound: true attribute: visualUpdate true description: the Action instance connected with this ActionEvent
   * source
   * @see javax.swing.Action
   * @see #getAction
   * @see #configurePropertiesFromAction
   * @see #createActionPropertyChangeListener
   * @since 1.3
   */
  public void setAction( final Action a ) {
    if ( selectedHandler == null ) {
      selectedHandler = new SelectedHandler();
    }

    final Action oldAction = getAction();
    if ( oldAction != null ) {
      oldAction.removePropertyChangeListener( selectedHandler );
    }
    super.setAction( a );
    final KeyStroke ks = ( a == null ) ? null :
      (KeyStroke) a.getValue( Action.ACCELERATOR_KEY );
    setAccelerator( ks );

    if ( a != null ) {
      a.addPropertyChangeListener( selectedHandler );
    }
  }

  public void setAccelerator( final KeyStroke ks ) {
    unregisterKeyboardAction( this.accelerator );
    if ( getAction() != null && ks != null ) {
      registerKeyboardAction( getAction(), ks, JComponent.WHEN_IN_FOCUSED_WINDOW );
    }
    this.accelerator = ks;
  }
}
