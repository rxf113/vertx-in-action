package chapter2.execblocking;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Offload extends AbstractVerticle {

  private final Logger logger = LoggerFactory.getLogger(Offload.class);

  @Override
  public void start() {
//    vertx.setPeriodic(5000, id -> {
//      logger.info("Tick");
//      vertx.executeBlocking(this::blockingCode, this::resultHandler);
//      System.out.println("fffff");
//    });
    vertx.setPeriodic(2000, id -> {
      Object name = vertx.getOrCreateContext().get("name");
      System.out.printf("\nname: %s",name);
    });

    vertx.executeBlocking(promise -> {
      try {
        Thread.sleep(4000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      vertx.getOrCreateContext().put("name","rxf113");
      promise.complete("123");
    },ar -> {
      System.out.println(ar.result());
    });
  }

  private void blockingCode(Promise<String> promise) {
    logger.info("Blocking code running");
    try {
      Thread.sleep(4000);
      logger.info("Done!");
      promise.complete("Ok!");
    } catch (InterruptedException e) {
      promise.fail(e);
    }
  }

  private void resultHandler(AsyncResult<String> ar) {
    if (ar.succeeded()) {
      logger.info("Blocking code result: {}", ar.result());
    } else {
      logger.error("Woops", ar.cause());
    }
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Offload());
  }
}
