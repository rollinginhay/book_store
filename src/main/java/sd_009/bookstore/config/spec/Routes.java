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
    public static final String PUT_BOOK_UPDATE = "/v1/book/update";
    public static final String DELETE_BOOK_DELETE = "/v1/book/{id}";
    public static final String MULTI_BOOK_RELATIONSHIP_CREATOR = "/v1/book/{id}/relationships/creator";
    public static final String MULTI_BOOK_RELATIONSHIP_PUBLISHER = "/v1/book/{id}/relationships/publisher";
    public static final String MULTI_BOOK_RELATIONSHIP_SERIES = "/v1/book/{id}/relationships/series";
    public static final String MULTI_BOOK_RELATIONSHIP_GENRE = "/v1/book/{id}/relationships/genre";
    public static final String MULTI_BOOK_RELATIONSHIP_BOOK_DETAIL = "/v1/book/{id}/relationships/bookDetail";
    public static final String MULTI_BOOK_RELATIONSHIP_GENERIC = "/v1/book/{id}/relationships/{dependent}";

    public static final String GET_GENRES = "/v1/genres";
    public static final String GET_GENRE_BY_ID = "/v1/genre/{id}";
    public static final String POST_GENRE_CREATE = "/v1/genre/create";
    public static final String PUT_GENRE_UPDATE = "/v1/genre/update";
    public static final String DELETE_GENRE_DELETE = "/v1/genre/{id}";

    public static final String GET_PUBLISHERS = "/v1/publishers";
    public static final String GET_PUBLISHER_BY_ID = "/v1/publisher/{id}";
    public static final String POST_PUBLISHER_CREATE = "/v1/publisher/create";
    public static final String PUT_PUBLISHER_UPDATE = "/v1/publisher/update";
    public static final String DELETE_PUBLISHER_DELETE = "/v1/publisher/{id}";

    public static final String GET_SERIES = "/v1/seriess";
    public static final String GET_SERIES_BY_ID = "/v1/series/{id}";
    public static final String POST_SERIES_CREATE = "/v1/series/create";
    public static final String PUT_SERIES_UPDATE = "/v1/series/update";
    public static final String DELETE_SERIES_DELETE = "/v1/series/{id}";

    public static final String GET_CREATORS = "/v1/creators";
    public static final String GET_CREATOR_BY_ID = "/v1/creator/{id}";
    public static final String POST_CREATOR_CREATE = "/v1/creator/create";
    public static final String PUT_CREATOR_UPDATE = "/v1/creator/update";
    public static final String DELETE_CREATOR_DELETE = "/v1/creator/{id}";

    public static final String GET_BOOK_DETAIL_BY_ID = "/v1/bookDetail/{id}";
    public static final String POST_BOOK_DETAIL_CREATE = "/v1/bookDetail/create";
    public static final String PUT_BOOK_DETAIL_UPDATE = "/v1/bookDetail/update";
    public static final String DELETE_BOOK_DETAIL_DELETE = "/v1/bookDetail/{id}";

    /*
     * RECEIPT AND DEPENDENTS
     */
    public static final String GET_RECEIPT_DETAIL_BY_ID = "/v1/receiptDetail/{id}";
    public static final String POST_RECEIPT_DETAIL_CREATE = "/v1/receiptDetail/create";
    public static final String POST_RECEIPT_DETAIL_CREATE_ONLINE = "/v1/receiptDetail/createOnline";
    public static final String PUT_RECEIPT_DETAIL_UPDATE = "/v1/receiptDetail/update";
    public static final String DELETE_RECEIPT_DETAIL_DELETE = "/v1/receiptDetail/{id}";
    public static final String MULTI_RECEIPT_DETAIL_RELATIONSHIP_GENERIC = "/v1/receiptDetail/{id}/relationships/{dependent}";

    public static final String GET_PAYMENT_DETAIL_BY_ID = "/v1/paymentDetail/{id}";
    public static final String POST_PAYMENT_DETAIL_CREATE = "/v1/paymentDetail/create";
    public static final String PUT_PAYMENT_DETAIL_UPDATE = "/v1/paymentDetail/update";
    public static final String DELETE_PAYMENT_DETAIL_DELETE = "/v1/paymentDetail/{id}";

    public static final String GET_RECEIPTS = "/v1/receipts";
    public final static String GET_RECEIPT_BY_ID = "/v1/receipt/{id}";
    public static final String POST_RECEIPT_CREATE = "/v1/receipt/create";
    public static final String POST_RECEIPT_CREATE_ONLINE = "/v1/receipt/createOnline";
    public static final String PUT_RECEIPT_UPDATE = "/v1/receipt/update";
    public static final String DELETE_RECEIPT_DELETE = "/v1/receipt/{id}";
    public static final String MULTI_RECEIPT_RELATIONSHIP_RECEIPT_DETAIL = "/v1/receipt/{id}/relationships/receiptDetail";
    public static final String MULTI_RECEIPT_RELATIONSHIP_PAYMENT_DETAIL = "/v1/receipt/{id}/relationships/paymentDetail";
    public static final String MULTI_RECEIPT_RELATIONSHIP_GENERIC = "/v1/receipt/{id}/relationships/{dependent}";
    public static final String GET_RECEIPTS_LIST = "/v1/receipts/list"; // hiển thị receipt admin


    // CART DETAIL
    public static final String GET_ALL_CART_DETAIL_BY_USER_ID = "/v1/user/{userId}/relationships/cartDetail";
    public static final String GET_CART_DETAIL_BY_ID = "/v1/cartDetail/{id}";
    public static final String POST_CART_DETAIL_CREATE = "/v1/cartDetail/create";
    public static final String PUT_CART_DETAIL_UPDATE = "/v1/cartDetail/update";
    public static final String DELETE_CART_DETAIL_DELETE = "/v1/cartDetail/{id}";
    public static final String MULTI_USER_RELATIONSHIP_CART_DETAIL = "/v1/user/{id}/relationships/cartDetail";

    // ===================== CAMPAIGN =====================
    public static final String GET_CAMPAIGNS = "/v1/campaigns";
    public static final String GET_ACTIVE_CAMPAIGNS = "/v1/activecampaigns";
    public static final String GET_CAMPAIGN_BY_ID = "/v1/campaign/{id}";
    public static final String POST_CAMPAIGN_CREATE = "/v1/campaign/create";
    public static final String PUT_CAMPAIGN_UPDATE = "/v1/campaign/update";
    public static final String DELETE_CAMPAIGN_DELETE = "/v1/campaign/{id}";

    public static final String GET_CAMPAIGN_DETAIL_BY_ID = "/v1/campaignDetail/{id}";
    public static final String POST_CAMPAIGN_DETAIL_CREATE = "/v1/campaignDetail/create";
    public static final String PUT_CAMPAIGN_DETAIL_UPDATE = "/v1/campaignDetail/update";
    public static final String DELETE_CAMPAIGN_DETAIL_DELETE = "/v1/campaignDetail/{id}";
    public static final String MULTI_CAMPAIGN_RELATIONSHIP_CAMPAIGN_DETAIL = "/v1/campaign/{id}/relationships/campaignDetail";
    public static final String GET_COMBO_BY_BOOK_DETAIL_ID = "/v1/campaigns/combo";

    // ===================== VOUCHER =====================
    public static final String GET_VOUCHERS = "/v1/vouchers";
    public static final String GET_VOUCHER_BY_ID = "/v1/voucher/{id}";
    public static final String POST_VOUCHER_CREATE = "/v1/voucher/create";
    public static final String PUT_VOUCHER_UPDATE = "/v1/voucher/update";
    public static final String DELETE_VOUCHER_DELETE = "/v1/voucher/{id}";

    // ===================== USER =====================
    public static final String GET_USERS = "/v1/users";
    public static final String GET_USER_BY_ID = "/v1/user/{id}";
    public static final String GET_USER_ME = "/v1/users/me";
    public static final String POST_USER_CREATE = "/v1/user/create";
    public static final String POST_USER_CREATE_WITH_ROLE = "/v1/user/create-with-role";
    public static final String PUT_USER_UPDATE = "/v1/user/update";
    public static final String PUT_USER_UPDATE_WITH_ROLE = "/v1/user/update-with-role";
    public static final String DELETE_USER_DELETE = "/v1/user/{id}";
    public static final String GET_ROLES = "/v1/roles";

    // GENRE CLOSURE (Cây thể loại)
    public static final String GET_GENRE_CLOSURES = "/v1/genreClosures";
    public static final String GET_GENRE_CLOSURE_DESCENDANTS = "/v1/genreClosure/{ancestorId}/descendants";
    public static final String GET_GENRE_CLOSURE_ANCESTORS = "/v1/genreClosure/{descendantId}/ancestors";

}
