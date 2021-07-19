/*
 * Extends the abstract listener to implement the server stream listener.
 */
package com.reverse_proxy.listeners;

public class ServerStreamListener extends StreamListener {

  @Override
  String getOnRequestCommunicationDirection() {
    return "CLIENT -> PROXY";
  }

  @Override
  String getOnResponseCommunicationDirection() {
    return "CLIENT <- PROXY";
  }

  @Override
  String getOnExchangeCommunicationDirection() {
    return "CLIENT <- PROXY";
  }
}
