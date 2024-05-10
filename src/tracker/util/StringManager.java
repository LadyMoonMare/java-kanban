package tracker.util;

import tracker.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringManager {
    private StringManager() {
    }

    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

    public static String taskToString(Task task) {
        return task.getId() + "," + TaskType.TASK + "," + task.getTaskName() + ","
                + task.getStatus() + "," + task.getTaskDescription() + ", ,"
                + task.getStartTime().format(formatter) + "," + task.getDuration().toMinutes();
    }

    public static String epicToString(Epic epic) {
        return epic.getId() + "," + TaskType.EPIC + "," + epic.getTaskName() + ","
                + epic.getStatus() + "," + epic.getTaskDescription() + ", ,"
                + epic.getStartTime().format(formatter) + "," + epic.getDuration().toMinutes();
    }

    public static String subtaskToString(Subtask subtask) {
        return subtask.getId() + "," + TaskType.SUBTASK + "," + subtask.getTaskName() + ","
                + subtask.getStatus() + "," + subtask.getTaskDescription() + "," + subtask.getEpicId() + ","
                + subtask.getStartTime().format(formatter) + "," + subtask.getDuration().toMinutes();
    }

    public static Task taskFromString(String[] parts) {
        return new Task(parts[2], parts[4], Integer.parseInt(parts[0]), Status.valueOf(parts[3]),
                LocalDateTime.parse(parts[6],formatter), Duration.ofMinutes(Long.parseLong(parts[7])));
    }

    public static Epic epicFromString(String[] parts) {
        return new Epic(parts[2], parts[4], Integer.parseInt(parts[0]),
                LocalDateTime.parse(parts[6],formatter), Duration.ofMinutes(Long.parseLong(parts[7])));
    }

    public static Subtask subtaskFromString(String[] parts) {
        return new Subtask(parts[2], parts[4], Integer.parseInt(parts[0]), Status.valueOf(parts[3]),
                Integer.parseInt(parts[5]), LocalDateTime.parse(parts[6], formatter),
                Duration.ofMinutes(Long.parseLong(parts[7])));
    }
}
