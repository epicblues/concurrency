package epicblues.practice.concurrency.service;

import static org.assertj.core.api.Assertions.assertThat;

import epicblues.practice.concurrency.MultipleThreadExecutor;
import epicblues.practice.concurrency.domain.Stock;
import epicblues.practice.concurrency.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisLockServiceFacadeTest {

  @Autowired
  StockRepository stockRepository;

  @Autowired
  RedisLockServiceFacade stockServiceFacade;

  @AfterEach
  void cleanUp() {
    stockRepository.deleteAll();
  }

  @Test
  void 재고_감소_Redis_락() throws InterruptedException {

    // Given
    int numberOfThreads = 100;
    Stock stock = new Stock(5L, (long) numberOfThreads);
    stockRepository.save(stock);

    // When
    MultipleThreadExecutor.execute(() -> {
      try {
        stockServiceFacade.decrease(stock.getId(), 1L);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }, numberOfThreads);

    // Then
    assertThat(stockRepository.findById(stock.getId()).orElseThrow().getQuantity()).isZero();

  }

}
