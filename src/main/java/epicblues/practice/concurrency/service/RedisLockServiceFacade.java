package epicblues.practice.concurrency.service;

import epicblues.practice.concurrency.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisLockServiceFacade {

  private final StockService stockService;

  private final RedisRepository redisLockRepository;

  public void decrease(Long id, Long amount) throws InterruptedException {
    while (!redisLockRepository.lock(id)) {
      Thread.sleep(50); // spinlock
    }

    stockService.decrease(id, amount);
    redisLockRepository.unlock(id);
  }
}
