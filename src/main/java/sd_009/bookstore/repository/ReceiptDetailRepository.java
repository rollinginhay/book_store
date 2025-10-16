package sd_009.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.entity.receipt.ReceiptDetail;

import java.util.List;

@Repository
public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Long>, JpaSpecificationExecutor<ReceiptDetail> {

    List<ReceiptDetail> findByReceipt(Receipt receipt);
}