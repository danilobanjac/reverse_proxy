/*
 * Implements the reverse proxy server.
 */
package com.reverse_proxy;

import com.reverse_proxy.config_parsers.Cache;
import com.reverse_proxy.config_parsers.DownstreamService;
import com.reverse_proxy.config_parsers.ReverseProxyConfig;
import com.reverse_proxy.listeners.RequesterStreamListener;
import com.reverse_proxy.listeners.ServerExceptionListener;
import com.reverse_proxy.listeners.ServerStreamListener;
import com.reverse_proxy.validators.ConfigValidator;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.cache.CacheConfig;
import org.apache.hc.client5.http.impl.cache.CachingHttpClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
import org.apache.hc.core5.http.impl.bootstrap.HttpServer;
import org.apache.hc.core5.http.impl.bootstrap.ServerBootstrap;
import org.apache.hc.core5.http.impl.io.HttpRequestExecutor;
import org.apache.hc.core5.util.TimeValue;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/** Implements the reverse proxy server. */
public class ReverseProxy {

  private static final int FILE_PATH = 0;

  /**
   * Retrieve the reverse proxy YAML configuration file.
   *
   * @param filePath The path to the YAML file.
   * @return The instance of the reverse proxy config object.
   * @throws IOException The file can not be found.
   */
  public static ReverseProxyConfig getReverseProxyConfig(String filePath) throws IOException {
    try (InputStream inputStream = new FileInputStream(filePath)) {
      Yaml yaml = new Yaml(new Constructor(ReverseProxyConfig.class));
      return yaml.load(inputStream);
    }
  }

  /**
   * Get the reverse proxy cache configuration.
   *
   * @param reverseProxyConfig The reverse proxy main config object.
   * @return The instance of the apache cache config object.
   */
  public static CacheConfig getCacheConfig(ReverseProxyConfig reverseProxyConfig) {
    int maxCacheEntries = 0;
    int maxObjectSize = 0;
    Cache proxyCacheConfig = reverseProxyConfig.getProxy().getCache();

    if (proxyCacheConfig != null) {
      if (proxyCacheConfig.getMaxCacheEntries() > 0) {
        maxCacheEntries = proxyCacheConfig.getMaxCacheEntries();
      }
      if (proxyCacheConfig.getMaxObjectSize() > 0) {
        maxObjectSize = proxyCacheConfig.getMaxObjectSize();
      }
    }

    return CacheConfig.custom()
        .setMaxCacheEntries(maxCacheEntries)
        .setMaxObjectSize(maxObjectSize)
        .build();
  }

  public static void main(final String[] args) throws Exception {
    String filePath;

    if (args.length == 1) {
      filePath = args[FILE_PATH];
    } else {
      throw new IllegalStateException(
          "The path to the YAML configuration file must be provided as an argument.");
    }

    ReverseProxyConfig reverseProxyConfig = getReverseProxyConfig(filePath);
    new ConfigValidator(reverseProxyConfig).validate();

    List<String> virtualHosts =
        reverseProxyConfig.getProxy().getServices().stream()
            .map(DownstreamService::getDomain)
            .collect(Collectors.toList());

    HttpRequestExecutor requestExecutor =
        new HttpRequestExecutor(
            HttpRequestExecutor.DEFAULT_WAIT_FOR_CONTINUE,
            DefaultConnectionReuseStrategy.INSTANCE,
            new RequesterStreamListener());

    // Create the instance of the http client builder and set the appropriate listeners
    final HttpClientBuilder requesterBuilder =
        CachingHttpClients.custom()
            .setCacheConfig(getCacheConfig(reverseProxyConfig))
            .setDefaultRequestConfig(RequestConfig.custom().build())
            .setRequestExecutor(requestExecutor);

    // Create the instance of the server and set the appropriate listeners
    final ServerBootstrap serverBootstrap =
        ServerBootstrap.bootstrap()
            .setLocalAddress(
                InetAddress.getByName(reverseProxyConfig.getProxy().getListen().getAddress()))
            .setListenerPort(reverseProxyConfig.getProxy().getListen().getPort())
            .setStreamListener(new ServerStreamListener())
            .setExceptionListener(new ServerExceptionListener());

    try (final CloseableHttpClient requester = requesterBuilder.build()) {
      // Register all domains that the server will recognize
      for (String virtualHost : virtualHosts) {
        serverBootstrap.registerVirtual(
            virtualHost, "*", new ReverseProxyHandler(reverseProxyConfig, requester));
      }

      try (final HttpServer server = serverBootstrap.create()) {
        server.start();
        server.awaitTermination(TimeValue.MAX_VALUE);
      }
    }
  }
}
