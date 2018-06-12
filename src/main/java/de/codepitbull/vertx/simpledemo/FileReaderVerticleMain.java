package de.codepitbull.vertx.simpledemo;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class FileReaderVerticleMain {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(FileReaderVerticle.class, new DeploymentOptions());
    }
}
