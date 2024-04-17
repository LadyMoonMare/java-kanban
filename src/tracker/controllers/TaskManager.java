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

    Task addTask(Task newTask);

    Task updateTask(Task updatedTask);

    Task removeTask(Integer taskId);

    ArrayList<Epic> getAllEpics();

    void removeAllEpics();

    Epic getEpic(int epicId);

    List<Subtask> getAllSubtasksInEpic(Epic epic);

    Epic addEpic(Epic newEpic);

    Task updateEpic(Epic updatedEpic);

    Epic removeEpic(Integer epicId);

    ArrayList<Subtask> getAllSubtasks();

    void removeAllSubtasks();

    void removeAllSubtasksInEpic(Epic epic);

    Subtask getSubtask(int subtaskId);

    Subtask addSubtask(Subtask newSubtask);

    Subtask updateSubtask(Subtask updatedSubtask);

    Subtask removeSubtask(Integer subtaskId);

    List<Task> getHistory();

}
