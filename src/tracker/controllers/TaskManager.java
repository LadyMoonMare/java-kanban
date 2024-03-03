package tracker.controllers;

import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.util.IdGenerator;

import java.util.HashMap;
import java.util.ArrayList;
public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public Task getTask(int taskId) {
        return tasks.get(taskId);
    }

    public Task addTask(Task newTask) {
        IdGenerator idGenerator = new IdGenerator();
        newTask.setId(idGenerator.id);

        tasks.put(newTask.getId(),newTask);
        return newTask;
    }

    public Task updateTask(Task updatedTask) {
        Task currentTask = tasks.get(updatedTask.getId());

        tasks.put(updatedTask.getId(),currentTask);
        return currentTask;
    }

    public Task removeTask(Integer taskId) {
        Task removedTask = tasks.get(taskId);

        tasks.remove(taskId);
        return removedTask;
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public Epic getEpic(int epicId) {
        return epics.get(epicId);
    }

    public ArrayList<Subtask> getAllSubtasksInEpic(Epic epic) {
        return epic.getSubtasks();
    }

    public Epic addEpic(Epic newEpic) {
        IdGenerator idGenerator = new IdGenerator();

        newEpic.setId(idGenerator.id);
        newEpic.setStatus(newEpic.setEpicStatus());

        epics.put(newEpic.getId(),newEpic);
        return newEpic;
    }

    public Task updateEpic(Epic updatedEpic) {
        Epic currentEpic = epics.get(updatedEpic.getId());

        epics.put(updatedEpic.getId(),currentEpic);
        return currentEpic;
    }

    public Epic removeEpic(Integer epicId) {
        Epic removedEpic = epics.get(epicId);

        epics.remove(epicId);
        return removedEpic;
    }

    public ArrayList<Subtask> getAllSubtasks() {

        return new ArrayList<>(subtasks.values());
    }

    public void removeAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllSubtasks();
            epic.setStatus(epic.setEpicStatus());
        }
    }

    public void removeAllSubtasksInEpic(Epic epic) {
        epic.removeAllSubtasks();
    }

    public Subtask getSubtask(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    public Subtask addSubtask(Subtask newSubtask) {
        IdGenerator idGenerator = new IdGenerator();
        newSubtask.setId(idGenerator.id);

        Epic epic = epics.get(newSubtask.getEpicId());
        epic.addNewSubtask(newSubtask);
        epic.setStatus(epic.setEpicStatus());

        subtasks.put(newSubtask.getId(),newSubtask);
        return newSubtask;
    }

    public Subtask updateSubtask(Subtask updatedSubtask) {
        Subtask currentSubtask = subtasks.get(updatedSubtask.getId());
        Epic currentEpic = epics.get(currentSubtask.getEpicId());

        currentEpic.setStatus(currentEpic.setEpicStatus());
        subtasks.put(updatedSubtask.getId(),currentSubtask);
        return currentSubtask;
    }

    public Subtask removeSubtask(Integer subtaskId) {
        Subtask removedSubtask = subtasks.get(subtaskId);
        Epic epic = epics.get(removedSubtask.getEpicId());

        epic.deleteSubtask(removedSubtask);
        subtasks.remove(subtaskId);
        return removedSubtask;
    }


    @Override
    public String toString() {
        return "TaskManager{" +
                "tasks=" + tasks +
                ", epics=" + epics +
                ", subtasks=" + subtasks +
                '}';
    }
}
