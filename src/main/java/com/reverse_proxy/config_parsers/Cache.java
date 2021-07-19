/*
 * Implements the class which instance holds the reverse proxy cache configuration.
 */
package com.reverse_proxy.config_parsers;

@SuppressWarnings("unused")
public class Cache implements Configurable {
  private int maxCacheEntries;
  private int maxObjectSize;

  public int getMaxCacheEntries() {
    return maxCacheEntries;
  }

  public void setMaxCacheEntries(int maxCacheEntries) {
    this.maxCacheEntries = maxCacheEntries;
  }

  public int getMaxObjectSize() {
    return maxObjectSize;
  }

  public void setMaxObjectSize(int maxObjectSize) {
    this.maxObjectSize = maxObjectSize;
  }
}
