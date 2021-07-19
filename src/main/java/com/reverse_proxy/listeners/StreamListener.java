/*
 * Implements the http1 requester stream listener and provides some boilerplate code.
 * When possible, every stream listener should extend from this abstract listener.
 */
package com.reverse_proxy.listeners;

import java.sql.Timestamp;
import org.apache.hc.core5.http.HttpConnection;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.impl.Http1StreamListener;

abstract class StreamListener implements Http1StreamListener {
  abstract String getOnRequestCommunicationDirection();

  abstract String getOnResponseCommunicationDirection();

  abstract String getOnExchangeCommunicationDirection();

  @Override
  public void onRequestHead(final HttpConnection connection, final HttpRequest request) {
    System.out.println(
        "["
            + this.getOnRequestCommunicationDirection()
            + "] ["
            + new Timestamp(System.currentTimeMillis())
            + "] ["
            + connection.getEndpointDetails()
            + "] "
            + request.getMethod()
            + " "
            + request.getRequestUri());
  }

  @Override
  public void onResponseHead(final HttpConnection connection, final HttpResponse response) {
    System.out.println(
        "["
            + this.getOnResponseCommunicationDirection()
            + "] ["
            + new Timestamp(System.currentTimeMillis())
            + "] ["
            + connection.getEndpointDetails()
            + "] "
            + response.getCode()
            + " "
            + response.getReasonPhrase());
  }

  @Override
  public void onExchangeComplete(final HttpConnection connection, final boolean keepAlive) {
    System.out.println(
        "["
            + this.getOnExchangeCommunicationDirection()
            + "] ["
            + new Timestamp(System.currentTimeMillis())
            + "] ["
            + connection.getEndpointDetails()
            + "] exchange completed; connection "
            + (keepAlive ? "kept alive" : "cannot be kept alive"));
  }
}
