package tracker.controllers;

import tracker.exceptions.TaskValidTimeException;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.util.IdGenerator;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    public InMemoryTaskManager() {

    }

    protected HistoryManager manager = Managers.getDefaultHistory();
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    Comparator<Task> comparator = (t1, t2) -> {
        if (t1.getStartTime().isBefore(t2.getStartTime())) {
            return -1;
        } else if (t2.getStartTime().isBefore(t1.getStartTime())) {
            return 1;
        } else {
            return 0;
        }
    };
    private SortedSet<Task> prioritizedTasks =  new TreeSet<>(comparator);


    public InMemoryTaskManager(HistoryManager manager) {
        this.manager = manager;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void removeAllTasks() {
        tasks.entrySet().stream()
                .forEach(task -> {
                    manager.remove(task.getKey());
                    prioritizedTasks.remove(task.getValue());
                });
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
    public void addTask(Task newTask) throws TaskValidTimeException {
        IdGenerator idGenerator = new IdGenerator();
        newTask.setId(idGenerator.getId());
        if (newTask.getStartTime() == null) {
            tasks.put(newTask.getId(),newTask);
        } else {
            if (!isNotValid(newTask)) {
                prioritizedTasks.add(newTask);
                tasks.put(newTask.getId(),newTask);
            } else {
                throw new TaskValidTimeException(newTask);
            }
        }
    }

    @Override
    public void addTask(Task newTask,Integer id) throws TaskValidTimeException {
        if (newTask.getStartTime() == null) {
            tasks.put(newTask.getId(),newTask);
        } else {
            if (!isNotValid(newTask)) {
                prioritizedTasks.add(newTask);
                tasks.put(newTask.getId(),newTask);
            } else {
                throw new TaskValidTimeException(newTask);
            }
        }
    }

    @Override
    public void updateTask(Task updatedTask) throws TaskValidTimeException {
        Task currentTask = tasks.get(updatedTask.getId());
        if (currentTask.getStartTime() == null) {
            tasks.put(currentTask.getId(),currentTask);
        } else {
            if (!isNotValid(currentTask)) {
                prioritizedTasks.add(currentTask);
                tasks.put(currentTask.getId(),currentTask);
            } else {
                throw new TaskValidTimeException(currentTask);
            }
        }
    }

    @Override
    public void removeTask(Integer taskId) {
        tasks.remove(taskId);
        prioritizedTasks.remove(tasks.get(taskId));
        manager.remove(taskId);
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllEpics() {
        epics.keySet().stream()
                .forEach(e -> manager.remove(e));
        epics.clear();

        subtasks.entrySet().stream()
                .forEach(s -> {
                    manager.remove(s.getKey());
                    prioritizedTasks.remove(s.getValue());
                });
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
        if (!newEpic.getSubtasks().isEmpty()) {
            newEpic.getEndTime();
            newEpic.getDuration();
        }

        epics.put(newEpic.getId(),newEpic);
    }

    @Override
    public void addEpic(Epic newEpic, Integer id) {
        newEpic.setStatus(newEpic.setEpicStatus());
        if (!newEpic.getSubtasks().isEmpty()) {
            newEpic.getEndTime();
            newEpic.getDuration();
        }
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

        removedEpic.getSubtasks().stream()
                .forEach(s -> {
                    subtasks.remove(s.getId());
                    prioritizedTasks.remove(s);
                    manager.remove(s.getId());
                });
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
        subtasks.entrySet().stream()
                .forEach(s -> {
                    manager.remove(s.getKey());
                    prioritizedTasks.remove(s.getValue());
                });
        subtasks.clear();

        epics.values().stream()
                .forEach(e -> {
                    e.removeAllSubtasks();
                    e.setStatus(e.setEpicStatus());
                    e.getEndTime();
                    e.getDuration();
                });
    }

    @Override
    public void removeAllSubtasksInEpic(Epic epic) {
        epic.getSubtasks().stream()
                .forEach(s -> {
                    manager.remove(s.getId());
                    prioritizedTasks.remove(s);
                });
        epic.removeAllSubtasks();

        epic.setStatus(epic.setEpicStatus());
        epic.getEndTime();
        epic.getDuration();
    }

    @Override
    public Subtask getSubtask(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            manager.add(subtasks.get(subtaskId));
        }
        return subtasks.get(subtaskId);
    }

    @Override
    public void addSubtask(Subtask newSubtask) throws TaskValidTimeException {
        IdGenerator idGenerator = new IdGenerator();
        newSubtask.setId(idGenerator.getId());
        Epic epic = epics.get(newSubtask.getEpicId());
        epic.addNewSubtask(newSubtask);
        epic.setStatus(epic.setEpicStatus());
        if (newSubtask.getStartTime() == null) {
            subtasks.put(newSubtask.getId(),newSubtask);
        } else {
            if (!isNotValid(newSubtask)) {
                prioritizedTasks.add(newSubtask);
                subtasks.put(newSubtask.getId(),newSubtask);
                epic.getEndTime();
                epic.getDuration();
            } else {
                throw new TaskValidTimeException(newSubtask);
            }
        }
    }

    @Override
    public void addSubtask(Subtask newSubtask,Integer id) throws TaskValidTimeException {
        Epic epic = epics.get(newSubtask.getEpicId());
        epic.addNewSubtask(newSubtask);
        epic.setStatus(epic.setEpicStatus());
        if (newSubtask.getStartTime() == null) {
            subtasks.put(newSubtask.getId(),newSubtask);
        } else {
            if (!isNotValid(newSubtask)) {
                prioritizedTasks.add(newSubtask);
                subtasks.put(newSubtask.getId(),newSubtask);
                epic.getEndTime();
                epic.getDuration();
            } else {
                throw new TaskValidTimeException(newSubtask);
            }
        }
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) throws TaskValidTimeException {
        Subtask currentSubtask = subtasks.get(updatedSubtask.getId());
        Epic currentEpic = epics.get(currentSubtask.getEpicId());

        currentEpic.setStatus(currentEpic.setEpicStatus());
        if (currentSubtask.getStartTime() == null) {
            subtasks.put(currentSubtask.getId(),currentSubtask);
        } else {
            if (!isNotValid(currentSubtask)) {
                prioritizedTasks.add(currentSubtask);
                subtasks.put(currentSubtask.getId(),currentSubtask);
                currentEpic.getEndTime();
                currentEpic.getDuration();
            } else {
                throw new TaskValidTimeException(currentSubtask);
            }
        }
    }

    @Override
    public void removeSubtask(Integer subtaskId) {
        Subtask removedSubtask = subtasks.get(subtaskId);

        Epic epic = epics.get(removedSubtask.getEpicId());
        epic.deleteSubtask(removedSubtask);
        epic.setStatus(epic.setEpicStatus());
        epic.getEndTime();
        epic.getDuration();

        subtasks.remove(subtaskId);
        prioritizedTasks.remove(removedSubtask);
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

    public SortedSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public boolean isNotValid(Task task) {
        if (!prioritizedTasks.isEmpty()) {
            for (Task t : getPrioritizedTasks()) {
                if (t.getStartTime().isAfter(task.getStartTime()) &&
                        t.getStartTime().isBefore(task.getEndTime()) ||
                        t.getEndTime().isAfter(task.getStartTime())
                                && t.getEndTime().isBefore(task.getEndTime()) ||
                        t.getStartTime().isBefore(task.getStartTime())
                                && t.getEndTime().isAfter(task.getEndTime())) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
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
