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


package org.pentaho.reporting.libraries.css.resolver.values;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.libraries.base.config.Configuration;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;
import org.pentaho.reporting.libraries.css.LibCssBoot;
import org.pentaho.reporting.libraries.css.dom.DocumentContext;
import org.pentaho.reporting.libraries.css.dom.LayoutElement;
import org.pentaho.reporting.libraries.css.keys.internal.InternalStyleKeys;
import org.pentaho.reporting.libraries.css.model.StyleKey;
import org.pentaho.reporting.libraries.css.model.StyleKeyRegistry;
import org.pentaho.reporting.libraries.css.values.CSSAutoValue;
import org.pentaho.reporting.libraries.css.values.CSSValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Creation-Date: 11.12.2005, 14:40:17
 *
 * @author Thomas Morgner
 */
public class ResolverFactory {
  private static final Log logger = LogFactory.getLog( ResolverFactory.class );
  private static final String AUTO_PREFIX = "org.jfree.layouting.resolver.auto.";
  private static final String COMPUTED_PREFIX = "org.jfree.layouting.resolver.computed.";
  private static final String PERCENTAGE_PREFIX = "org.jfree.layouting.resolver.percentages.";

  private static ResolverFactory factory;

  public static synchronized ResolverFactory getInstance() {
    if ( factory == null ) {
      factory = new ResolverFactory();
      factory.registerDefaults();
    }
    return factory;
  }

  private ResolveHandlerModule[] handlers;
  private StyleKeyRegistry registry;

  private ResolverFactory() {
    handlers = null;
    registry = StyleKeyRegistry.getRegistry();
  }

  public void registerDefaults() {
    final ArrayList handlerList = new ArrayList();

    final HashMap autoHandlers = loadModules( AUTO_PREFIX );
    final HashMap compHandlers = loadModules( COMPUTED_PREFIX );
    final HashMap percHandlers = loadModules( PERCENTAGE_PREFIX );
    final HashSet keys = new HashSet();
    keys.addAll( autoHandlers.keySet() );
    keys.addAll( compHandlers.keySet() );
    keys.addAll( percHandlers.keySet() );

    for ( Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
      final StyleKey key = (StyleKey) iterator.next();
      final ResolveHandler autoHandler = (ResolveHandler) autoHandlers.get( key );
      final ResolveHandler compHandler = (ResolveHandler) compHandlers.get( key );
      final ResolveHandler percHandler = (ResolveHandler) percHandlers.get( key );
      handlerList.add( new ResolveHandlerModule( key, autoHandler, compHandler,
        percHandler ) );
    }

    handlers = (ResolveHandlerModule[]) handlerList.toArray
      ( new ResolveHandlerModule[ handlerList.size() ] );
    handlers = ResolveHandlerSorter.sort( handlers );
    //    for (int i = 0; i < handlers.length; i++)
    //    {
    //      ResolveHandlerModule handler = handlers[i];
    //      Log.debug("Registered sorted handler (" + handler.getWeight() + ") " + handler.getKey());
    //
    //    }
    //    Log.debug("Registered " + handlers.length + " modules.");
  }

  private HashMap loadModules( final String configPrefix ) {
    final HashMap handlers = new HashMap();
    final Configuration config = LibCssBoot.getInstance().getGlobalConfig();
    final Iterator sit = config.findPropertyKeys( configPrefix );
    final int length = configPrefix.length();
    while ( sit.hasNext() ) {
      final String configkey = (String) sit.next();
      final String name = configkey.substring( length ).toLowerCase();
      final StyleKey key = registry.findKeyByName( name );
      if ( key == null ) {
        logger.warn( "Invalid stylekey for resolver: " + name );
        continue;
      }

      final String c = config.getConfigProperty( configkey );
      final ResolveHandler module = (ResolveHandler)
        ObjectUtilities.loadAndInstantiate( c, ResolverFactory.class, ResolveHandler.class );
      if ( module != null ) {
        //Log.info("Loaded resolver: " + name + " (" + module + ")");
        handlers.put( key, module );
      } else {
        logger.warn( "Invalid resolver implementation: " + c );
      }
    }
    return handlers;
  }

  public void performResolve( final DocumentContext process,
                              final LayoutElement node ) {
    node.getLayoutStyle().setValue( InternalStyleKeys.INTERNAL_CONTENT, new ContentSpecification() );

    for ( int i = 0; i < handlers.length; i++ ) {
      final ResolveHandlerModule handler = handlers[ i ];
      final StyleKey key = handler.getKey();
      final CSSValue value = node.getLayoutStyle().getValue( key );

      final ResolveHandler autoValueHandler = handler.getAutoValueHandler();
      if ( autoValueHandler != null ) {
        if ( value instanceof CSSAutoValue ) {
          autoValueHandler.resolve( process, node, key );
        }
      }

      final ResolveHandler compValueHandler = handler.getComputedValueHandler();
      if ( compValueHandler != null ) {
        compValueHandler.resolve( process, node, key );
      }

      final ResolveHandler percValueHandler = handler.getPercentagesValueHandler();
      if ( percValueHandler != null ) {
        percValueHandler.resolve( process, node, key );
      }
    }
  }
}
