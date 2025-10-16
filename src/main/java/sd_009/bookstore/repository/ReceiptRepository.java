package sd_009.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sd_009.bookstore.entity.receipt.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}