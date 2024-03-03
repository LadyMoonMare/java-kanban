package tracker.model;

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

    public Status setEpicStatus() {
        int counter = 0;

        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks) {
                if (subtask.getStatus().equals(Status.NEW)){
                    counter++;
                } else if (subtask.getStatus().equals(Status.DONE)) {
                    counter--;
                }
            }
            if (counter == subtasks.size()){
                return Status.NEW;
            } else if (counter == -subtasks.size()){
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
