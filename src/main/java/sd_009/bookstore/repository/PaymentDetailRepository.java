package sd_009.bookstore.repository;

import sd_009.bookstore.entity.receipt.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long>, JpaSpecificationExecutor<PaymentDetail> {

}