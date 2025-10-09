package sd_009.bookstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sd_009.bookstore.entity.book.Creator;

import java.util.Optional;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, Long>, JpaSpecificationExecutor<Creator> {

    Page<Creator> findByEnabled(Boolean enabled, Pageable pageable);

    Page<Creator> findByNameContainingAndEnabled(String name, Boolean enabled, Pageable pageable);

    Optional<Creator> findByEnabledAndId(Boolean enabled, Long id);

    Optional<Creator> findByName(String name);
}