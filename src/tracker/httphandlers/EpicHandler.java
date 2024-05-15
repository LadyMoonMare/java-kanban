package tracker.httphandlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tracker.controllers.TaskManager;
import tracker.exceptions.NotFoundException;
import tracker.exceptions.TaskValidTimeException;
import tracker.model.Epic;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    public EpicHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange h) throws IOException {
        Endpoint endpoint = getEndpoint(h.getRequestURI().getPath(), h.getRequestMethod());

        switch (endpoint) {
            case GET -> handleGetEpics(h);
            case GET_ID -> handleGetEpicById(h,h.getRequestURI().getPath());
            case GET_SUBTASKS -> handleGetSubtasksInEpic(h,h.getRequestURI().getPath());
            case POST -> handlePostTask(h);
            case DELETE -> handleDeleteEpic(h,h.getRequestURI().getPath());
            case UNKNOWN -> sendNotFound(h);
        }
    }

    private void handleGetEpics(HttpExchange h) throws IOException {
        sendText(h,manager.getAllEpics().toString());
    }

    private void handleGetEpicById(HttpExchange h, String path) throws IOException {
        String[] parts = path.split("/");

        try {
            sendText(h,manager.getEpic(Integer.parseInt(parts[2])).toString());
        } catch (NotFoundException e) {
            sendNotFound(h);
        }

    }

    private void handleGetSubtasksInEpic(HttpExchange h, String path) throws IOException {
        String[] parts = path.split("/");

        try {
            sendText(h,manager.getEpic(Integer.parseInt(parts[2])).getSubtasks().toString());
        } catch (NotFoundException e) {
            sendNotFound(h);
        }
    }

    private void handleDeleteEpic(HttpExchange h, String path) throws IOException {
        String[] parts = path.split("/");

        try {
            manager.removeEpic(Integer.parseInt(parts[2]));
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
            Epic epic = gson.fromJson(jsonObject, Epic.class);
            manager.addEpic(epic);
            sendSuccess(h);
        } catch (NotFoundException e) {
            sendNotFound(h);
        } catch (IllegalArgumentException | TaskValidTimeException e) {
            sendHasInteractions(h);
        }
    }
}
