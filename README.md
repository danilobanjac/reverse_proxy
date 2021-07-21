# Simple Reverse Proxy

Simple reverse proxy with a classic (blocking) message transport. The reverse proxy is implemented using the `org.apache.httpcomponents` library and packaged with the `maven` package manager.

## Features
- It listens to HTTP requests and forwards them to one of the instances of a downstream service that will process the requests
- Requests are load-balanced and supports multiple load-balancing strategies (Random, Round-Robin)
- After processing the request, the downstream service sends the HTTP response back to the reverse proxy
- The reverse proxy forwards the response to the client making the initial request
- The reverse proxy is configured using the YAML specified below
- Implements an in-memory cache (Cache settings can be configured through YAML specified below)
- Implements proper logging (at the moment everything gets printed into the stdout and stderr)

## SLIs (Service Level Indicators) and SLOs (Service Level Objectives)

| **Category**    | **SLI**                                                                                                                                                | **SLO**                                               | **Measure**                                                                                                                                                                                                                                                         |
|-----------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **HTTP Server** |                                                                                                                                                        |                                                       |                                                                                                                                                                                                                                                                     |
| Availability    | The proportion of successful HTTP requests, as measured from the reverse proxy logs. Any HTTP status other than 500–599 is considered successful.      | 99%                                                   | `count of "web" http_requests which do not have a 5XX status code divided by count of all "web" http_requests`                                                                                                                                                      |
| Latency         | The proportion of sufficiently fast HTTP requests, as measured from the reverse proxy logs. “Sufficiently fast” is defined as < 200 ms, or < 1,000 ms. | 90% of requests <= 200 ms 99% of requests <= 1,000 ms | `count of "web" http_requests with a duration less than or equal to "0.2" seconds divided by count of all "web" http_requests` - `count of "web" http_requests with  a duration less than or equal to  "1.0" seconds  divided by  count of all "web" http_requests` |
| Throughput      | The proportion of successful HTTP requests per second, as measured from the reverse proxy logs.                                                        | >= 400 requests per second                          |                                                                                                                                                                                                                                                                     |


The user can retrieve the information necessary to calculate the above-specified metrics (SLIs) from logs produced by `RequesterStreamListener`, `ServerExceptionListener`, `ServerStreamListener`.

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
- [ ] Extends listeners with a unique ID for each transaction
- [ ] Add option for user to specify the directory for the log files and store the output of the server there (stdout, stderr)
- [ ] Implement asynchronous HTTP reverse proxy
