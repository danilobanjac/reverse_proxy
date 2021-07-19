/*
 * Implements the class which instance holds the address and port of some downstream server.
 */
package com.reverse_proxy.config_parsers;

import javax.validation.constraints.NotEmpty;

@SuppressWarnings("unused")
public class AddressHolder implements Configurable {
  @NotEmpty(message = "'address' cannot be empty")
  private String address;

  private int port;

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }
}
