package epicblues.practice.concurrency.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import epicblues.practice.concurrency.domain.QStock;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomStockRepositoryImpl implements CustomStockRepository {

  private static final QStock stock = QStock.stock;
  private final JPAQueryFactory queryFactory;

  @Override
  public void decrementCapacity(Long id, Long amount) {
    queryFactory.update(stock)
        .set(stock.quantity, stock.quantity.subtract(amount))
        .where(stock.id.eq(id))
        .execute();

  }
}
