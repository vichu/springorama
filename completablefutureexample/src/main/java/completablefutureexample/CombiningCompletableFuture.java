package completablefutureexample;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CombiningCompletableFuture {

  public static void main(String[] args) {
    ForkJoinPool myThreadPool = new ForkJoinPool(10);

    CompletableFuture<Integer> myCompletableFutureInt = CompletableFuture.supplyAsync(() -> {
      try {
        int sleepTime = new Random().nextInt(2000);
        Thread.sleep(sleepTime);
        System.out.println(
            "Sleeping for " + sleepTime + " in myCompletableFutureInt on thread "
                + Thread.currentThread().getName());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return new Random().nextInt(50);
    }, myThreadPool);

    CompletableFuture<BigDecimal> myCompletableFutureBigDecimal = CompletableFuture
        .supplyAsync(() -> {
          try {
            int sleepTime = new Random().nextInt(1000);
            Thread.sleep(sleepTime);
            System.out.println(
                "Sleeping for " + sleepTime + " in myCompletableFutureBigDecimal on thread "
                    + Thread.currentThread().getName());
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          return new BigDecimal(new Random().nextDouble() * 50);
        }, myThreadPool);

    CompletableFuture<Long> myCompletableFutureLong = CompletableFuture.supplyAsync(() -> {
      try {
        int sleepTime = new Random().nextInt(1000);
        Thread.sleep(sleepTime);
        System.out.println(
            "Sleeping for " + sleepTime + " in myCompletableFutureLong on thread "
                + Thread.currentThread().getName());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return new Random().nextLong();
    }, myThreadPool);

    CompletableFuture<CompletableFuture<CompletableFuture<Map<String, Object>>>> result = myCompletableFutureBigDecimal
        .thenApply(bigDecimal ->
            myCompletableFutureInt.thenApply(integer ->
                myCompletableFutureLong.thenApply(aLong -> {
                  Map<String, Object> objectHashMap = new HashMap<>();
                  objectHashMap.put("IntegerValue", integer);
                  objectHashMap.put("LongValue", aLong);
                  objectHashMap.put("BigDecimalValue", bigDecimal);
                  return objectHashMap;
                })));

    try {
      Map<String, Object> re = result
          .get(2, TimeUnit.SECONDS)
          .get()
          .get();
      System.out.println("\n-------Combining using thenApply()-------");
      System.out.println(re);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      e.printStackTrace();
    }

    CompletableFuture<Map<String, Object>> myObjectCompletableFuture =
        myCompletableFutureBigDecimal.thenCompose(bigDecimalValue ->
            myCompletableFutureInt
                .thenCombine(myCompletableFutureLong,
                    ((integerValue, longValue) -> {
                      Map<String, Object> objectHashMap = new HashMap<>();
                      objectHashMap.put("IntegerValue", integerValue);
                      objectHashMap.put("LongValue", longValue);
                      objectHashMap.put("BigDecimalValue", bigDecimalValue);
                      return objectHashMap;
                    })));

    try {
      Map<String, Object> myObjectMap = myObjectCompletableFuture.get(2, TimeUnit.SECONDS);
      System.out.println("\n------Compose and combine CompletableFutures----");
      myObjectMap.entrySet().forEach(stringObjectEntry -> System.out
          .println(stringObjectEntry.getKey() + " = " + stringObjectEntry.getValue().toString()));
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      e.printStackTrace();
    }

  }

}
