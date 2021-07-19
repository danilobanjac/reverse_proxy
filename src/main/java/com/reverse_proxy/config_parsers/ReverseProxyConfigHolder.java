/*
 * Implements the class which instance holds all configurations for the reverse reverse_proxy server.
 */
package com.reverse_proxy.config_parsers;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public class ReverseProxyConfigHolder implements Configurable {
  @NotNull(message = "'listen' cannot be null")
  private AddressHolder listen;

  @NotEmpty(message = "'services' cannot be empty")
  private List<DownstreamService> services;

  private LoadBalancingAlgorithm loadBalancingAlgorithm;
  private Cache cache;

  public Cache getCache() {
    return cache;
  }

  public void setCache(Cache cache) {
    this.cache = cache;
  }

  public AddressHolder getListen() {
    return listen;
  }

  public void setListen(AddressHolder listen) {
    this.listen = listen;
  }

  public List<DownstreamService> getServices() {
    return services;
  }

  public void setServices(List<DownstreamService> services) {
    this.services = services;
  }

  public LoadBalancingAlgorithm getLoadBalancingAlgorithm() {
    return loadBalancingAlgorithm;
  }

  public void setLoadBalancingAlgorithm(LoadBalancingAlgorithm loadBalancingAlgorithm) {
    this.loadBalancingAlgorithm = loadBalancingAlgorithm;
  }

  public enum LoadBalancingAlgorithm {
    RANDOM,
    ROUND_ROBIN
  }
}
