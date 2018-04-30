package completablefutureexample;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class App {

  public static void main(String[] args) {

    CompletableFuture<Integer> completableFuture = new CompletableFuture<>();

    //Define your flow
    completableFuture.thenApply((integer) -> {
      int randomNumber = 0;
      try {
        randomNumber = generateRandomNumber();
        System.out.println("Adding " + integer + " to the random number " + randomNumber);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(
          "Generated random number=" + randomNumber + " from thread : " + Thread.currentThread()
              .getName());
      return randomNumber + integer;
    }).thenApplyAsync(integer -> {
      System.out.println("Got result from the first thread.");
      return integer + 500;
    }, new ForkJoinPool()).thenAccept(integer -> {
      System.out.println("Added 500 to the result. Terminating the completable future.");
    });

    //Start the processing
    completableFuture.complete(10);

  }

  private static int generateRandomNumber() throws InterruptedException {
    int number = new Random().nextInt(100);
    System.out.println("Sleeping for " + number + "ms");
    Thread.sleep(number);
    return number;
  }
}
