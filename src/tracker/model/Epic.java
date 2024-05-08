package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Epic extends Task {
    private List<Subtask> subtasks = new ArrayList<>();
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }

    public Epic(String taskName, String taskDescription, LocalDateTime startTime, Duration duration) {
        super(taskName, taskDescription);
        this.startTime = startTime;
        this.duration = duration;
    }

    public Epic(String taskName, String taskDescription, Integer taskId, Status status) {
        super(taskName, taskDescription, taskId, status);
    }

    public Epic(String taskName, String taskDescription, Integer taskId, Status status,
                LocalDateTime startTime, Duration duration) {
        super(taskName, taskDescription, taskId, status);
        this.startTime = startTime;
        this.duration = duration;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addNewSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void deleteSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public Status setEpicStatus() {
        int counter = 0;

        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks) {
                if (subtask.getStatus().equals(Status.NEW)) {
                    counter++;
                } else if (subtask.getStatus().equals(Status.DONE)) {
                    counter--;
                }
            }
            if (counter == subtasks.size()) {
                return Status.NEW;
            } else if (counter == -subtasks.size()) {
                return Status.DONE;
            } else {
                return Status.IN_PROGRESS;
            }
        } else {
            return Status.NEW;
        }
    }

    public void removeAllSubtasks() {
        subtasks.clear();
    }

    public Optional<LocalDateTime> setEpicEndTime() {
        if (!subtasks.isEmpty()) {
            List<Subtask> timedSubtasks = new ArrayList<>();

            for (Subtask s : subtasks) {
                if (s.getStartTime() != null) {
                    timedSubtasks.add(s);
                }
            }
            startTime = timedSubtasks.get(0).getStartTime();
            endTime = timedSubtasks.get(timedSubtasks.size() - 1).getStartTime()
                    .plus(subtasks.get(subtasks.size() - 1).getDuration());
            return Optional.of(endTime);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Duration> setEpicDuration() {
        if (!subtasks.isEmpty()) {
            duration = Duration.ofMinutes(0);
            for (Subtask subtask : subtasks) {
                if (subtask.getDuration() != null) {
                    duration = duration.plus(subtask.getDuration());
                }
            }
            return Optional.of(duration);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public LocalDateTime getEndTime() {
        if (setEpicEndTime().isPresent()) {
            endTime = setEpicEndTime().get();
        }
        return endTime;
    }

    @Override
    public Duration getDuration() {
        if (setEpicDuration().isPresent()) {
            duration = setEpicDuration().get();
        }
        return duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "epicId=" + this.getId() +
                ", epicName=" + this.getTaskName() +
                ", epicDescription=" + this.getTaskDescription() +
                ", status=" + this.getStatus() +
                ", subtasks=" + subtasks +

                '}';
    }
}
