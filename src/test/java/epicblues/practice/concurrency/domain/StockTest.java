package epicblues.practice.concurrency.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StockTest {

  @ParameterizedTest
  @CsvSource({"10,5,5", "1000000000,999999999,1"})
  @DisplayName("재고 감소를 정상적으로 수행한다.")
  void test(long quantity, long amount, long result) {

    // Given
    Stock stock = new Stock(1L, quantity);

    // When
    stock.decrease(amount);

    // Then(상태 기반)
    assertThat(stock.getQuantity()).isEqualTo(result);

  }

}
