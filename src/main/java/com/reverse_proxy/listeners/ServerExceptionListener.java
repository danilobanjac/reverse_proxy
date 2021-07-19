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

  @Override
  public void onError(final Exception exception) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);

    printWriter
        .append("[EXCEPTION] [CLIENT -> PROXY] [")
        .append(new Timestamp(System.currentTimeMillis()).toString())
        .append("] ")
        .append(exception.getMessage());

    if (!(exception instanceof SocketException)) { // Add traceback
      printWriter.append("; Traceback: ");
      exception.printStackTrace(printWriter);
    }

    System.out.println(stringWriter);
  }

  @Override
  public void onError(final HttpConnection connection, final Exception exception) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);

    printWriter
        .append("[EXCEPTION] [CLIENT -> PROXY] [")
        .append(new Timestamp(System.currentTimeMillis()).toString())
        .append("] [")
        .append(connection.getEndpointDetails().toString())
        .append("] ")
        .append(exception.getMessage());

    if (!(exception instanceof SocketTimeoutException
        || exception instanceof SocketException
        || exception instanceof ConnectionClosedException)) { // Add traceback
      printWriter.append("; Traceback: ");
      exception.printStackTrace(printWriter);
    }

    System.out.println(stringWriter);
  }
}
