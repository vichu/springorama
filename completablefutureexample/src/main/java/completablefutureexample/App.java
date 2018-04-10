package completablefutureexample;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //Future example
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> future = executorService.submit(() -> {
            System.out.println("Executing a task.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new Random().nextInt(20);
        });

        while (!future.isDone()) {
            System.out.println("Task not yet done...");
            Thread.sleep(100);
        }
        System.out.println("The future is resolved. Value generated = " + future.get());
        executorService.shutdown();

    }
}
