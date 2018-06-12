package de.codepitbull.vertx.simpledemo;

import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.file.FileSystem;
import java.util.concurrent.ThreadLocalRandom;

import static de.codepitbull.vertx.simpledemo.FileReaderVerticle.Tuple.tuple;

public class FileReaderVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        FileSystem fileSystem = vertx.fileSystem();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        vertx
            .eventBus()
            .consumer("enlighten.me")
            .toFlowable()
            .flatMap(msg ->
                    fileSystem
                        .rxReadFile("quotes/"+random.nextInt(1, 5 + 1) + ".txt")
                        .map(buffer -> tuple(msg, buffer))
                        .toFlowable()
            )
            .forEach(msgFileTuple -> {
                msgFileTuple.a.reply(msgFileTuple.b.toString() + " - ( " + hashCode() + ") ");
            });
    }

    // I HATE THIS
    static class Tuple<A,B>{
        final A a;
        final B b;

        Tuple(A a, B b) {
            this.a = a;
            this.b = b;
        }

        static<A,B> Tuple<A,B> tuple(A a, B b) {
            return new Tuple<>(a,b);
        }
    }
}