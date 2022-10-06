package com.github.throyer.example.modules.infra.environments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RateLimitEnvironments {
  @Autowired
  public RateLimitEnvironments(
    @Value("${bucket4j.filters[0].rate-limits[0].bandwidths[0].capacity}"
  ) Integer maxRequestsPerMinute) {
    RateLimitEnvironments.MAX_REQUESTS_PER_MINUTE = maxRequestsPerMinute;
  }
  public static Integer MAX_REQUESTS_PER_MINUTE;
}
