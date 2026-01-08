package sd_009.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.entity.receipt.ReceiptHistory;

import java.util.List;

@Repository
public interface ReceiptHistoryRepository extends JpaRepository<ReceiptHistory, Long>, JpaSpecificationExecutor<ReceiptHistory> {

    List<ReceiptHistory> findByReceipt(Receipt receipt);
    
    List<ReceiptHistory> findByReceiptOrderByCreatedAtDesc(Receipt receipt);
}

