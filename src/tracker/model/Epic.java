package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    private List<Subtask> subtasks;
    private Duration epDuration;
    private LocalDateTime epStartTime;
    private LocalDateTime endTime;
    private Comparator<Subtask> comparator;

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        subtasks = new ArrayList<>();
        comparator = (s1, s2) -> {
            if (s1.getStartTime().isBefore(s2.getStartTime())) {
                return -1;
            } else if (s2.getStartTime().isBefore(s1.getStartTime())) {
                return 1;
            } else {
                return 0;
            }
        };
    }

    public Epic(String taskName, String taskDescription, LocalDateTime startTime, Duration duration) {
        super(taskName, taskDescription);
        this.epStartTime = startTime;
        this.epDuration = duration;
        subtasks = new ArrayList<>();
        comparator = (s1, s2) -> {
            if (s1.getStartTime().isBefore(s2.getStartTime())) {
                return -1;
            } else if (s2.getStartTime().isBefore(s1.getStartTime())) {
                return 1;
            } else {
                return 0;
            }
        };
    }

    public Epic(String taskName, String taskDescription, Integer taskId,
                LocalDateTime startTime, Duration duration) {
        super(taskName, taskDescription, taskId);
        this.epStartTime = startTime;
        this.epDuration = duration;
        subtasks = new ArrayList<>();
        comparator = (s1, s2) -> {
            if (s1.getStartTime().isBefore(s2.getStartTime())) {
                return -1;
            } else if (s2.getStartTime().isBefore(s1.getStartTime())) {
                return 1;
            } else {
                return 0;
            }
        };
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
            timedSubtasks.sort(comparator);
            epStartTime = timedSubtasks.get(0).getStartTime();
            endTime = timedSubtasks.get(timedSubtasks.size() - 1).getStartTime()
                    .plus(subtasks.get(subtasks.size() - 1).getDuration());
            return Optional.of(endTime);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Duration> setEpicDuration() {
        if (!subtasks.isEmpty()) {
            epDuration = Duration.ofMinutes(0);
            for (Subtask subtask : subtasks) {
                if (subtask.getDuration() != null) {
                    epDuration = epDuration.plus(subtask.getDuration());
                }
            }
            return Optional.of(epDuration);
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
            epDuration = setEpicDuration().get();
        }
        return epDuration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return epStartTime;
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
