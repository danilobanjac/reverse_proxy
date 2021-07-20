/*
 * Implements the class which instance holds the reverse proxy cache configuration.
 */
package com.reverse_proxy.config_parsers;

import lombok.Data;

/**
 * Contains the reverse proxy cache configuration. The @Data generates all the boilerplate that is
 * normally associated with simple POJOs (Plain Old Java Objects) and beans: getters for all fields,
 * setters for all non-final fields, and appropriate toString, equals and hashCode implementations
 * that involve the fields of the class, and a constructor that initializes all final fields, as
 * well as all non-final fields with no initializer that have been marked with @NonNull, in order to
 * ensure the field is never null.
 */
@SuppressWarnings("unused")
@Data
public class Cache implements Configurable {
  private int maxCacheEntries;
  private int maxObjectSize;
}
