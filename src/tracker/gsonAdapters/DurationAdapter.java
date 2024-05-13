package tracker.server;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write( JsonWriter jsonWriter, final Duration duration) throws IOException {
        if (duration != null) {
        jsonWriter.value(duration.toMinutes());
        } else {
        jsonWriter.nullValue();
        }
    }
    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        String str = jsonReader.nextString();
        if (str.equals("null")) {
            return null;
        }
        return Duration.parse(str);
    }
}
