/*
 * Implements the random load balancer.
 */
package com.reverse_proxy.load_balancers;

import com.reverse_proxy.config_parsers.AddressHolder;
import com.reverse_proxy.config_parsers.DownstreamService;
import com.reverse_proxy.config_parsers.ReverseProxyConfig;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;
import java.util.List;

/**
 * The load balancer that randomly chooses which downstream server will process the client's
 * request.
 */
public class RandomLoadBalancer extends LoadBalancer {
  private static final int FIRST_SERVICE = 0;
  private final XoRoShiRo128PlusRandom xoroRandom;

  public RandomLoadBalancer(ReverseProxyConfig reverseProxyConfig) {
    super(reverseProxyConfig);
    this.xoroRandom = new XoRoShiRo128PlusRandom();
  }

  @Override
  public AddressHolder balance(DownstreamService service) {
    List<AddressHolder> hosts = service.getHosts();
    int hostsSize = hosts.size();

    return hostsSize == 1 ? hosts.get(FIRST_SERVICE) : hosts.get(xoroRandom.nextInt(hostsSize));
  }
}
