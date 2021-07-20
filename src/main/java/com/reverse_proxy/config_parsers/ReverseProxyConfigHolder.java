/*
 * Implements the class which instance holds all configurations for the reverse reverse_proxy server.
 */
package com.reverse_proxy.config_parsers;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * Contains all reverse proxy configurations. The @Data generates all the boilerplate that is
 * normally associated with simple POJOs (Plain Old Java Objects) and beans: getters for all fields,
 * setters for all non-final fields, and appropriate toString, equals and hashCode implementations
 * that involve the fields of the class, and a constructor that initializes all final fields, as
 * well as all non-final fields with no initializer that have been marked with @NonNull, in order to
 * ensure the field is never null.
 */
@SuppressWarnings("unused")
@Data
public class ReverseProxyConfigHolder implements Configurable {
  @NotNull(message = "The 'listen' field cannot be null.")
  private AddressHolder listen;

  @NotEmpty(message = "The 'services' field cannot be empty.")
  private List<DownstreamService> services;

  private LoadBalancingAlgorithm loadBalancingAlgorithm;
  private Cache cache;

  public enum LoadBalancingAlgorithm {
    RANDOM,
    ROUND_ROBIN
  }
}
