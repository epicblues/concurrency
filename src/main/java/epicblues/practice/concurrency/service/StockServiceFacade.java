package epicblues.practice.concurrency.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockServiceFacade {

  private final OptimisticStockService optimisticStockService;

  public void decrease(Long id, Long amount) {
    
    try {
      optimisticStockService.decrease(id, amount);

    } catch (OptimisticLockingFailureException exception) {

      decrease(id, amount);
    }
  }

}
