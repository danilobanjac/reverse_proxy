/*
 * Implements the server exception listener.
 */
package com.reverse_proxy.listeners;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import org.apache.hc.core5.http.ConnectionClosedException;
import org.apache.hc.core5.http.ExceptionListener;
import org.apache.hc.core5.http.HttpConnection;

public class ServerExceptionListener implements ExceptionListener {
  private static final String EXCEPTION_CLIENT_PROXY_OPEN = "[EXCEPTION] [CLIENT -> PROXY] [";
  private static final String SQUARE_CLOSED_SPACE_OPEN = "] [";
  private static final String SQUARE_CLOSED_SPACE = "] ";
  private static final String TRACEBACK_START = "; Traceback: ";

  @Override
  public void onError(final Exception exception) {
    StringWriter stringWriter = new StringWriter();

    try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
      printWriter
          .append(EXCEPTION_CLIENT_PROXY_OPEN)
          .append(new Timestamp(System.currentTimeMillis()).toString())
          .append(SQUARE_CLOSED_SPACE)
          .append(exception.getMessage());

      if (!(exception instanceof SocketException)) { // Add traceback
        printWriter.append(TRACEBACK_START);
        exception.printStackTrace(printWriter);
      }

      System.out.println(stringWriter);
    }
  }

  @Override
  public void onError(final HttpConnection connection, final Exception exception) {
    StringWriter stringWriter = new StringWriter();

    try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
      printWriter
          .append(EXCEPTION_CLIENT_PROXY_OPEN)
          .append(new Timestamp(System.currentTimeMillis()).toString())
          .append(SQUARE_CLOSED_SPACE_OPEN)
          .append(connection.getEndpointDetails().toString())
          .append(SQUARE_CLOSED_SPACE)
          .append(exception.getMessage());

      if (!(exception instanceof SocketTimeoutException
          || exception instanceof SocketException
          || exception instanceof ConnectionClosedException)) { // Add traceback
        printWriter.append(TRACEBACK_START);
        exception.printStackTrace(printWriter);
      }

      System.out.println(stringWriter);
    }
  }
}
