package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;
    private Duration subDuration;
    private LocalDateTime subStartTime;

    public Subtask(String taskName, String taskDescription, Status status, int epicId) {
        super(taskName, taskDescription, status);
        this.epicId = epicId;
    }

    public Subtask(String taskName, String taskDescription, Status status, int epicId,
                   LocalDateTime startTime, Duration duration) {
        super(taskName, taskDescription, status);
        this.epicId = epicId;
        this.subStartTime = startTime;
        this.subDuration = duration;
    }

    public Subtask(String taskName, String taskDescription, Integer taskId, Status status,
                   int epicId, LocalDateTime startTime, Duration duration) {
        super(taskName, taskDescription, taskId, status);
        this.epicId = epicId;
        this.subStartTime = startTime;
        this.subDuration = duration;
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
        return subStartTime.plus(subDuration);
    }

    @Override
    public Duration getDuration() {
        return subDuration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return subStartTime;
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
