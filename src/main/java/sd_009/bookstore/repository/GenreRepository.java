package sd_009.bookstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sd_009.bookstore.entity.book.Genre;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long>, JpaSpecificationExecutor<Genre> {


    Page<Genre> findByEnabled(Boolean enabled, Pageable pageable);

    Page<Genre> findByNameContainingAndEnabled(String name, Boolean enabled, Pageable pageable);

    Optional<Genre> findByEnabledAndId(Boolean enabled, Long id);

    Page<Genre> findByEnabledAndNameContaining(Boolean enabled, String name, Pageable pageable);

    Optional<Genre> findByName(String name);
}