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


package org.pentaho.reporting.engine.classic.extensions.datasources.cda;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.methods.HttpGet;
import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;
import org.pentaho.reporting.engine.classic.core.util.HttpClientManager;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.libraries.base.util.StringUtils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;


/**
 * Class that implements CDA to be used over HTTP to be used in PRD application
 *
 * @author dduque
 */
public class HttpQueryBackend extends CdaQueryBackend {
  private static final char DOMAIN_SEPARATOR = '\\';

  private transient HttpClient client;
  private transient volatile HttpGet httpCall;

  public TypedTableModel fetchData( final DataRow dataRow,
                                    final String method,
                                    final Map<String, String> extraParameter ) throws ReportDataFactoryException {
    final String baseURL = getBaseUrl();
    if ( StringUtils.isEmpty( baseURL, true ) ) {
      throw new ReportDataFactoryException( "Base URL is null" );
    }
    final String url = createURL( method, extraParameter );
    try {
      final HttpGet httpCall = new HttpGet( url );
      this.httpCall = httpCall;
      final HttpClient client = getHttpClient();
      HttpResponse httpResponse = client.execute( httpCall );
      final int status = httpResponse.getStatusLine().getStatusCode();
      if ( status != HttpStatus.SC_OK ) {
        throw new ReportDataFactoryException( "Failed to retrieve data: " + httpResponse.getStatusLine() );
      }

      final InputStream responseBody = httpResponse.getEntity().getContent();
      return CdaResponseParser.performParse( responseBody );
    } catch ( UnsupportedEncodingException use ) {
      throw new ReportDataFactoryException( "Failed to encode parameter", use );
    } catch ( Exception e ) {
      throw new ReportDataFactoryException( "Failed to send request : " + url, e );
    } finally {
      httpCall = null;
    }
  }

  public void cancelRunningQuery() {
    final HttpGet method = this.httpCall;
    if ( method != null ) {
      method.abort();
    }
  }

  protected HttpClient getHttpClient() {
    if ( client == null ) {
      HttpClientManager.HttpClientBuilderFacade clientBuilder = HttpClientManager.getInstance().createBuilder();
      client = clientBuilder.setCredentials( getUsername(), getPassword() )
        .setCookieSpec( CookieSpecs.DEFAULT ).build();
    }
    return client;
  }

  public static Credentials getCredentials( final String user,
                                            final String password ) {
    if ( StringUtils.isEmpty( user ) ) {
      return null;
    }

    final int domainIdx = user.indexOf( DOMAIN_SEPARATOR );
    if ( domainIdx == -1 ) {
      return new UsernamePasswordCredentials( user, password );
    }
    try {
      final String domain = user.substring( 0, domainIdx );
      final String username = user.substring( domainIdx + 1 );
      final String host = InetAddress.getLocalHost().getHostName();
      return new NTCredentials( username, password, host, domain );
    } catch ( UnknownHostException uhe ) {
      return new UsernamePasswordCredentials( user, password );
    }
  }

}
