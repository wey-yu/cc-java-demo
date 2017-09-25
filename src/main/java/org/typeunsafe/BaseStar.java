package org.typeunsafe;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.Arrays;
import java.util.Optional;

public class BaseStar extends AbstractVerticle {


  public void start() {

    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    router.get("/api/raiders").handler((RoutingContext context) -> {

      if(context.failed()) {
        System.out.println(Arrays.toString(context.failure().getStackTrace()));
      }


      context.response()
        .putHeader("content-type", "application/json;charset=UTF-8")
        .end(new JsonObject().put("message", "hello").encodePrettily());
    });

    // serve static assets, see /resources/webroot directory
    router.route("/*").handler(StaticHandler.create());

    Integer httpPort = Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8080"));

    HttpServer server = vertx.createHttpServer();

    server.requestHandler(router::accept).listen(httpPort, result -> {
      if(result.succeeded()) {
        System.out.println("🌍 Listening on " + httpPort);
      } else {
        System.out.println("😡 Houston, we have a problem: " + result.cause().getMessage());
      }
    });
  }
}
