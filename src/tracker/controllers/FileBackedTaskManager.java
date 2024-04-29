package tracker.controllers;
import tracker.model.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private Path path;
    public FileBackedTaskManager(Path path) {
        this.path = path;
    }

    @Override
    public Task addTask(Task task) {
        super.addTask(task);
        return task;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        return subtask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        super.addEpic(epic);
        return epic;
    }

    public void save() {

    }
}
