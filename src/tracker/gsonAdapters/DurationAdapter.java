package tracker.gsonAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (duration != null) {
        jsonWriter.value(duration.toMinutes());
        } else {
        jsonWriter.nullValue();
        }
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        String str = "PT" + jsonReader.nextString() + "M";
        if (str.equals("PTM")) {
            return null;
        }
        return Duration.parse(str);
    }
}
