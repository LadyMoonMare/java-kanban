package tracker.gsonAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tracker.model.Epic;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class EpicAdapter extends TypeAdapter<Epic> {
    @Override
    public void write(JsonWriter writer, Epic epic) throws IOException {
        writer.beginObject();
        writer.name("taskName").value(epic.getTaskName());
        writer.name("taskDescription").value(epic.getTaskDescription());
        writer.endObject();
    }

    @Override
    public Epic read(JsonReader reader) throws IOException {
        String taskName = "";
        String taskDescription = "";
        LocalDateTime epStartTime = null;
        Duration epDuration = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("taskName")) {
                taskName = reader.nextString();
            } else if (name.equals("taskDescription")) {
                taskDescription = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Epic(taskName, taskDescription);
    }
}
