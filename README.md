# Simple Reverse Proxy

Simple reverse proxy with a classic (blocking) message transport. The reverse proxy is implemented using the `org.apache.httpcomponents` library.

# TODO's
- [ ] Add UNIT tests
- [ ] Modularize `ConfigValidator`. At the moment when something gets added or removed from the config class structure, validations need to be updated. It would be great if this scenario could be avoided, so that the user can just add class, specify validations on the class (`@NotNull`, `@NotBlank` etc.) and be done with it. Maybe use `visitor` pattern and `Java Reflection` to parse class structure and run the validators.
- [ ] Implement asynchronous HTTP reverse proxy
