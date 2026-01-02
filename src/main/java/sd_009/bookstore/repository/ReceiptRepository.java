package sd_009.bookstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import sd_009.bookstore.entity.receipt.Receipt;

import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Page<Receipt> findByEnabled(Boolean enabled, Pageable pageable);

    @EntityGraph(attributePaths = {
            "receiptDetails",
            "receiptDetails.bookCopy",
            "receiptDetails.bookCopy.book",
            "paymentDetail"
    })
    Optional<Receipt> findWithDetailsById(Long id);
}