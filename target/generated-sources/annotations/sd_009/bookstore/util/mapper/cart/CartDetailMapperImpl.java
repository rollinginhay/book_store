package sd_009.bookstore.util.mapper.cart;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.cart.CartDetailDto;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.entity.cart.CartDetail;
import sd_009.bookstore.entity.user.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class CartDetailMapperImpl implements CartDetailMapper {

    @Override
    public CartDetail toEntity(CartDetailDto dto) {
        if ( dto == null ) {
            return null;
        }

        CartDetail.CartDetailBuilder cartDetail = CartDetail.builder();

        cartDetail.user( cartDetailDtoToUser( dto ) );
        cartDetail.bookDetail( cartDetailDtoToBookDetail( dto ) );
        if ( dto.getId() != null ) {
            cartDetail.id( Long.parseLong( dto.getId() ) );
        }
        cartDetail.amount( dto.getAmount() );

        return cartDetail.build();
    }

    @Override
    public CartDetailDto toDto(CartDetail entity) {
        if ( entity == null ) {
            return null;
        }

        String userId = null;
        String bookDetailId = null;
        String id = null;
        Long amount = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Boolean enabled = null;
        String note = null;

        Long id1 = entityUserId( entity );
        if ( id1 != null ) {
            userId = String.valueOf( id1 );
        }
        Long id2 = entityBookDetailId( entity );
        if ( id2 != null ) {
            bookDetailId = String.valueOf( id2 );
        }
        if ( entity.getId() != null ) {
            id = String.valueOf( entity.getId() );
        }
        amount = entity.getAmount();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();
        enabled = entity.getEnabled();
        note = entity.getNote();

        CartDetailDto cartDetailDto = new CartDetailDto( id, userId, bookDetailId, amount, createdAt, updatedAt, enabled, note );

        return cartDetailDto;
    }

    @Override
    public CartDetail partialUpdate(CartDetailDto dto, CartDetail entity) {
        if ( dto == null ) {
            return entity;
        }

        if ( entity.getUser() == null ) {
            entity.setUser( User.builder().build() );
        }
        cartDetailDtoToUser1( dto, entity.getUser() );
        if ( entity.getBookDetail() == null ) {
            entity.setBookDetail( BookDetail.builder().build() );
        }
        cartDetailDtoToBookDetail1( dto, entity.getBookDetail() );
        if ( dto.getCreatedAt() != null ) {
            entity.setCreatedAt( dto.getCreatedAt() );
        }
        if ( dto.getUpdatedAt() != null ) {
            entity.setUpdatedAt( dto.getUpdatedAt() );
        }
        if ( dto.getEnabled() != null ) {
            entity.setEnabled( dto.getEnabled() );
        }
        if ( dto.getNote() != null ) {
            entity.setNote( dto.getNote() );
        }
        if ( dto.getId() != null ) {
            entity.setId( Long.parseLong( dto.getId() ) );
        }
        if ( dto.getAmount() != null ) {
            entity.setAmount( dto.getAmount() );
        }

        return entity;
    }

    protected User cartDetailDtoToUser(CartDetailDto cartDetailDto) {
        if ( cartDetailDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        if ( cartDetailDto.getUserId() != null ) {
            user.id( Long.parseLong( cartDetailDto.getUserId() ) );
        }

        return user.build();
    }

    protected BookDetail cartDetailDtoToBookDetail(CartDetailDto cartDetailDto) {
        if ( cartDetailDto == null ) {
            return null;
        }

        BookDetail.BookDetailBuilder bookDetail = BookDetail.builder();

        if ( cartDetailDto.getBookDetailId() != null ) {
            bookDetail.id( Long.parseLong( cartDetailDto.getBookDetailId() ) );
        }

        return bookDetail.build();
    }

    private Long entityUserId(CartDetail cartDetail) {
        User user = cartDetail.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private Long entityBookDetailId(CartDetail cartDetail) {
        BookDetail bookDetail = cartDetail.getBookDetail();
        if ( bookDetail == null ) {
            return null;
        }
        return bookDetail.getId();
    }

    protected void cartDetailDtoToUser1(CartDetailDto cartDetailDto, User mappingTarget) {
        if ( cartDetailDto == null ) {
            return;
        }

        if ( cartDetailDto.getUserId() != null ) {
            mappingTarget.setId( Long.parseLong( cartDetailDto.getUserId() ) );
        }
    }

    protected void cartDetailDtoToBookDetail1(CartDetailDto cartDetailDto, BookDetail mappingTarget) {
        if ( cartDetailDto == null ) {
            return;
        }

        if ( cartDetailDto.getBookDetailId() != null ) {
            mappingTarget.setId( Long.parseLong( cartDetailDto.getBookDetailId() ) );
        }
    }
}
