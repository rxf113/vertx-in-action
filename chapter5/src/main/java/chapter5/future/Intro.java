package chapter5.future;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Intro {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    Promise<String> promise = Promise.promise();

//    System.out.println("Waiting...");
//    vertx.setTimer(5000, id -> {
//      if (System.currentTimeMillis() % 2L == 0L) {
//        promise.complete("Ok!");
//      } else {
//        promise.fail(new RuntimeException("Bad luck..."));
//      }
//    });
//
//    Future<String> future = promise.future();
//    future
//      .onSuccess(System.out::println)
//      .onFailure(err -> System.out.println(err.getMessage()));
//
//    promise.future()
//      .recover(err -> Future.succeededFuture("Let's say it's ok!"))
//      .map(String::toUpperCase)
//      .flatMap(str -> {
//        Promise<String> next = Promise.promise();
//        vertx.setTimer(3000, id -> next.complete(">>> " + str));
//        return next.future();
//      })
//      .onSuccess(System.out::println);
//
//    CompletionStage<String> cs = promise.future().toCompletionStage();
//    cs
//      .thenApply(String::toUpperCase)
//      .thenApply(str -> "~~~ " + str)
//      .whenComplete((str, err) -> {
//        if (err == null) {
//          System.out.println(str);
//        } else {
//          System.out.println("Oh... " + err.getMessage());
//        }
//      });

    promise.complete("123");

    vertx.setTimer(1000, id -> {

      CompletionStage<String> completionStage = promise.future().toCompletionStage();
      completionStage
        .thenApplyAsync(res -> {
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          System.out.println(Thread.currentThread().getId());
          return res.concat(" 1");
        }).thenApplyAsync(res -> {
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          System.out.println(Thread.currentThread().getId());
          return res.concat(" 2");
        }).thenApplyAsync(res -> {
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          System.out.println(Thread.currentThread().getId());
          return res.concat(" 3");
        })
        .whenCompleteAsync((res, err) -> {
          System.out.println(res);
        });
    });

   /* CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return "5 seconds have elapsed";
    });

    Future
      .fromCompletionStage(cf, vertx.getOrCreateContext())
      .onSuccess(System.out::println)
      .onFailure(Throwable::printStackTrace);*/

    vertx.setTimer(1000, id -> {
      CompletableFuture<Object> cf = CompletableFuture.supplyAsync(() -> {
          try {
            Thread.sleep(5000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          return "5 seconds have elapsed";
        })
        .thenApplyAsync(res -> res.concat(" thenApplyAsync"))
        .thenApplyAsync(res -> res.concat(" rxf113"))
        .thenApplyAsync(res -> {
          System.out.println("123");
          return res.concat(" rxf113");
        });
      Future
        .fromCompletionStage(cf, vertx.getOrCreateContext())
        .onSuccess(System.out::println)
        .onFailure(Throwable::printStackTrace);
    });
  }
}
