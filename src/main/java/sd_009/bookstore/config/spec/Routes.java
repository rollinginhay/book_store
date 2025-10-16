package sd_009.bookstore.config.spec;

public enum Routes {

    ;
    private final String name;

    Routes(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /*
     *BOOK AND DEPENDENTS
     */
    public static final String GET_BOOKS = "/v1/books";
    public static final String GET_BOOK_BY_ID = "/v1/book/{id}";
    public static final String POST_BOOK_CREATE = "/v1/book/create";
    public static final String PUT_BOOK_UPDATE = "/v1/book/{id}";
    public static final String DELETE_BOOK_DELETE = "/v1/book/{id}";
    public static final String MULTI_BOOK_RELATIONSHIP_CREATOR = "/v1/book/{id}/relationships/creator";
    public static final String MULTI_BOOK_RELATIONSHIP_PUBLISHER = "/v1/book/{id}/relationships/publisher";
    public static final String MULTI_BOOK_RELATIONSHIP_SERIES = "/v1/book/{id}/relationships/series";
    public static final String MULTI_BOOK_RELATIONSHIP_GENRE = "/v1/book/{id}/relationships/genre";
    public static final String MULTI_BOOK_RELATIONSHIP_BOOK_DETAIL = "/v1/book/{id}/relationships/bookDetail";
    public static final String MULTI_BOOK_RELATIONSHIP_REVIEW = "/v1/book/{id}/relationships/review";
    public static final String MULTI_BOOK_RELATIONSHIP_GENERIC = "/v1/book/{id}/relationships/{dependent}";

    public static final String GET_GENRES = "/v1/genres";
    public static final String GET_GENRE_BY_ID = "/v1/genre/{id}";
    public static final String POST_GENRE_CREATE = "/v1/genre/create";
    public static final String PUT_GENRE_UPDATE = "/v1/genre/{id}";
    public static final String DELETE_GENRE_DELETE = "/v1/genre/{id}";

    public static final String GET_PUBLISHERS = "/v1/publishers";
    public static final String GET_PUBLISHER_BY_ID = "/v1/publisher/{id}";
    public static final String POST_PUBLISHER_CREATE = "/v1/publisher/create";
    public static final String PUT_PUBLISHER_UPDATE = "/v1/publisher/{id}";
    public static final String DELETE_PUBLISHER_DELETE = "/v1/publisher/{id}";

    public static final String GET_SERIES = "/v1/series";
    public static final String GET_SERIES_BY_ID = "/v1/series/{id}";
    public static final String POST_SERIES_CREATE = "/v1/series/create";
    public static final String PUT_SERIES_UPDATE = "/v1/series/{id}";
    public static final String DELETE_SERIES_DELETE = "/v1/series/{id}";

    public static final String GET_CREATORS = "/v1/creators";
    public static final String GET_CREATOR_BY_ID = "/v1/creator/{id}";
    public static final String POST_CREATOR_CREATE = "/v1/creator/create";
    public static final String PUT_CREATOR_UPDATE = "/v1/creator/{id}";
    public static final String DELETE_CREATOR_DELETE = "/v1/creator/{id}";

    public static final String GET_BOOK_DETAIL_BY_ID = "/v1/bookDetail/{id}";
    public static final String POST_BOOK_DETAIL_CREATE = "/v1/bookDetail/create";
    public static final String PUT_BOOK_DETAIL_UPDATE = "/v1/bookDetail/{id}";
    public static final String DELETE_BOOK_DETAIL_DELETE = "/v1/bookDetail/{id}";

    public static final String GET_REVIEW_BY_ID = "/v1/review/{id}";
    public static final String POST_REVIEW_CREATE = "/v1/review/create";
    public static final String PUT_REVIEW_UPDATE = "/v1/review/{id}";
    public static final String DELETE_REVIEW_DELETE = "/v1/review/{id}";

    /*
     * RECEIPT AND DEPENDENTS
     */
    public static final String GET_RECEIPT_DETAIL_BY_ID = "/v1/receiptDetail/{id}";
    public static final String POST_RECEIPT_DETAIL_CREATE = "/v1/receiptDetail/create";
    public static final String PUT_RECEIPT_DETAIL_UPDATE = "/v1/receiptDetail/{id}";
    public static final String DELETE_RECEIPT_DETAIL_DELETE = "/v1/receiptDetail/{id}";

    public static final String GET_PAYMENT_DETAIL_BY_ID = "/v1/paymentDetail/{id}";
    public static final String POST_PAYMENT_DETAIL_CREATE = "/v1/paymentDetail/create";
    public static final String PUT_PAYMENT_DETAIL_UPDATE = "/v1/paymentDetail/{id}";
    public static final String DELETE_PAYMENT_DETAIL_DELETE = "/v1/paymentDetail/{id}";

    public static final String GET_RECEIPTS = "/v1/receipts";
    public final static String GET_RECEIPT_BY_ID = "/v1/receipt/{id}";
    public static final String POST_RECEIPT_CREATE = "/v1/receipt/create";
    public static final String PUT_RECEIPT_UPDATE = "/v1/receipt/{id}";
    public static final String DELETE_RECEIPT_DELETE = "/v1/receipt/{id}";

    public static final String MULTI_RECEIPT_RELATIONSHIP_RECEIPT_DETAIL = "/v1/receipt/{id}/relationships/receiptDetail";
    public static final String MULTI_RECEIPT_RELATIONSHIP_PAYMENT_DETAIL = "/v1/receipt/{id}/relationships/paymentDetail";
    public static final String MULTI_RECEIPT_RELATIONSHIP_GENERIC = "/v1/receipt/{id}/relationships/{dependent}";
}
