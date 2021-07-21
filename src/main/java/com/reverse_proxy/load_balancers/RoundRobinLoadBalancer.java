/*
 * Implements the round-robin load balancer.
 */
package com.reverse_proxy.load_balancers;

import com.reverse_proxy.config_parsers.AddressHolder;
import com.reverse_proxy.config_parsers.DownstreamService;
import com.reverse_proxy.config_parsers.ReverseProxyConfig;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The load balancer that uses a round-robin algorithm to choose which downstream server will
 * process the client's request.
 */
public class RoundRobinLoadBalancer extends LoadBalancer {
  HashMap<DownstreamService, RepeatedCounter> counters;

  public RoundRobinLoadBalancer(ReverseProxyConfig reverseProxyConfig) {
    super(reverseProxyConfig);
    this.counters = new HashMap<>();

    for (DownstreamService service : reverseProxyConfig.getProxy().getServices()) {
      counters.put(service, new RepeatedCounter(service.getHosts().size()));
    }
  }

  @Override
  public AddressHolder balance(DownstreamService service) {
    return service.getHosts().get(counters.get(service).incrementAndGet() - 1);
  }

  /**
   * The counter that counts to the desired number then resets and counts to the same number again
   * (repeated counting).
   */
  static class RepeatedCounter {
    private final AtomicInteger counter;
    private final int maxCount;

    /**
     * Constructs the instance of the repeated counter.
     *
     * @param maxCount An upper bound for the repeated counter.
     */
    public RepeatedCounter(int maxCount) {
      this.counter = new AtomicInteger(0);
      this.maxCount = maxCount;
    }

    /**
     * Increment the current counter value and returns the new incremented value.
     *
     * @return The current counter value.
     */
    public int incrementAndGet() {
      return this.counter.accumulateAndGet(1, (prev, next) -> prev >= maxCount ? 1 : prev + next);
    }
  }
}
