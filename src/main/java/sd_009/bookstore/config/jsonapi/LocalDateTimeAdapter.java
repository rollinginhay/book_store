package sd_009.bookstore.config.jsonapi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LocalDateTimeAdapter extends JsonAdapter<LocalDateTime> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDateTime fromJson(JsonReader reader) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL) {
            reader.nextNull();
            return null;
        }

        String s = reader.nextString();

        // 1. Try parsing full datetime
        try {
            return LocalDateTime.parse(s, DATE_TIME_FORMATTER);
        } catch (Exception ignored) {}

        // 2. Try parsing as LocalDate (yyyy-MM-dd)
        try {
            LocalDate date = LocalDate.parse(s, DATE_FORMATTER);
            return date.atStartOfDay(); // Set time = 00:00
        } catch (Exception ignored) {}

        throw new IOException("Invalid date/time format: " + s);
    }

    @Override
    public void toJson(JsonWriter writer, LocalDateTime value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(value.format(DATE_TIME_FORMATTER));
    }
}
