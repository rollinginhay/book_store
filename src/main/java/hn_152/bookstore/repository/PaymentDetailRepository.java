package hn_152.bookstore.repository;

import hn_152.bookstore.entity.receipt.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long>, JpaSpecificationExecutor<PaymentDetail> {

}