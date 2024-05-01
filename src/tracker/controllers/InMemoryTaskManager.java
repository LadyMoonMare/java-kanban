package tracker.controllers;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.util.IdGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class InMemoryTaskManager implements TaskManager {
    public InMemoryTaskManager() {

    }

    protected HistoryManager manager = Managers.getDefaultHistory();
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();

    public InMemoryTaskManager(HistoryManager manager) {
        this.manager = manager;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void removeAllTasks() {
        for (Integer taskId : tasks.keySet()) {
            manager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public Task getTask(int taskId) {
        if (tasks.containsKey(taskId)) {
           manager.add(tasks.get(taskId));
        }
        return tasks.get(taskId);
    }

    @Override
    public void addTask(Task newTask) {
        IdGenerator idGenerator = new IdGenerator();
        newTask.setId(idGenerator.getId());

        tasks.put(newTask.getId(),newTask);
    }

    @Override
    public void addTask(Task newTask,Integer id) {
        tasks.put(id, newTask);
    }

    @Override
    public void updateTask(Task updatedTask) {
        Task currentTask = tasks.get(updatedTask.getId());

        tasks.put(updatedTask.getId(),currentTask);
    }

    @Override
    public void removeTask(Integer taskId) {
        tasks.remove(taskId);
        manager.remove(taskId);
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllEpics() {
        for (Integer taskId : epics.keySet()) {
            manager.remove(taskId);
        }
        epics.clear();

        for (Integer taskId : subtasks.keySet()) {
            manager.remove(taskId);
        }
        subtasks.clear();
    }

    @Override
    public Epic getEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            manager.add(epics.get(epicId));
        }
        return epics.get(epicId);
    }

    @Override
    public List<Subtask> getAllSubtasksInEpic(Epic epic) {
        return epic.getSubtasks();
    }

    @Override
    public void addEpic(Epic newEpic) {
        IdGenerator idGenerator = new IdGenerator();

        newEpic.setId(idGenerator.getId());
        newEpic.setStatus(newEpic.setEpicStatus());

        epics.put(newEpic.getId(),newEpic);
    }

    @Override
    public void addEpic(Epic newEpic, Integer id) {
        newEpic.setStatus(newEpic.setEpicStatus());
        epics.put(id,newEpic);
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        Epic currentEpic = epics.get(updatedEpic.getId());
        currentEpic.setStatus(currentEpic.setEpicStatus());

        epics.put(updatedEpic.getId(),currentEpic);
    }

    @Override
    public void removeEpic(Integer epicId) {
        Epic removedEpic = epics.get(epicId);

        for (Task subtask : removedEpic.getSubtasks()) {
            subtasks.remove(subtask.getId());
            manager.remove(subtask.getId());
        }
        removedEpic.removeAllSubtasks();
        epics.remove(epicId);
        manager.remove(epicId);
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {

        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeAllSubtasks() {
        for (Integer taskId : subtasks.keySet()) {
            manager.remove(taskId);
        }
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.removeAllSubtasks();
            epic.setStatus(epic.setEpicStatus());
        }
    }

    @Override
    public void removeAllSubtasksInEpic(Epic epic) {
        for (Task subtask : epic.getSubtasks()) {
            manager.remove(subtask.getId());
        }
        epic.removeAllSubtasks();

        epic.setStatus(epic.setEpicStatus());
    }

    @Override
    public Subtask getSubtask(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            manager.add(subtasks.get(subtaskId));
        }
        return subtasks.get(subtaskId);
    }

    @Override
    public void addSubtask(Subtask newSubtask) {
        IdGenerator idGenerator = new IdGenerator();
        newSubtask.setId(idGenerator.getId());
        Epic epic = epics.get(newSubtask.getEpicId());
        epic.addNewSubtask(newSubtask);
        epic.setStatus(epic.setEpicStatus());

        subtasks.put(newSubtask.getId(),newSubtask);
    }

    @Override
    public void addSubtask(Subtask newSubtask,Integer id) {
        Epic epic = epics.get(newSubtask.getEpicId());
        epic.addNewSubtask(newSubtask);
        epic.setStatus(epic.setEpicStatus());

        subtasks.put(id,newSubtask);
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        Subtask currentSubtask = subtasks.get(updatedSubtask.getId());
        Epic currentEpic = epics.get(currentSubtask.getEpicId());

        currentEpic.setStatus(currentEpic.setEpicStatus());
        subtasks.put(updatedSubtask.getId(),currentSubtask);
    }

    @Override
    public void removeSubtask(Integer subtaskId) {
        Subtask removedSubtask = subtasks.get(subtaskId);

        Epic epic = epics.get(removedSubtask.getEpicId());
        epic.deleteSubtask(removedSubtask);
        epic.setStatus(epic.setEpicStatus());

        subtasks.remove(subtaskId);
        manager.remove(subtaskId);
    }

    @Override
    public List<Task> getHistory() {
        return manager.getHistory();
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
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
