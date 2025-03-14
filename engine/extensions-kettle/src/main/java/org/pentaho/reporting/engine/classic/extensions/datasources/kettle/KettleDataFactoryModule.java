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


package org.pentaho.reporting.engine.classic.extensions.datasources.kettle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.libraries.base.boot.AbstractModule;
import org.pentaho.reporting.libraries.base.boot.ModuleInitializeException;
import org.pentaho.reporting.libraries.base.boot.SubSystem;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

// Make sure you do NOT reference any Kettle classes here, or we cannot patch up the kettle-system properties.
public class KettleDataFactoryModule extends AbstractModule {
  public static final String NAMESPACE = "http://jfreereport.sourceforge.net/namespaces/datasources/kettle";
  public static final String TAG_DEF_PREFIX =
    "org.pentaho.reporting.engine.classic.extensions.datasources.kettle.tag-def.";
  private static final Log logger = LogFactory.getLog( KettleDataFactoryModule.class );

  public KettleDataFactoryModule() throws ModuleInitializeException {
    loadModuleInfo();
  }

  /**
   * Initializes the module. Use this method to perform all initial setup operations. This method is called only once in
   * a modules lifetime. If the initializing cannot be completed, throw a ModuleInitializeException to indicate the
   * error,. The module will not be available to the system.
   *
   * @param subSystem the subSystem.
   * @throws ModuleInitializeException if an error ocurred while initializing the module.
   */
  public void initialize( final SubSystem subSystem ) throws ModuleInitializeException {
    fixKettleLoggingAssumptions();
    fixKettlePluginSearchPath();

    performExternalInitialize( KettleDataFactoryModuleInitializer.class.getName(), getClass() );
  }

  private void fixKettlePluginSearchPath() {
    final String kettlePluginBaseFolders = System.getProperty( "DI_HOME" );
    if ( kettlePluginBaseFolders != null ) {
      return;
    }

    // find default directory.
    try {
      final ClassLoader classLoader = ObjectUtilities.getClassLoader( getClass() );
      final URL resource = classLoader.getResource( ".kettle-plugin-directory-marker" );
      if ( resource != null ) {
        try {
          final File f = new File( resource.toURI() );
          final File parentFile = f.getParentFile();
          if ( parentFile != null ) {
            final File pluginsParent = parentFile.getParentFile();
            if ( pluginsParent != null ) {
              final String absPath = pluginsParent.getAbsolutePath();
              System.setProperty( "DI_HOME", absPath );
            }
          }
        } catch ( URISyntaxException e ) {
          logger.debug( "Failed to add reporting plugin " + resource, e );
        }
      }
    } catch ( Throwable t ) {
      logger.debug( "Failed to adjust DI_HOME", t );
    }

    logger.debug( "DI_HOME=" + System.getProperty( "DI_HOME" ) );
  }

  private void fixKettleLoggingAssumptions() {
    try {
      if ( System.getProperty( "KETTLE_REDIRECT_STDOUT" ) == null ) {
        System.setProperty( "KETTLE_REDIRECT_STDOUT", "N" );
      }
      if ( System.getProperty( "KETTLE_REDIRECT_STDERR" ) == null ) {
        System.setProperty( "KETTLE_REDIRECT_STDERR", "N" );
      }
      if ( System.getProperty( "KETTLE_DISABLE_CONSOLE_LOGGING" ) == null ) {
        System.setProperty( "KETTLE_DISABLE_CONSOLE_LOGGING", "N" );
      }
    } catch ( SecurityException se ) {
      // ignore ..
    }
  }
}
