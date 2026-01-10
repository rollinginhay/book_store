package sd_009.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.entity.cart.CartDetail;
import sd_009.bookstore.entity.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long>, JpaSpecificationExecutor<CartDetail> {

    // Lấy toàn bộ sản phẩm trong giỏ của 1 user
    List<CartDetail> findByUser(User user);

    // Tìm 1 dòng cụ thể theo user + bookDetail (để check trùng khi thêm)
    Optional<CartDetail> findByUserAndBookDetail(User user, BookDetail bookDetail);

    // Lấy giỏ hàng đang hoạt động (enabled = true)
    List<CartDetail> findByUserAndEnabled(User user, Boolean enabled);

    // Lấy toàn bộ cartDetail theo user + bookDetail + enabled (để check tồn kho theo giỏ)
    List<CartDetail> findByUserAndBookDetailAndEnabled(User user, BookDetail bookDetail, Boolean enabled);


}