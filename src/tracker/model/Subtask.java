package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;
    private Duration duration;
    private LocalDateTime startTime;

    public Subtask(String taskName, String taskDescription, Status status, int epicId) {
        super(taskName, taskDescription, status);
        this.epicId = epicId;
    }

    public Subtask(String taskName, String taskDescription, Status status, int epicId,
                   LocalDateTime startTime, Duration duration) {
        super(taskName, taskDescription, status);
        this.epicId = epicId;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Subtask(String taskName, String taskDescription, Integer taskId, Status status,
                   int epicId, LocalDateTime startTime, Duration duration) {
        super(taskName, taskDescription, taskId, status);
        this.epicId = epicId;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Subtask(String taskName, String taskDescription, Integer taskId, Status status, int epicId) {
        super(taskName, taskDescription, taskId, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "subtaskId=" + this.getId() +
                ", subtaskName=" + this.getTaskName() +
                ", subtaskDescription=" + this.getTaskDescription() +
                ", status=" + this.getStatus() +
                ", epicId=" + epicId +
                '}';
    }
}
