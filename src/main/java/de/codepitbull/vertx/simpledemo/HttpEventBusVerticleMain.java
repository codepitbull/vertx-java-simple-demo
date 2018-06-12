package de.codepitbull.vertx.simpledemo;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class HttpEventBusVerticleMain {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(HttpEventBusVerticle.class, new DeploymentOptions());
    }
}
