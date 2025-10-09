package sd_009.bookstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sd_009.bookstore.entity.book.Publisher;

import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long>, JpaSpecificationExecutor<Publisher> {

    Page<Publisher> findByEnabled(Boolean enabled, Pageable pageable);

    Page<Publisher> findByNameContainingAndEnabled(String name, Boolean enabled, Pageable pageable);

    Optional<Publisher> findByEnabledAndId(Boolean enabled, Long id);

    Optional<Publisher> findByName(String name);
}