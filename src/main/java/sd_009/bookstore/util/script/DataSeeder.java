package sd_009.bookstore.util.script;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.entity.book.*;
import sd_009.bookstore.entity.receipt.PaymentDetail;
import sd_009.bookstore.entity.receipt.PaymentType;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.entity.receipt.ReceiptDetail;
import sd_009.bookstore.repository.BookDetailRepository;
import sd_009.bookstore.repository.BookRepository;
import sd_009.bookstore.repository.GenreRepository;
import sd_009.bookstore.repository.ReceiptRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(2)
public class DataSeeder {
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final BookDetailRepository bookDetailRepository;
    private final ReceiptRepository receiptRepository;

//    @EventListener(ApplicationReadyEvent.class)
    public void seedReceipt() {
        PaymentDetail paymentDetail = PaymentDetail.builder()
                .amount(999999D)
                .paymentType(PaymentType.TRANSFER)
                .provider("nothing")
                .providerId("nothing")
                .build();

        ReceiptDetail receiptDetail = ReceiptDetail.builder()
                .bookCopy(bookDetailRepository.findById(1L).get())
                .pricePerUnit(999999L)
                .quantity(999999L)
                .build();

        Receipt receipt = Receipt.builder()
                .customer(null)
                .customerAddress(null)
                .customerName(null)
                .customerPhone(null)
                .receiptDetails(List.of(receiptDetail))
                .paymentDetail(paymentDetail)
                .build();

        receiptDetail.setReceipt(receipt);
        paymentDetail.setReceipt(receipt);

        receiptRepository.save(receipt);
    }

    //commandline runner create async error when insert before schema is ready, use event listener instead
//    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void seedBook() {
        Genre genre1 = Genre.builder().name("genre 1").build();
        Genre genre2 = Genre.builder().name("genre 2").build();
        Genre genre3 = Genre.builder().name("unattached genre").build();

        Publisher publisher1 = Publisher.builder().name("publisher 1").build();

        Creator creator1 = Creator.builder().name("creator 1").build();
        Creator creator2 = Creator.builder().name("creator 2").build();

        Series series1 = Series.builder().name("series 1").build();

        BookDetail bookDetail1 = BookDetail.builder()
                .bookFormat("paperback")
                .isbn("ISBN 13")
                .bookFormat("paperback")
                .dimensions("1x1")
                .printLength(999L)
                .stock(999L)
                .salePrice(999L)
                .bookCondition("new")
                .build();
        BookDetail bookDetail2 = BookDetail.builder()
                .bookFormat("paperback")
                .isbn("ISBN 13")
                .bookFormat("paperback")
                .dimensions("1x1")
                .printLength(999L)
                .stock(999L)
                .salePrice(899L)
                .bookCondition("old")
                .build();

        Review review1 = Review.builder()
                .comment("comment")
                .rating(5)
                .build();

        Review review2 = Review.builder()
                .comment("comment")
                .rating(4)
                .build();

        Book book1 = Book.builder()
                .title("book 1")
                .language("English")
                .edition("edition 1")
                .published(LocalDateTime.now())
                .publisher(publisher1)
                .creators(List.of(creator1, creator2))
                .genres(List.of(genre1, genre2))
                .reviews(List.of(review1, review2))
                .series(series1)
                .bookCopies(List.of(bookDetail1, bookDetail2))
                .build();

        bookDetail1.setBook(book1);
        bookDetail2.setBook(book1);
        review1.setBook(book1);
        review2.setBook(book1);

        genreRepository.save(genre3);
        bookRepository.save(book1);
    }
}
