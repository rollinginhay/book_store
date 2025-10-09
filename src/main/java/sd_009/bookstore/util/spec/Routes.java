package sd_009.bookstore.util.spec;

public enum Routes {
    GET_BOOKS("/v1/books"),
    GET_BOOK_BY_ID("/v1//book/{id}"),

    GET_GENRES("v1/genres"),
    GET_GENRE_BY_ID("v1/genre/{id}"),

    GET_PUBLISHERS("/v1/publishers"),
    GET_PUBLISHER_BY_ID("v1/publisher/{id}"),

    GET_SERIES("/v1/series"),
    GET_SERIES_BY_ID("v1/series/{id}"),

    GET_CREATORS("/v1/creators"),
    GET_CREATOR_BY_ID("v1/creator/{id}"),

    GET_REVIEWS("/v1/reviews"),
    GET_REVIEW_BY_ID("v1/review/{id}"),

    GET_BOOK_DETAILS_BY_BOOK_ID("v1/book/{id}/bookdetails"),
    GET_REVIEWS_BY_BOOK_ID("v1/book/{id}/reviews"),

    ;
    private final String name;

    Routes(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
