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


package org.pentaho.reporting.engine.classic.extensions.toc;

import junit.framework.TestCase;

public class IndexUtilityTest extends TestCase {
  public IndexUtilityTest() {
  }

  public IndexUtilityTest( final String name ) {
    super( name );
  }

  public void testCondensedText() {
    assertEquals( "", IndexUtility.getCondensedIndexText( new Integer[ 0 ], "," ) );
    assertEquals( "1-3,3-4", IndexUtility.getCondensedIndexText( new Integer[] { 1, 2, 3, 3, 4 }, "," ) );
    assertEquals( "1-4", IndexUtility.getCondensedIndexText( new Integer[] { 1, 2, 3, 4 }, "," ) );
    assertEquals( "1,2,4-6,8", IndexUtility.getCondensedIndexText( new Integer[] { 1, 2, 4, 5, 6, 8 }, "," ) );
    assertEquals( "1,2,4-8", IndexUtility.getCondensedIndexText( new Integer[] { 1, 2, 4, 5, 6, 7, 8 }, "," ) );

  }
}
