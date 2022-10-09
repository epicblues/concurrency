package epicblues.practice.concurrency.service;

import epicblues.practice.concurrency.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

  private final StockRepository stockRepository;

  public StockService(StockRepository stockRepository) {
    this.stockRepository = stockRepository;

  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void decrease(Long id, Long quantity) {

    stockRepository.decrementCapacity(id, quantity);

  }
}
