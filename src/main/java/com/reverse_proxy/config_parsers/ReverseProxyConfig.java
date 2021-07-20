/*
 * Implements the class, which instance encapsulates
 * the class that holds all configurations for the reverse reverse_proxy.
 */
package com.reverse_proxy.config_parsers;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * Contains the instance which has all reverse proxy configurations. The @Data generates all the
 * boilerplate that is normally associated with simple POJOs (Plain Old Java Objects) and beans:
 * getters for all fields, setters for all non-final fields, and appropriate toString, equals and
 * hashCode implementations that involve the fields of the class, and a constructor that initializes
 * all final fields, as well as all non-final fields with no initializer that have been marked
 * with @NonNull, in order to ensure the field is never null.
 */
@Data
public class ReverseProxyConfig implements Configurable {
  @NotNull(message = "The 'proxy' field cannot be null.")
  private ReverseProxyConfigHolder proxy;
}
