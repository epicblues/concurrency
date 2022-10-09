package epicblues.practice.concurrency.repository;

import epicblues.practice.concurrency.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long>, CustomStockRepository {

}
