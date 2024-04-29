package tracker.util;

import tracker.model.*;

public class StringManager {
    private StringManager() {
    }

    public static String taskToString(Task task) {
        return task.getId() + "," + TaskType.TASK + "," + task.getTaskName() + ","
                + "," + task.getStatus() + "," + task.getTaskDescription();
    }

    public static String epicToString(Epic epic) {
        return epic.getId() + "," + TaskType.EPIC + "," + epic.getTaskName() + ","
                + "," + epic.getStatus() + "," + epic.getTaskDescription();
    }

    public static String subtaskToString(Subtask subtask) {
        return subtask.getId() + "," + TaskType.SUBTASK + "," + subtask.getTaskName() + ","
                + subtask.getStatus() + "," + subtask.getTaskDescription() + "," + subtask.getEpicId();
    }

    public static Task taskFromString(String[] parts) {
        return new Task(parts[2], parts[4], Integer.parseInt(parts[0]), Status.valueOf(parts[3]));
    }

    public static Epic epicFromString(String[] parts) {
        return new Epic(parts[2], parts[4], Integer.parseInt(parts[0]), Status.valueOf(parts[3]));
    }

    public static Subtask subtaskFromString(String[] parts) {
        return new Subtask(parts[2], parts[4], Integer.parseInt(parts[0]), Status.valueOf(parts[3]),
                Integer.parseInt(parts[5]));
    }
}
