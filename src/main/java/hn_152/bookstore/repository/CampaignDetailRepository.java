package hn_152.bookstore.repository;

import hn_152.bookstore.model.entity.campaign.CampaignDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignDetailRepository extends JpaRepository<CampaignDetail, Long>, JpaSpecificationExecutor<CampaignDetail> {

}