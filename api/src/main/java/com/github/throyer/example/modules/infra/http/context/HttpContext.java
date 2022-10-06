package com.github.throyer.example.modules.infra.http.context;

import com.github.throyer.example.modules.authentication.services.PublicRoutes;

public class HttpContext {
  private static final PublicRoutes publicRoutes = new PublicRoutes();

  public static PublicRoutes publicRoutes() {
    return HttpContext.publicRoutes;
  }
}
