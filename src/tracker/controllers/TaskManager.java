package tracker.controllers;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getAllTasks();

    void removeAllTasks();

    Task getTask(int taskId);

    void addTask(Task newTask);

    void updateTask(Task updatedTask);

    void removeTask(Integer taskId);

    ArrayList<Epic> getAllEpics();

    void removeAllEpics();

    Epic getEpic(int epicId);

    List<Subtask> getAllSubtasksInEpic(Epic epic);

    void addEpic(Epic newEpic);

    void updateEpic(Epic updatedEpic);

    void removeEpic(Integer epicId);

    ArrayList<Subtask> getAllSubtasks();

    void removeAllSubtasks();

    void removeAllSubtasksInEpic(Epic epic);

    Subtask getSubtask(int subtaskId);

    void addSubtask(Subtask newSubtask);

    void updateSubtask(Subtask updatedSubtask);

    void removeSubtask(Integer subtaskId);

    List<Task> getHistory();

}
