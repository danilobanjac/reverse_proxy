/*
 * Handles the requests received by the reverse proxy server.
 */
package com.reverse_proxy;

import com.reverse_proxy.config_parsers.AddressHolder;
import com.reverse_proxy.config_parsers.DownstreamService;
import com.reverse_proxy.config_parsers.ReverseProxyConfig;
import com.reverse_proxy.config_parsers.ReverseProxyConfigHolder.LoadBalancingAlgorithm;
import com.reverse_proxy.load_balancers.LoadBalancer;
import com.reverse_proxy.load_balancers.RandomLoadBalancer;
import com.reverse_proxy.load_balancers.RoundRobinLoadBalancer;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import org.apache.hc.client5.http.cache.HttpCacheContext;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpMessage;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.hc.core5.http.io.HttpRequestHandler;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
import org.apache.hc.core5.http.protocol.HttpContext;

/** Implements the HTTP request handler. */
class ReverseProxyHandler implements HttpRequestHandler {

  public static final Set<String> HOP_BY_HOP_HEADERS =
      Set.of(
          HttpHeaders.HOST.toLowerCase(Locale.ROOT),
          HttpHeaders.CONTENT_LENGTH.toLowerCase(Locale.ROOT),
          HttpHeaders.TRANSFER_ENCODING.toLowerCase(Locale.ROOT),
          HttpHeaders.CONNECTION.toLowerCase(Locale.ROOT),
          HttpHeaders.KEEP_ALIVE.toLowerCase(Locale.ROOT),
          HttpHeaders.PROXY_AUTHENTICATE.toLowerCase(Locale.ROOT),
          HttpHeaders.TE.toLowerCase(Locale.ROOT),
          HttpHeaders.TRAILER.toLowerCase(Locale.ROOT),
          HttpHeaders.UPGRADE.toLowerCase(Locale.ROOT));

  private final CloseableHttpClient requester;
  private final LoadBalancer loadBalancer;
  private final HashMap<String, DownstreamService> downstreamServices;
  private HttpHost targetHost;

  public ReverseProxyHandler(
      final ReverseProxyConfig reverseProxyConfig,
      final CloseableHttpClient requester) {
    super();
    this.requester = requester;
    this.loadBalancer = getLoadBalancer(reverseProxyConfig);
    this.downstreamServices = new HashMap<>();

    for (DownstreamService service : reverseProxyConfig.getProxy().getServices()) {
      this.downstreamServices.put(service.getDomain().toLowerCase(Locale.ROOT), service);
    }
  }

  /**
   * Depending on the chosen load balancing algorithm,
   * return the appropriate instance of the load balancer.
   *
   * @param config Reverse proxy config fetched from the YAML config file.
   * @return The instance of the load balancer class.
   */
  private static LoadBalancer getLoadBalancer(ReverseProxyConfig config) {
    LoadBalancingAlgorithm algorithm = config.getProxy().getLoadBalancingAlgorithm();
    algorithm = algorithm == null ? LoadBalancingAlgorithm.RANDOM : algorithm;

    return switch (algorithm) {
      case RANDOM -> new RandomLoadBalancer(config);
      case ROUND_ROBIN -> new RoundRobinLoadBalancer(config);
    };
  }

  /**
   * Construct the address of the chosen downstream service.
   * @param hostHeader The host HTTP header.
   * @return The address of the downstream service containing its IP address and port.
   */
  public String getTargetHost(Header hostHeader) {
    String host = hostHeader.getValue().toLowerCase(Locale.ROOT);
    AddressHolder addressHolder =
        loadBalancer.balance(this.downstreamServices.get(host));

    return String.format("%s:%d", addressHolder.getAddress(), addressHolder.getPort());
  }

  /**
   * Copy the HTTP request headers from one object to another.
   * Hop by hop headers are not copied.
   * @param from Copy the HTTP headers from this HTTP message object.
   * @param to Copy the HTTP headers to this HTTP message object.
   */
  public void copyHeaders(HttpMessage from, HttpMessage to) {
    for (final Iterator<Header> iter = from.headerIterator(); iter.hasNext(); ) {
      final Header header = iter.next();
      if (!HOP_BY_HOP_HEADERS.contains(header.getName().toLowerCase(Locale.ROOT))) {
        to.addHeader(header);
      }
    }
  }

  @Override
  public void handle(
      final ClassicHttpRequest incomingRequest,
      final ClassicHttpResponse outgoingResponse,
      final HttpContext serverContext)
      throws IOException, ProtocolException {

    final HttpCacheContext clientContext = HttpCacheContext.create();
    Header hostHeader = incomingRequest.getHeader(HttpHeaders.HOST);

    try {
      targetHost = HttpHost.create(this.getTargetHost(hostHeader));
    } catch (URISyntaxException exception) {
      exception.printStackTrace();
    }

    final ClassicHttpRequest outgoingRequest =
        new BasicClassicHttpRequest(
            incomingRequest.getMethod(), targetHost, incomingRequest.getPath());

    this.copyHeaders(incomingRequest, outgoingRequest);

    outgoingRequest.setEntity(incomingRequest.getEntity());
    final ClassicHttpResponse incomingResponse =
        requester.execute(targetHost, outgoingRequest, clientContext);
    outgoingResponse.setCode(incomingResponse.getCode());

    this.copyHeaders(incomingResponse, outgoingResponse);

    outgoingResponse.setEntity(incomingResponse.getEntity());
  }
}
