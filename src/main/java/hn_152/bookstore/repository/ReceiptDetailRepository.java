package hn_152.bookstore.repository;

import hn_152.bookstore.model.entity.receipt.ReceiptDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Long>, JpaSpecificationExecutor<ReceiptDetail> {

}