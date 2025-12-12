package sd_009.bookstore.util.mapper.cart;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailDto;
import sd_009.bookstore.dto.jsonApiResource.cart.CartDetailOwningDto;
import sd_009.bookstore.dto.jsonApiResource.user.UserDto;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.entity.cart.CartDetail;
import sd_009.bookstore.entity.user.User;
import sd_009.bookstore.util.mapper.book.BookDetailMapper;
import sd_009.bookstore.util.mapper.user.UserMapper;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class CartDetailOwningMapperImpl implements CartDetailOwningMapper {

    @Autowired
    private BookDetailMapper bookDetailMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public CartDetail toEntity(CartDetailOwningDto dto) {
        if ( dto == null ) {
            return null;
        }

        CartDetail.CartDetailBuilder cartDetail = CartDetail.builder();

        if ( dto.getId() != null ) {
            cartDetail.id( Long.parseLong( dto.getId() ) );
        }
        cartDetail.user( userMapper.toEntity( dto.getUser() ) );
        cartDetail.bookDetail( bookDetailMapper.toEntity( dto.getBookDetail() ) );
        if ( dto.getAmount() != null ) {
            cartDetail.amount( dto.getAmount().longValue() );
        }

        return cartDetail.build();
    }

    @Override
    public CartDetailOwningDto toDto(CartDetail entity) {
        if ( entity == null ) {
            return null;
        }

        String id = null;
        Double amount = null;
        Boolean enabled = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        UserDto user = null;
        BookDetailDto bookDetail = null;

        if ( entity.getId() != null ) {
            id = String.valueOf( entity.getId() );
        }
        if ( entity.getAmount() != null ) {
            amount = entity.getAmount().doubleValue();
        }
        enabled = entity.getEnabled();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();
        user = userMapper.toDto( entity.getUser() );
        bookDetail = bookDetailMapper.toDto( entity.getBookDetail() );

        Integer quantity = null;

        CartDetailOwningDto cartDetailOwningDto = new CartDetailOwningDto( id, quantity, amount, enabled, createdAt, updatedAt, user, bookDetail );

        return cartDetailOwningDto;
    }

    @Override
    public CartDetail partialUpdate(CartDetailOwningDto dto, CartDetail entity) {
        if ( dto == null ) {
            return entity;
        }

        if ( dto.getCreatedAt() != null ) {
            entity.setCreatedAt( dto.getCreatedAt() );
        }
        if ( dto.getUpdatedAt() != null ) {
            entity.setUpdatedAt( dto.getUpdatedAt() );
        }
        if ( dto.getEnabled() != null ) {
            entity.setEnabled( dto.getEnabled() );
        }
        if ( dto.getId() != null ) {
            entity.setId( Long.parseLong( dto.getId() ) );
        }
        if ( dto.getUser() != null ) {
            if ( entity.getUser() == null ) {
                entity.setUser( User.builder().build() );
            }
            userMapper.partialUpdate( dto.getUser(), entity.getUser() );
        }
        if ( dto.getBookDetail() != null ) {
            if ( entity.getBookDetail() == null ) {
                entity.setBookDetail( BookDetail.builder().build() );
            }
            bookDetailMapper.partialUpdate( dto.getBookDetail(), entity.getBookDetail() );
        }
        if ( dto.getAmount() != null ) {
            entity.setAmount( dto.getAmount().longValue() );
        }

        return entity;
    }
}
