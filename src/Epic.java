import java.util.ArrayList;
public class Epic extends Task{
    private ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);

    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addNewSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void deleteSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public boolean isStatusNew() {
        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks) {
                if (!subtask.getStatus().equals(Status.NEW)){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isStatusDone() {
        for (Subtask subtask : subtasks) {
            if (!subtask.getStatus().equals(Status.DONE)) {
                return false;
            }
        }
        return true;
    }

    protected void removeAllSubtasks() {
        subtasks.clear();
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
