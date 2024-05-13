package tracker.httphandlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tracker.controllers.TaskManager;
import tracker.exceptions.NotFoundException;
import tracker.exceptions.TaskValidTimeException;
import tracker.gsonAdapters.DurationAdapter;
import tracker.gsonAdapters.LocalDateTimeAdapter;
import tracker.model.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager manager;

    public SubtaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    @Override
    public void handle(HttpExchange h) throws IOException {
        Endpoint endpoint = getEndpoint(h.getRequestURI().getPath(), h.getRequestMethod());
        switch (endpoint) {
            case GET -> handleGetTasks(h);
            case GET_ID -> handleGetTaskById(h,h.getRequestURI().getPath());
            case POST -> handlePostTask(h);
            case DELETE -> handleDeleteTask(h,h.getRequestURI().getPath());
            case UNKNOWN -> sendNotFound(h);
        }

    }

    private void handleGetTasks(HttpExchange h) throws IOException {
        sendText(h,manager.getAllSubtasks().toString());
    }

    private void handleGetTaskById(HttpExchange h, String path) throws IOException {
        String[] parts = path.split("/");

        try {
            sendText(h,manager.getSubtask(Integer.parseInt(parts[2])).toString());
        } catch (NotFoundException e) {
            sendNotFound(h);
        }

    }

    private void handleDeleteTask(HttpExchange h, String path) throws IOException {
        String[] parts = path.split("/");

        try {
            manager.removeSubtask(Integer.parseInt(parts[2]));
            sendSuccess(h);
        } catch (NotFoundException e) {
            sendNotFound(h);
        } catch (IllegalArgumentException e) {
            sendHasInteractions(h);
        }
    }

    private void handlePostTask(HttpExchange h) throws IOException {
        try {
            InputStream bodyInputStream = h.getRequestBody();
            String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
            JsonElement jsonElement = JsonParser.parseString(body);
            if (!jsonElement.isJsonObject()) {
                sendNotFound(h);
            }
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Subtask subtask = gson.fromJson(jsonObject,Subtask.class);
            if (subtask.getId() != null) {
                manager.updateSubtask(subtask);
            } else {
                manager.addSubtask(subtask);
            }
            sendSuccess(h);
        } catch (NotFoundException e) {
            sendNotFound(h);
        } catch (IllegalArgumentException | TaskValidTimeException e) {
            sendHasInteractions(h);
        }
    }
}
