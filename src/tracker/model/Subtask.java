package tracker.model;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String taskName, String taskDescription, Status status, int epicId) {
        super(taskName, taskDescription, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
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
