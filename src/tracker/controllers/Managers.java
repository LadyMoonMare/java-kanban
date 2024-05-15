package tracker.controllers;

import tracker.server.HttpTaskServer;

import java.io.IOException;

public class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static HttpTaskServer getDefaultServer() throws IOException {
        return new HttpTaskServer(getDefault());
    }

    public static HttpTaskServer getDefaultServer(TaskManager manager) throws IOException {
        return new HttpTaskServer(manager); //метод для возможности тестирования
    }
}
