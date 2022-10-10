package epicblues.practice.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MultipleThreadExecutor {

  public static void execute(Runnable work, int numberOfWork) throws InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfWork);
    CountDownLatch latch = new CountDownLatch(numberOfWork);
    for (int i = 0; i < numberOfWork; i++) {
      executorService.submit(() -> {
        try {
          work.run();
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();
  }

}
