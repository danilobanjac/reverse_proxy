/*
 * Implements the validator for the reverse proxy YAML configuration file.
 */

package com.reverse_proxy.validators;

import com.reverse_proxy.config_parsers.AddressHolder;
import com.reverse_proxy.config_parsers.Configurable;
import com.reverse_proxy.config_parsers.DownstreamService;
import com.reverse_proxy.config_parsers.ReverseProxyConfig;
import com.reverse_proxy.config_parsers.ReverseProxyConfigHolder;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ConfigValidator {
  private final ReverseProxyConfig reverseProxyConfig;

  /**
   * Create the instance of the config validator.
   * Usage: new ConfigValidator(reverseProxyConfig).validate();
   *
   * @param reverseProxyConfig The reverse proxy main configuration object.
   */
  public ConfigValidator(ReverseProxyConfig reverseProxyConfig) {
    this.reverseProxyConfig = reverseProxyConfig;
  }

  /**
   * Validate the single instance of some configurable object.
   *
   * @param configurable Instance of some configurable object.
   */
  private void validate(Configurable configurable) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    Set<ConstraintViolation<Configurable>> violationMessages = validator.validate(configurable);

    if (!violationMessages.isEmpty()) {
      throw new IllegalStateException(
          violationMessages.stream()
              .map(ConstraintViolation::getMessage)
              .collect(Collectors.joining(", ")));
    }
  }

  /** Validate the reverse proxy configurations loaded from the YAML configuration file. */
  public void validate() {
    Objects.requireNonNull(
        this.reverseProxyConfig, "The reverse proxy config file cannot be empty.");

    validate(this.reverseProxyConfig);

    ReverseProxyConfigHolder reverseProxyConfigHolder = this.reverseProxyConfig.getProxy();

    validate(reverseProxyConfigHolder);
    validate(reverseProxyConfigHolder.getListen());

    for (DownstreamService service : reverseProxyConfigHolder.getServices()) {
      validate(service);

      for (AddressHolder host : service.getHosts()) {
        validate(host);
      }
    }
  }
}
