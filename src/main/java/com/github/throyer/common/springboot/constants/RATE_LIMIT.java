package com.github.throyer.common.springboot.constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RATE_LIMIT {

    @Autowired
    public RATE_LIMIT(
        @Value("${bucket4j.filters[0].rate-limits[0].bandwidths[0].capacity}") Integer maxRequestsPerMinute
    ) {
        RATE_LIMIT.MAX_REQUESTS_PER_MINUTE = maxRequestsPerMinute;
    }

    public static Integer MAX_REQUESTS_PER_MINUTE;
}
