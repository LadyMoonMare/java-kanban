package tracker.model;

public class Task {
    private String taskName;
    private String taskDescription;
    private Integer taskId;
    private Status status;

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }

    public Task(String taskName, String taskDescription, Status status) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
    }

    public Task(String taskName, String taskDescription, Integer taskId, Status status) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskId = taskId;
        this.status = status;
    }

    public int getId() {
        return taskId;
    }

    public void setId(Integer taskId) {
        this.taskId = taskId;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskId=" + taskId +
                ", status=" + status +
                '}';
    }
}
