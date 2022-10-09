package epicblues.practice.concurrency.service;

import epicblues.practice.concurrency.domain.Stock;
import epicblues.practice.concurrency.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

  private final StockRepository stockRepository;

  public StockService(StockRepository stockRepository) {
    this.stockRepository = stockRepository;

  }

  @Transactional
  public void decrease(Long id, Long quantity) {
    Stock stock = stockRepository.findByIdForUpdate(id).orElseThrow();
    stock.decrease(quantity);
    stockRepository.saveAndFlush(stock);
  }

}
