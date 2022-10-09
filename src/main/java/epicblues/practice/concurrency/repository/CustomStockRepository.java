package epicblues.practice.concurrency.repository;

public interface CustomStockRepository {

  void decrementCapacity(Long id, Long amount);
}
