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

  @Autowired
  StockServiceFacade stockServiceFacade;

  @AfterEach
  void cleanUp() {
    stockRepository.deleteAll();
  }

  @Test
  void test_decrease() {

    // Given
    Stock stock = new Stock(5L, 19L);
    stockRepository.save(stock);

    // When

    stockService.decrease(stock.getId(), 9L);

    // Then
    assertThat(stockRepository.findById(stock.getId()).orElseThrow().getQuantity()).isEqualTo(10L);

  }

  @Test
  void 모든_재고_감소_요청이_유효하게_적용된다() throws InterruptedException {

    // Given
    int numberOfThreads = 100;
    Stock stock = new Stock(5L, (long) numberOfThreads);
    stockRepository.save(stock);

    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
    CountDownLatch latch = new CountDownLatch(numberOfThreads);

    // When
    for (int i = 0; i < numberOfThreads; i++) {
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

  @Test
  @DisplayName("testName")
  void 재고_감소_낙관적_락() throws InterruptedException {

    // Given
    int numberOfThreads = 100;
    Stock stock = new Stock(5L, (long) numberOfThreads);
    stockRepository.save(stock);

    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
    CountDownLatch latch = new CountDownLatch(numberOfThreads);

    // When
    for (int i = 0; i < numberOfThreads; i++) {
      executorService.submit(() -> {
        try {
          stockServiceFacade.decrease(stock.getId(), 1L);
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
