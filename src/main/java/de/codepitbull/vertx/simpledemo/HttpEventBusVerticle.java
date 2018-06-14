package de.codepitbull.vertx.simpledemo;

import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.eventbus.MessageProducer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
import io.vertx.reactivex.ext.web.handler.sockjs.SockJSHandler;

import java.util.ArrayList;
import java.util.List;

public class HttpEventBusVerticle extends AbstractVerticle {

    public static final int PORT = 8666;

    public List<Integer> dummeListe = new ArrayList<>();

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route("/simple/static/*").handler(StaticHandler.create("webroot-simple"));
        router.route("/simple/eventbus/*").handler(sockJsHandler());
        router.get("/simple/world").handler(this::hellWorld);

        vertx.createHttpServer().requestHandler(router::accept).listen(PORT);

        vertx
            .periodicStream(4000)
            .handler(elapsedTime ->
                    vertx
                            .eventBus()
                            .<Buffer>rxSend("enlighten.me", true)
                            .toMaybe()
                            .subscribe(
                                enlightenment -> vertx.eventBus().send("example-browser", enlightenment.body()),
                                throwable -> vertx.eventBus().send("example-browser", "No enlightenmnet today: "+throwable.getMessage()),
                                () -> System.out.println("Completed"))
            );

        vertx.eventBus().consumer("example-server")
                .handler(a -> System.out.println((a.body())));

        System.out.println(this.getClass().getSimpleName() + "on port " + PORT);
    }

    public SockJSHandler sockJsHandler() {
        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        BridgeOptions options = new BridgeOptions()
            .addOutboundPermitted(new PermittedOptions().setAddress("example-browser"))
            .addInboundPermitted(new PermittedOptions().setAddress("example-server"));
        sockJSHandler.bridge(options);
        return sockJSHandler;
    }

    public void hellWorld(RoutingContext r) {
        r.response().end("Hello World !!!");
    }
}
