package tracker.controllers;
import tracker.exceptions.ManagerSaveException;
import tracker.model.*;
import tracker.util.StringManager;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private Path path;
    private static final String COLUMNS = "id, type, name, status, description, epic, start, duration";

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
        manager.add(task);
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
        manager.add(epic);
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
        manager.add(subtask);
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
            StringBuilder history = new StringBuilder();

            manager.getHistory().stream()
                    .forEach(t -> history.append(t.getId()).append(","));
            String historyAsStr = history.toString();

            bw.write(COLUMNS);
            bw.newLine();
            saveTasksToFile(bw);
            bw.newLine();
            saveEpicsToFile(bw);
            bw.newLine();
            saveSubtasksToFile(bw);
            bw.newLine();
            bw.write(historyAsStr);
            bw.newLine();
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public void saveTasksToFile(BufferedWriter bw) {
        super.getTasks().values().stream()
                .forEach(t -> {
                    try {
                        bw.write(StringManager.taskToString(t));
                        bw.newLine();
                    } catch (IOException e) {
                        throw new ManagerSaveException();
                    }
                });
    }

    public void saveEpicsToFile(BufferedWriter bw) {
        super.getEpics().values().stream()
                .forEach(ep -> {
                    try {
                        bw.write(StringManager.epicToString(ep));
                        bw.newLine();
                    } catch (IOException e) {
                        throw new ManagerSaveException();
                    }
                });
    }

    public void saveSubtasksToFile(BufferedWriter bw) {
        super.getSubtasks().values().stream()
                .forEach(s -> {
                    try {
                        bw.write(StringManager.subtaskToString(s));
                        bw.newLine();
                    } catch (IOException e) {
                        throw new ManagerSaveException();
                    }
                });
    }

     static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(file.toPath());
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (br.ready()) {
                String line = br.readLine();
                String[] parts = line.split(",");
                if (!line.equals(COLUMNS) && !line.isEmpty()) {
                    loadFromFileByTaskTypes(fileManager,parts);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        return fileManager;
    }


    static void loadFromFileByTaskTypes(FileBackedTaskManager fileManager, String[] parts) {
        try {
            switch (TaskType.valueOf(parts[1])) {
                case TASK -> fileManager.addTask(StringManager.taskFromString(parts),
                        StringManager.taskFromString(parts).getId());
                case EPIC -> fileManager.addEpic(StringManager.epicFromString(parts),
                        StringManager.epicFromString(parts).getId());
                case SUBTASK -> fileManager.addSubtask(StringManager.subtaskFromString(parts),
                        StringManager.subtaskFromString(parts).getId());
            }
        } catch (IllegalArgumentException e) {
            for (String part : parts) {
                if (fileManager.getTasks().containsKey(Integer.parseInt(part))) {
                    fileManager.getTask(Integer.parseInt(part));
                } else if (fileManager.getEpics().containsKey(Integer.parseInt(part))) {
                    fileManager.getEpic(Integer.parseInt(part));
                } else if (fileManager.getSubtasks().containsKey(Integer.parseInt(part))) {
                    fileManager.getSubtask(Integer.parseInt(part));
                }
            }
        }
    }
}