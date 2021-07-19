/*
 * Implements the class, which instance encapsulates
 * the class that holds all configurations for the reverse reverse_proxy.
 */
package com.reverse_proxy.config_parsers;

import javax.validation.constraints.NotNull;

public class ReverseProxyConfig implements Configurable {
  @NotNull(message = "'proxy' cannot be null")
  private ReverseProxyConfigHolder proxy;

  public ReverseProxyConfigHolder getProxy() {
    return proxy;
  }

  public void setProxy(ReverseProxyConfigHolder proxy) {
    this.proxy = proxy;
  }
}
