package sd_009.bookstore.repository;

import sd_009.bookstore.entity.receipt.ReceiptDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Long>, JpaSpecificationExecutor<ReceiptDetail> {

}