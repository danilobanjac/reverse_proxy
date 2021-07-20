# Simple Reverse Proxy

Simple reverse proxy with a classic (blocking) message transport. The reverse proxy is implemented using the `org.apache.httpcomponents` library and packaged with the `maven` package manager.

## Features
- It listens to HTTP requests and forwards them to one of the instances of a downstream service that will process the requests
- Requests are load-balanced and supports multiple load-balancing strategies (Random, Round-Robin)
- After processing the request, the downstream service sends the HTTP response back to the reverse proxy
- The reverse proxy forwards the response to the client making the initial request
- The reverse proxy is configured using the YAML specified below
- Implements an in-memory cache (Basic cache settings can be configured through YAML specified below)

## YAML configuration
```YAML
proxy:
  loadBalancingAlgorithm: ROUND_ROBIN
  listen:
    address: "localhost"
    port: 8080
  services:
    - name: some-name
      domain: some-name.com
      hosts:
        - address: "167.99.47.15"
          port: 8000
    - name: some-other-name
      domain: some-other-name.com
      hosts:
        - address: "178.62.228.77"
          port: 8090
  cache:
    maxCacheEntries: 1000
    maxObjectSize: 8192
```

## TODO's
- [ ] Add UNIT tests
- [ ] Modularize `ConfigValidator`. When something gets added or removed from the config class structure, validations need to be updated. It would be great if this scenario could be avoided, so that the user can add class, specify validations on the class (`@NotNull`, `@NotBlank` etc.), and be done with it. Maybe use the `visitor` pattern and `Java Reflection` to parse class structure and run the validators
- [ ] Implement more load-balancing strategies (`Weighted Round Robin`, `Least connections` etc.)
- [ ] Add the `@Alias` annotation. The annotation would allow that the `YAML` config names can differ from the Java class attribute names
- [ ] Implement asynchronous HTTP reverse proxy
