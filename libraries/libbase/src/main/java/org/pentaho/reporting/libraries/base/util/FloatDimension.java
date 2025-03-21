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


package org.pentaho.reporting.libraries.base.util;

import java.awt.geom.Dimension2D;
import java.io.Serializable;

/**
 * A dimension object specified using <code>float</code> values.
 *
 * @author Thomas Morgner
 */
public class FloatDimension extends Dimension2D implements Serializable {

  /**
   * For serialization.
   */
  private static final long serialVersionUID = 5367882923248086744L;

  /**
   * The width.
   */
  private float width;

  /**
   * The height.
   */
  private float height;

  /**
   * Creates a new dimension object with width and height set to zero.
   */
  public FloatDimension() {
    this.width = 0.0f;
    this.height = 0.0f;
  }

  /**
   * Creates a new dimension that is a copy of another dimension.
   *
   * @param fd the dimension to copy.
   */
  public FloatDimension( final FloatDimension fd ) {
    this.width = fd.width;
    this.height = fd.height;
  }

  /**
   * Creates a new dimension.
   *
   * @param width  the width.
   * @param height the height.
   */
  public FloatDimension( final float width, final float height ) {
    this.width = width;
    this.height = height;
  }

  /**
   * Returns the width.
   *
   * @return the width.
   */
  public double getWidth() {
    return this.width;
  }

  /**
   * Returns the height.
   *
   * @return the height.
   */
  public double getHeight() {
    return this.height;
  }

  /**
   * Sets the width.
   *
   * @param width the width.
   */
  public void setWidth( final double width ) {
    this.width = (float) width;
  }

  /**
   * Sets the height.
   *
   * @param height the height.
   */
  public void setHeight( final double height ) {
    this.height = (float) height;
  }

  /**
   * Sets the size of this <code>Dimension</code> object to the specified width and height.  This method is included for
   * completeness, to parallel the {@link java.awt.Component#getSize() getSize} method of {@link java.awt.Component}.
   *
   * @param width  the new width for the <code>Dimension</code> object
   * @param height the new height for the <code>Dimension</code> object
   */
  public void setSize( final double width, final double height ) {
    setHeight( (float) height );
    setWidth( (float) width );
  }

  /**
   * Creates and returns a copy of this object.
   *
   * @return a clone of this instance.
   * @see Cloneable
   */
  public Object clone() {
    return super.clone();
  }

  /**
   * Returns a string representation of the object. In general, the <code>toString</code> method returns a string that
   * "textually represents" this object. The result should be a concise but informative representation that is easy for
   * a person to read.
   * <p/>
   *
   * @return a string representation of the object.
   */
  public String toString() {
    return getClass().getName() + ":={width=" + getWidth() + ", height="
      + getHeight() + '}';
  }

  /**
   * Tests this object for equality with another object.
   *
   * @param o the other object.
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean equals( final Object o ) {
    if ( this == o ) {
      return true;
    }
    if ( !( o instanceof FloatDimension ) ) {
      return false;
    }

    final FloatDimension floatDimension = (FloatDimension) o;

    //noinspection FloatingPointEquality
    if ( this.height != floatDimension.height ) {
      return false;
    }
    //noinspection FloatingPointEquality
    if ( this.width != floatDimension.width ) {
      return false;
    }

    return true;
  }

  /**
   * Returns a hash code.
   *
   * @return A hash code.
   */
  public int hashCode() {
    int result;
    result = Float.floatToIntBits( this.width );
    result = 29 * result + Float.floatToIntBits( this.height );
    return result;
  }
}
