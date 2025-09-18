package sd_009.bookstore.repository;

import sd_009.bookstore.entity.book.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, Long>, JpaSpecificationExecutor<Creator> {

}