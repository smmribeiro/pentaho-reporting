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


package org.pentaho.reporting.libraries.base.versioning;

/**
 * An data-structure documenting external dependencies. Use this in your ProjectInformation implementation to tell users
 * what other libraries you use to give them proper credit.
 *
 * @author : Thomas Morgner
 */
public class DependencyInformation {
  /**
   * The name of the library.
   */
  private String name;
  /**
   * The version of the library, if known.
   */
  private String version;
  /**
   * The license the library is distributed under.
   */
  private String licenseName;
  /**
   * Some more information, liek a web-site or comment.
   */
  private String info;

  /**
   * Creates a minimal dependency information object for the library with the given name.
   *
   * @param name the name of the library, never null.
   */
  public DependencyInformation( final String name ) {
    if ( name == null ) {
      throw new NullPointerException( "Name must be given" );
    }
    this.name = name;
  }

  /**
   * Creates a minimal dependency information object for the library with the given name. All properties but the name
   * are optional.
   *
   * @param name        the name of the library, never null.
   * @param version     The version of the library, if known.
   * @param licenseName The license the library is distributed under.
   * @param info        Some more information, liek a web-site or comment.
   */
  public DependencyInformation( final String name,
                                final String version,
                                final String licenseName,
                                final String info ) {
    this( name );
    this.version = version;
    this.licenseName = licenseName;
    this.info = info;
  }

  /**
   * Redefines the version of the dependency.
   *
   * @param version the version.
   */
  protected void setVersion( final String version ) {
    this.version = version;
  }

  /**
   * Redefines the license name of the dependency.
   *
   * @param licenseName the name of the license.
   */
  protected void setLicenseName( final String licenseName ) {
    this.licenseName = licenseName;
  }

  /**
   * Redefines the extra information of the dependency.
   *
   * @param info the version.
   */
  protected void setInfo( final String info ) {
    this.info = info;
  }

  /**
   * Returns the name of the dependency, which is never null.
   *
   * @return the name.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the version number.
   *
   * @return the version information, or null if no version information is known.
   */
  public String getVersion() {
    return version;
  }

  /**
   * Returns the name of the license of this dependency.
   *
   * @return the license name.
   */
  public String getLicenseName() {
    return licenseName;
  }

  /**
   * Returns the extra information.
   *
   * @return the text information, or null if no extra information is known.
   */
  public String getInfo() {
    return info;
  }

  /**
   * Tests this object for equality. The object is equal if the name matches, the extra information is ignored.
   *
   * @param o the other object.
   * @return true, if the dependency information given denotes the same library as this dependency information.
   */
  public boolean equals( final Object o ) {
    if ( this == o ) {
      return true;
    }
    if ( !( o instanceof DependencyInformation ) ) {
      return false;
    }

    final DependencyInformation that = (DependencyInformation) o;

    if ( !name.equals( that.name ) ) {
      return false;
    }

    return true;
  }

  /**
   * Computes a hashcode based on the name of the dependency.
   *
   * @return the hashcode.
   */
  public int hashCode() {
    return name.hashCode();
  }
}
