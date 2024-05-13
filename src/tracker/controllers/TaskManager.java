package tracker.controllers;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    void removeAllTasks();

    Task getTask(int taskId);

    void addTask(Task newTask);

    void addTask(Task newTask, Integer taskId);

    void updateTask(Task updatedTask);

    void removeTask(Integer taskId);

    List<Epic> getAllEpics();

    void removeAllEpics();

    Epic getEpic(int epicId);

    List<Subtask> getAllSubtasksInEpic(Epic epic);

    void addEpic(Epic newEpic);

    void addEpic(Epic newEpic, Integer id);

    void updateEpic(Epic updatedEpic);

    void removeEpic(Integer epicId);

    List<Subtask> getAllSubtasks();

    void removeAllSubtasks();

    void removeAllSubtasksInEpic(Epic epic);

    Subtask getSubtask(int subtaskId);

    void addSubtask(Subtask newSubtask);

    void addSubtask(Subtask newSubtask, Integer id);

    void updateSubtask(Subtask updatedSubtask);

    void removeSubtask(Integer subtaskId);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

}
