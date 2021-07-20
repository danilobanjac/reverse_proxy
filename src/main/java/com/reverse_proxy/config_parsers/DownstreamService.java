/*
 * Implements the class which instance holds the configurations for some downstream service.
 */
package com.reverse_proxy.config_parsers;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Contains the name, domain, and hosts of some downstream service. The @Data generates all the
 * boilerplate that is normally associated with simple POJOs (Plain Old Java Objects) and beans:
 * getters for all fields, setters for all non-final fields, and appropriate toString, equals and
 * hashCode implementations that involve the fields of the class, and a constructor that initializes
 * all final fields, as well as all non-final fields with no initializer that have been marked
 * with @NonNull, in order to ensure the field is never null.
 */
@SuppressWarnings("unused")
@Data
public class DownstreamService implements Configurable {
  @NotBlank(message = "The service 'name' field cannot be blank.")
  private String name;

  @NotBlank(message = "The service 'domain' field cannot be blank.")
  private String domain;

  @NotEmpty(message = "The service 'hosts' field cannot be empty.")
  private List<AddressHolder> hosts;
}
