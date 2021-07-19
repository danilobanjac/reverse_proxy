/*
 * Extends the abstract listener to implement the requester stream listener.
 */
package com.reverse_proxy.listeners;

public class RequesterStreamListener extends StreamListener {
  @Override
  String getOnRequestCommunicationDirection() {
    return "PROXY -> SERVER";
  }

  @Override
  String getOnResponseCommunicationDirection() {
    return "PROXY <- SERVER";
  }

  @Override
  String getOnExchangeCommunicationDirection() {
    return "PROXY <- SERVER";
  }
}
