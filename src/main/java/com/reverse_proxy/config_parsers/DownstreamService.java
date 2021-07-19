/*
 * Implements the class which instance holds the configurations for some downstream service.
 */
package com.reverse_proxy.config_parsers;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@SuppressWarnings("unused")
public class DownstreamService implements Configurable {
  @NotBlank(message = "service 'name' cannot be blank")
  private String name;

  @NotBlank(message = "service 'domain' cannot be blank")
  private String domain;

  @NotEmpty(message = "service 'hosts' cannot be empty")
  private List<AddressHolder> hosts;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public List<AddressHolder> getHosts() {
    return hosts;
  }

  public void setHosts(List<AddressHolder> hosts) {
    this.hosts = hosts;
  }
}
