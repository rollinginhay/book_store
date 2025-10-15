package sd_009.bookstore.config.spec;

public enum Routes {
    GET_BOOKS("/v1/books"),
    GET_BOOK_BY_ID("/v1//book/{id}"),
    BOOK_RELATIONSHIP_CREATOR("/v1/book/{id}/relationships/creator"),
    BOOK_RELATIONSHIP_PUBLISHER("/v1/book/{id}/relationships/publisher"),
    BOOK_RELATIONSHIP_SERIES("/v1/book/{id}/relationships/series"),
    BOOK_RELATIONSHIP_GENRE("/v1/book/{id}/relationships/genre"),
    BOOK_RELATIONSHIP_BOOK_DETAIL("/v1/book/{id}/relationships/bookDetail"),
    BOOK_RELATIONSHIP_BOOK_REVIEW("/v1/book/{id}/relationships/review"),

    GET_GENRES("v1/genres"),
    GET_GENRE_BY_ID("v1/genre/{id}"),

    GET_PUBLISHERS("/v1/publishers"),
    GET_PUBLISHER_BY_ID("v1/publisher/{id}"),

    GET_SERIES("/v1/series"),
    GET_SERIES_BY_ID("v1/series/{id}"),

    GET_CREATORS("/v1/creators"),
    GET_CREATOR_BY_ID("v1/creator/{id}"),


    GET_BOOK_DETAILS_BY_BOOK_ID("v1/book/{id}/bookDetails"),
    GET_BOOK_DETAIL_BY_ID("v1/bookDetail/{id}"),

    GET_REVIEWS_BY_BOOK_ID("v1/book/{id}/reviews"),
    GET_REVIEW_BY_ID("v1/review/{id}"),

    ;
    private final String name;

    Routes(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static final String GET_BOOKS_PATH = "/v1/books";
    public static final String GET_BOOK_BY_ID_PATH = "/v1/book/{id}";
    public static final String BOOK_RELATIONSHIP_CREATOR_PATH = "/v1/book/{id}/relationships/creator";
    public static final String BOOK_RELATIONSHIP_PUBLISHER_PATH = "/v1/book/{id}/relationships/publisher";
    public static final String BOOK_RELATIONSHIP_SERIES_PATH = "/v1/book/{id}/relationships/series";
    public static final String BOOK_RELATIONSHIP_GENRE_PATH = "/v1/book/{id}/relationships/genre";
    public static final String BOOK_RELATIONSHIP_BOOK_DETAIL_PATH = "/v1/book/{id}/relationships/bookDetail";
    public static final String BOOK_RELATIONSHIP_BOOK_REVIEW_PATH = "/v1/book/{id}/relationships/review";
    public static final String GET_BOOK_RELATIONSHIP_GENERIC_PATH = "/v1/book/{id}/relationships/{dependent}";

    public static final String GET_GENRES_PATH = "/v1/genres";
    public static final String GET_GENRE_BY_ID_PATH = "/v1/genre/{id}";

    public static final String GET_PUBLISHERS_PATH = "/v1/publishers";
    public static final String GET_PUBLISHER_BY_ID_PATH = "/v1/publisher/{id}";

    public static final String GET_SERIES_PATH = "/v1/series";
    public static final String GET_SERIES_BY_ID_PATH = "/v1/series/{id}";

    public static final String GET_CREATORS_PATH = "/v1/creators";
    public static final String GET_CREATOR_BY_ID_PATH = "/v1/creator/{id}";

    public static final String GET_BOOK_DETAILS_BY_BOOK_ID_PATH = "/v1/book/{id}/bookDetails";
    public static final String GET_BOOK_DETAIL_BY_ID_PATH = "/v1/bookDetail/{id}";

    public static final String GET_REVIEWS_BY_BOOK_ID_PATH = "/v1/book/{id}/reviews";
    public static final String GET_REVIEW_BY_ID_PATH = "/v1/review/{id}";

}
