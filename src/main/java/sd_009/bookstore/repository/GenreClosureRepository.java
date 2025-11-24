package sd_009.bookstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sd_009.bookstore.entity.book.Genre;
import sd_009.bookstore.entity.book.GenreClosure;
import sd_009.bookstore.entity.book.GenreClosureId;

import java.util.List;

@Repository
public interface GenreClosureRepository extends JpaRepository<GenreClosure, GenreClosureId>, JpaSpecificationExecutor<GenreClosure> {

    // 🔹 Lấy danh sách các thể loại con (descendant) của 1 thể loại cha
    @Query("SELECT gc.descendant.id FROM GenreClosure gc WHERE gc.ancestor.id = :ancestorId")
    List<Long> findAllDescendantIds(@Param("ancestorId") Long ancestorId);

    // 🔹 Lấy danh sách các thể loại cha của 1 thể loại con
    @Query("SELECT gc.ancestor.id FROM GenreClosure gc WHERE gc.descendant.id = :descendantId")
    List<Long> findAllAncestorIds(@Param("descendantId") Long descendantId);

    // 🔹 Tìm theo độ sâu (depth = 0 là chính nó, 1 là cha–con, 2 là ông–cháu,…)
    Page<GenreClosure> findByDepth(Long depth, Pageable pageable);

    // 🔹 Kiểm tra tồn tại quan hệ cha–con
    boolean existsByAncestor_IdAndDescendant_Id(Long ancestorId, Long descendantId);

    @Query("""
    SELECT g
    FROM GenreClosure gc
    JOIN Genre g ON g.id = gc.descendant.id
    WHERE gc.ancestor.id = :genreId
      AND gc.depth > 0
""")
    List<Genre> findChildren(Long genreId);


}
