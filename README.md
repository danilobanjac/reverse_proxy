# Simple Reverse Proxy

Simple reverse proxy with a classic (blocking) message transport. The reverse proxy is implemented using the `org.apache.httpcomponents` library and packaged with the `maven` package manager.

# TODO's
- [ ] Add UNIT tests
- [ ] Modularize `ConfigValidator`. When something gets added or removed from the config class structure, validations need to be updated. It would be great if this scenario could be avoided, so that the user can add class, specify validations on the class (`@NotNull`, `@NotBlank` etc.), and be done with it. Maybe use the `visitor` pattern and `Java Reflection` to parse class structure and run the validators
- [ ] Add the `@Alias` annotation. The annotation would allow that the `YAML` config names can differ from the Java class attribute names
- [ ] Implement asynchronous HTTP reverse proxy
