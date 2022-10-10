package epicblues.practice.concurrency.service;

import epicblues.practice.concurrency.domain.Stock;
import epicblues.practice.concurrency.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

  private final StockRepository stockRepository;

  @Transactional
  public void decreaseWithPessimisticLock(Long id, Long quantity) {
    Stock stock = stockRepository.findByIdForUpdate(id).orElseThrow();
    stock.decrease(quantity);
  }

  @Transactional
  public void decrease(Long id, Long quantity) {
    Stock stock = stockRepository.findById(id).orElseThrow();
    stock.decrease(quantity);

  }

}
