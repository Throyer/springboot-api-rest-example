package com.github.throyer.example.api.infra.middlewares;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;

import java.io.IOException;

import org.springframework.stereotype.Component;

import io.micrometer.tracing.CurrentTraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TraceMiddleware implements Filter {
  private static final String TRACE_ID_HEADER_NAME = "traceparent";
  private static final String DEFAULT = "00";

  private final Tracer tracer;

  @Override
  public void doFilter(
    ServletRequest request,
    ServletResponse servletResponse,
    FilterChain chain
  ) throws IOException, ServletException {

    HttpServletResponse response = (HttpServletResponse) servletResponse;

    if (hasNoTraceHeader(response)) {
      if(traceIsEmpty(tracer)) {
        chain.doFilter(request, servletResponse);
        return;
      }

      response.setHeader(TRACE_ID_HEADER_NAME, header(tracer));
    }

    chain.doFilter(request, servletResponse);
  }

  public static String header(Tracer tracer) {
    var context = tracer.currentTraceContext().context();
    var traceId = requireNonNull(context).traceId();
    var parentId = context.spanId();
    return format("%s-%s-%s-%s", DEFAULT, traceId, parentId, DEFAULT);
  }

  public static Boolean hasNoTraceHeader(HttpServletResponse response) {
    return !response.getHeaderNames().contains(TRACE_ID_HEADER_NAME);
  }

  public static Boolean traceIsEmpty(Tracer tracer) {
    return of(tracer)
      .map(Tracer::currentTraceContext)
      .map(CurrentTraceContext::context)
      .isEmpty();
  }
}
