package tracker.httphandlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {

    protected BaseHttpHandler() {

    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendSuccess(HttpExchange h) throws IOException {
        h.sendResponseHeaders(201, 0);
        h.close();
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        h.sendResponseHeaders(404, 0);

        try (OutputStream os = h.getResponseBody()) {
            os.write("Not Found".getBytes());
        }
        h.close();
    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        h.sendResponseHeaders(406, 0);

        try (OutputStream os = h.getResponseBody()) {
            os.write("Not Acceptable".getBytes());
        }
        h.close();
    }

    protected void sendHasError(HttpExchange h) throws IOException {
        h.sendResponseHeaders(500, 0);

        try (OutputStream os = h.getResponseBody()) {
            os.write("Internal Server Error".getBytes());
        }
        h.close();
    }

    protected Endpoint getEndpoint(String path,String method) {
        String[] parts = path.split("/");
        switch (method) {
            case "GET" -> {
                if (parts.length == 3) {
                    try {
                        Integer.parseInt(parts[2]);
                        return Endpoint.GET_ID;
                    } catch (IllegalArgumentException e) {
                        return Endpoint.UNKNOWN;
                    }
                } else if ((parts.length == 4 && parts[3].equals("subtasks"))) {
                    return Endpoint.GET_SUBTASKS;
                } else if (parts.length == 2) {
                    return Endpoint.GET;
                } else {
                    return Endpoint.UNKNOWN;
                }
            }
            case "POST" -> {
                return Endpoint.POST;
            }
            case "DELETE" -> {
                return Endpoint.DELETE;
            }
            default -> {
                return Endpoint.UNKNOWN;
            }
        }
    }

    protected enum Endpoint {
        GET,GET_ID,GET_SUBTASKS,POST,DELETE,UNKNOWN
    }
}
