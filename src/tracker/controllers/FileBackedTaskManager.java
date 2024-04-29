package tracker.controllers;
import tracker.model.*;
import tracker.util.StringManager;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    static InMemoryTaskManager manager = new InMemoryTaskManager();
    private Path path;
    private static final String COLUMNS = "id, type, name, status, description, epic";
    List<Integer> history = new ArrayList<>();

    public FileBackedTaskManager(Path path) {
        this.path = path;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public Task getTask(int taskId) {
        Task task = super.getTask(taskId);
        history.add(taskId);
        save();
        return task;
    }

    @Override
    public void updateTask(Task updatedTask) {
        super.updateTask(updatedTask);
        save();
    }

    @Override
    public void removeTask(Integer taskId) {
        super.removeTask(taskId);
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = super.getEpic(epicId);
        history.add(epicId);
        save();
        return epic;
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        super.updateEpic(updatedEpic);
        save();
    }

    @Override
    public void removeEpic(Integer epicId) {
        super.removeEpic(epicId);
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeAllSubtasksInEpic(Epic epic) {
        super.removeAllSubtasksInEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public Subtask getSubtask(int subtaskId) {
        Subtask subtask = super.getSubtask(subtaskId);
        history.add(subtaskId);
        save();
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        super.updateSubtask(updatedSubtask);
        save();
    }

    @Override
    public void removeSubtask(Integer subtaskId) {
        super.removeSubtask(subtaskId);
        save();
    }

    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path.toFile(), StandardCharsets.UTF_8))) {
            String tasksAsStr = saveTasksToFile(bw);
            String epicsAsStr = saveEpicsToFile(bw);
            String subtasksAsStr = saveSubtasksToFile(bw);
            String historyAsStr = history.toString();

            bw.write(COLUMNS);
            bw.newLine();
            bw.write(tasksAsStr);
            bw.newLine();
            bw.write(epicsAsStr);
            bw.newLine();
            bw.write(subtasksAsStr);
            bw.newLine();
            bw.write(historyAsStr);
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public String saveTasksToFile(BufferedWriter bw) throws IOException {
        String stateAsText;

        for (Task task : manager.getTasks().values()) {
            stateAsText = StringManager.taskToString(task);
            bw.write(stateAsText);
            bw.newLine();
        }
        return bw.toString();
    }

    public String saveEpicsToFile(BufferedWriter bw) throws IOException {
        String stateAsText;

        for (Epic epic : manager.getEpics().values()) {
            stateAsText = StringManager.epicToString(epic);
            bw.write(stateAsText);
            bw.newLine();
        }
        return bw.toString();
    }

    public String saveSubtasksToFile(BufferedWriter bw) throws IOException {
        String stateAsText;

        for (Subtask subtask : manager.getSubtasks().values()) {
            stateAsText = StringManager.subtaskToString(subtask);
            bw.write(stateAsText);
            bw.newLine();
        }
        return bw.toString();
    }

    static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(file.toPath());
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (br.ready()) {
                String line = br.readLine();
                if (!line.equals(COLUMNS)) {
                    String[] parts = line.split(",");
                    switch (TaskType.valueOf(parts[1])) {
                        case TASK -> manager.addTask(StringManager.taskFromString(parts));
                        case EPIC -> manager.addEpic(StringManager.epicFromString(parts));
                        case SUBTASK -> manager.addSubtask(StringManager.subtaskFromString(parts));
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        return fileManager;
    }
}