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
  private static final String SQUARE_CLOSED_SPACE_OPEN = "] [";
  private static final String SQUARE_CLOSED_SPACE = "] ";
  private static final String SQUARE_OPEN = "[";
  private static final String SPACE = " ";

  abstract String getOnRequestCommunicationDirection();

  abstract String getOnResponseCommunicationDirection();

  abstract String getOnExchangeCommunicationDirection();

  @Override
  public void onRequestHead(final HttpConnection connection, final HttpRequest request) {
    System.out.println(
        SQUARE_OPEN
            + this.getOnRequestCommunicationDirection()
            + SQUARE_CLOSED_SPACE_OPEN
            + new Timestamp(System.currentTimeMillis())
            + SQUARE_CLOSED_SPACE_OPEN
            + connection.getEndpointDetails()
            + SQUARE_CLOSED_SPACE
            + request.getMethod()
            + SPACE
            + request.getRequestUri());
  }

  @Override
  public void onResponseHead(final HttpConnection connection, final HttpResponse response) {
    System.out.println(
        SQUARE_OPEN
            + this.getOnResponseCommunicationDirection()
            + SQUARE_CLOSED_SPACE_OPEN
            + new Timestamp(System.currentTimeMillis())
            + SQUARE_CLOSED_SPACE_OPEN
            + connection.getEndpointDetails()
            + SQUARE_CLOSED_SPACE
            + response.getCode()
            + SPACE
            + response.getReasonPhrase());
  }

  @Override
  public void onExchangeComplete(final HttpConnection connection, final boolean keepAlive) {
    System.out.println(
        SQUARE_OPEN
            + this.getOnExchangeCommunicationDirection()
            + SQUARE_CLOSED_SPACE_OPEN
            + new Timestamp(System.currentTimeMillis())
            + SQUARE_CLOSED_SPACE_OPEN
            + connection.getEndpointDetails()
            + "] exchange completed; connection "
            + (keepAlive ? "kept alive" : "cannot be kept alive"));
  }
}
