package sd_009.bookstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sd_009.bookstore.entity.book.Series;

import java.util.Optional;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long>, JpaSpecificationExecutor<Series> {

    Page<Series> findByEnabled(Boolean enabled, Pageable pageable);

    Page<Series> findByNameContainingAndEnabled(String name, Boolean enabled, Pageable pageable);

    Optional<Series> findByEnabledAndId(Boolean enabled, Long id);

    Optional<Series> findByName(String name);
}