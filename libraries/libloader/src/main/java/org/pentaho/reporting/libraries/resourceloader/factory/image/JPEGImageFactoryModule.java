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


package org.pentaho.reporting.libraries.resourceloader.factory.image;

import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceData;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.pentaho.reporting.libraries.resourceloader.SimpleResource;
import org.pentaho.reporting.libraries.resourceloader.factory.AbstractFactoryModule;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Creation-Date: 05.04.2006, 17:35:12
 *
 * @author Thomas Morgner
 */
public class JPEGImageFactoryModule extends AbstractFactoryModule {
  private static final int[] FINGERPRINT_1 = { 0xFF, 0xD8 };

  private static final String[] MIMETYPES =
    {
      "image/jpeg",
      "image/jpg",
      "image/jp_",
      "application/jpg",
      "application/x-jpg",
      "image/pjpeg",
      "image/pipeg",
      "image/vnd.swiftview-jpeg",
      "image/x-xbitmap"
    };

  private static final String[] FILEEXTENSIONS =
    {
      ".jpg", ".jpeg"
    };
  private static final int[] EMPTY_ARRAY = new int[ 0 ];

  public JPEGImageFactoryModule() {
  }

  public int getHeaderFingerprintSize() {
    // indicate that we cannot allow the generic fingerprinting. We need to test it by ourselfs.
    return -1;
  }

  protected boolean canHandleResourceByContent( final InputStream data )
    throws IOException {
    final int[] fingerprint = FINGERPRINT_1;
    for ( int i = 0; i < fingerprint.length; i++ ) {
      if ( fingerprint[ i ] != data.read() ) {
        return false;
      }
    }

    if ( data.read() == -1 ) {
      return false;
    }
    if ( data.read() == -1 ) {
      return false;
    }

    //    fingerprint = FINGERPRINT_2;
    //    for (int i = 0; i < fingerprint.length; i++)
    //    {
    //      if (fingerprint[i] != data.read())
    //      {
    //        return false;
    //      }
    //    }
    return true;
  }

  protected int[] getFingerPrint() {
    return EMPTY_ARRAY;
  }

  protected String[] getMimeTypes() {
    return (String[]) JPEGImageFactoryModule.MIMETYPES.clone();
  }

  protected String[] getFileExtensions() {
    return (String[]) JPEGImageFactoryModule.FILEEXTENSIONS.clone();
  }

  public Resource create( final ResourceManager caller,
                          final ResourceData data,
                          final ResourceKey context )
    throws ResourceLoadingException {
    final long version = data.getVersion( caller );
    final Image image =
      Toolkit.getDefaultToolkit().createImage( data.getResource( caller ) );
    return new SimpleResource( data.getKey(), image, Image.class, version );
  }
}
