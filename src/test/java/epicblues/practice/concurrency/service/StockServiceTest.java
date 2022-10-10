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
    stockService.decreaseWithPessimisticLock(stock.getId(), 9L);

    // Then
    assertThat(stockRepository.findById(stock.getId()).orElseThrow().getQuantity()).isEqualTo(10L);

  }

  @Test
  void 재고_감소_비관적_락() throws InterruptedException {

    // Given
    int numberOfThreads = 100;
    Stock stock = new Stock(5L, (long) numberOfThreads);
    stockRepository.save(stock);

    MultipleThreadExecutor.execute(() -> {
      stockService.decreaseWithPessimisticLock(stock.getId(), 1L);
    }, numberOfThreads);

    // Then
    assertThat(stockRepository.findById(stock.getId()).orElseThrow().getQuantity()).isZero();
  }

  @Test
  void 재고_감소_낙관적_락() throws InterruptedException {

    // Given
    int numberOfThreads = 100;
    Stock stock = new Stock(5L, (long) numberOfThreads);
    stockRepository.save(stock);

    // When
    MultipleThreadExecutor.execute(() -> {
      stockServiceFacade.decrease(stock.getId(), 1L);
    }, numberOfThreads);

    // Then
    assertThat(stockRepository.findById(stock.getId()).orElseThrow().getQuantity()).isZero();

  }

}
