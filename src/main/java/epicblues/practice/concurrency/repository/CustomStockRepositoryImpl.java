package epicblues.practice.concurrency.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import epicblues.practice.concurrency.domain.QStock;
import epicblues.practice.concurrency.domain.Stock;
import java.util.Optional;
import javax.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomStockRepositoryImpl implements CustomStockRepository {

  private static final QStock stock = QStock.stock;
  private final JPAQueryFactory queryFactory;

  @Override
  public void decrementCapacity(Long id, Long amount) {
    // 단점 : DB만으로 확인할 수 없는 무결성 조건을 제어하기 힘들다.
    queryFactory.update(stock)
        .set(stock.quantity, stock.quantity.subtract(amount))
        .where(stock.id.eq(id))
        .execute();

  }

  @Override
  public Optional<Stock> findByIdForUpdate(Long id) {
    return Optional.ofNullable(queryFactory.selectFrom(stock)
        .where(stock.id.eq(id))
        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
        .fetchOne());
  }
}
