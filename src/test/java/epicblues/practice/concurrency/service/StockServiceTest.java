package epicblues.practice.concurrency.service;

import static org.assertj.core.api.Assertions.assertThat;

import epicblues.practice.concurrency.domain.Stock;
import epicblues.practice.concurrency.repository.StockRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockServiceTest {

  @Autowired
  StockRepository stockRepository;

  @Autowired
  StockService stockService;

  @AfterEach
  void cleanUp() {
    stockRepository.deleteAll();
  }

  @Test
  @DisplayName("testName")
  void test_decrease() {

    // Given
    Stock stock = new Stock(5L, 19L);
    stock = stockRepository.save(stock);

    // When

    stockService.decrease(1L, 9L);

    // Then
    assertThat(stockRepository.findById(1L).orElseThrow().getQuantity()).isEqualTo(10L);

  }

  @Test
  void 스레드_100개_요청() throws InterruptedException {

    // Given
    int threadNum = 100;
    Stock stock = new Stock(5L, (long) threadNum);
    stockRepository.save(stock);

    ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
    CountDownLatch latch = new CountDownLatch(threadNum);

    // When
    for (int i = 0; i < threadNum; i++) {
      executorService.submit(() -> {
        try {
          stockService.decrease(stock.getId(), 1L);
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();
    // Then
    assertThat(stockRepository.findById(stock.getId()).orElseThrow().getQuantity()).isZero();
  }

}
