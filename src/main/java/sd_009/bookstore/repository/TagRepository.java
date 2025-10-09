package sd_009.bookstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sd_009.bookstore.entity.book.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

    Page<Tag> findByEnabled(Boolean enabled, Pageable pageable);

    Page<Tag> findByNameContainingAndEnabled(String name, Boolean enabled, Pageable pageable);
}