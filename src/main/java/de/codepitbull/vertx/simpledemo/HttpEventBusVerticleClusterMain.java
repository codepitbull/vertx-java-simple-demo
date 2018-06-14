package de.codepitbull.vertx.simpledemo;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class HttpEventBusVerticleClusterMain {
    public static void main(String[] args) {
        Vertx.clusteredVertx(new VertxOptions(), result -> {
            Vertx vertx = result.result();
            vertx.deployVerticle(HttpEventBusVerticle.class, new DeploymentOptions());
        });
    }
}
