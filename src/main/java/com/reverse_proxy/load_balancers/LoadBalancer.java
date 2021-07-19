/*
 * Implements the abstract base class that every load balancer class must extend from.
 */
package com.reverse_proxy.load_balancers;

import com.reverse_proxy.config_parsers.AddressHolder;
import com.reverse_proxy.config_parsers.DownstreamService;
import com.reverse_proxy.config_parsers.ReverseProxyConfig;

/** The abstract base class that every load balancer class must extend from. */
public abstract class LoadBalancer {
  public final ReverseProxyConfig reverseProxyConfig;

  /**
   * Default constructor that the subclass must initialize.
   *
   * @param reverseProxyConfig An instance that holds the reverse_proxy configuration loaded from the YAML config
   *     file.
   */
  protected LoadBalancer(ReverseProxyConfig reverseProxyConfig) {
    this.reverseProxyConfig = reverseProxyConfig;
  }

  /**
   * Using the specific load-balancing algorithm, decide which downstream server will handle the
   * client's request.
   *
   * @param service An instance of the downstream service loaded from the YAML config file.
   * @return Chosen downstream server that will process the request.
   */
  public abstract AddressHolder balance(DownstreamService service);
}
