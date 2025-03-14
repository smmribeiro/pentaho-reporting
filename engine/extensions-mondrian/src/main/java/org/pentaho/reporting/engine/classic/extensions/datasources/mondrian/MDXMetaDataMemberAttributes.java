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


package org.pentaho.reporting.engine.classic.extensions.datasources.mondrian;

import mondrian.olap.Member;
import org.pentaho.reporting.engine.classic.core.wizard.ConceptQueryMapper;
import org.pentaho.reporting.engine.classic.core.wizard.DataAttributeContext;
import org.pentaho.reporting.engine.classic.core.wizard.DataAttributes;
import org.pentaho.reporting.engine.classic.core.wizard.DefaultConceptQueryMapper;
import org.pentaho.reporting.libraries.base.util.StringUtils;

/**
 * Todo: Document Me
 *
 * @author Thomas Morgner
 */
public class MDXMetaDataMemberAttributes implements DataAttributes {
  private static final String[] NAMESPACES = new String[] { MDXMetaAttributeNames.NAMESPACE };
  private static final String[] PROPERTIES = new String[]
    {
      MDXMetaAttributeNames.BACKGROUND_COLOR,
      MDXMetaAttributeNames.FOREGROUND_COLOR,

      MDXMetaAttributeNames.FONT_FLAGS,
      MDXMetaAttributeNames.FONTSIZE,

      MDXMetaAttributeNames.FONTNAME,
      MDXMetaAttributeNames.FORMAT_STRING,
      MDXMetaAttributeNames.LANGUAGE,

      MDXMetaAttributeNames.MDX_ALL_MEMBER,
      MDXMetaAttributeNames.MDX_CAPTION,
      MDXMetaAttributeNames.MDX_DESCRIPTION,
      MDXMetaAttributeNames.MDX_CALCULATED,
      MDXMetaAttributeNames.MDX_HIDDEN,
    };

  private DataAttributes backend;
  private Member cell;

  public MDXMetaDataMemberAttributes( final DataAttributes backend,
                                      final Member cell ) {
    if ( cell == null ) {
      throw new NullPointerException();
    }
    if ( backend == null ) {
      throw new NullPointerException();
    }
    this.cell = cell;
    this.backend = backend;
  }

  public String[] getMetaAttributeDomains() {
    final String[] backendDomains = backend.getMetaAttributeDomains();
    return StringUtils.merge( NAMESPACES, backendDomains );
  }

  public String[] getMetaAttributeNames( final String domainName ) {
    if ( MDXMetaAttributeNames.NAMESPACE.equals( domainName ) ) {
      return PROPERTIES.clone();
    }

    return backend.getMetaAttributeNames( domainName );
  }

  public Object getMetaAttribute( final String domain, final String name, final Class type,
                                  final DataAttributeContext context ) {
    return getMetaAttribute( domain, name, type, context, null );
  }

  public Object getMetaAttribute( final String domain,
                                  final String name,
                                  final Class type,
                                  final DataAttributeContext context,
                                  final Object defaultValue ) {

    if ( MDXMetaAttributeNames.NAMESPACE.equals( domain ) ) {
      if ( name.equals( MDXMetaAttributeNames.MDX_ALL_MEMBER ) ) {
        return Boolean.valueOf( cell.isAll() );
      }
      if ( name.equals( MDXMetaAttributeNames.MDX_CALCULATED ) ) {
        return Boolean.valueOf( cell.isCalculated() );
      }
      if ( name.equals( MDXMetaAttributeNames.MDX_HIDDEN ) ) {
        return Boolean.valueOf( cell.isHidden() );
      }
      if ( name.equals( MDXMetaAttributeNames.MDX_CAPTION ) ) {
        return String.valueOf( cell.getCaption() );
      }
      if ( name.equals( MDXMetaAttributeNames.MDX_DESCRIPTION ) ) {
        return String.valueOf( cell.getDescription() );
      }

      final Object attribute = cell.getPropertyValue( name );
      if ( attribute == null ) {
        return defaultValue;
      }

      return attribute;
    }

    return backend.getMetaAttribute( domain, name, type, context, defaultValue );
  }

  public Object clone() throws CloneNotSupportedException {
    final MDXMetaDataMemberAttributes attributes = (MDXMetaDataMemberAttributes) super.clone();
    attributes.backend = (DataAttributes) backend.clone();
    return attributes;
  }

  public ConceptQueryMapper getMetaAttributeMapper( String domain, String name ) {
    return DefaultConceptQueryMapper.INSTANCE;
  }
}
