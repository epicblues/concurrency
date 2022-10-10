package epicblues.practice.concurrency.service;

import epicblues.practice.concurrency.domain.Stock;
import epicblues.practice.concurrency.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OptimisticStockService {

  private final StockRepository stockRepository;

  @Transactional
  public void decrease(Long id, Long amount) {

    Stock stock = stockRepository.findByIdWithOptimisticLock(id);
    stock.decrease(amount);
  }
}
