package epicblues.practice.concurrency.repository;

import epicblues.practice.concurrency.domain.Stock;
import java.util.Optional;

public interface CustomStockRepository {

  void decrementCapacity(Long id, Long amount);

  Optional<Stock> findByIdForUpdate(Long id);

  Stock findByIdWithOptimisticLock(Long id);
}
