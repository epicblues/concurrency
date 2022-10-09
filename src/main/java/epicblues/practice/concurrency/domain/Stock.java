package epicblues.practice.concurrency.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Stock {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long productId;

  private Long quantity;

  public Stock(Long productId, Long quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }

  public Stock() {
  }

  public void decrease(long amount) {
    if (amount > quantity) {
      throw new RuntimeException("amount exceeded");
    }

    this.quantity = this.quantity - amount;
  }

}
