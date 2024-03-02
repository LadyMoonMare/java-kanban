import java.util.HashMap;
import java.util.ArrayList;
public class TaskManager {

    public HashMap<Integer, Task> taskHashMap = new HashMap<>();
    public HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    public HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();

    public HashMap<Integer, Task> getAllTasks() {
        return taskHashMap;
    }

    public void removeAllTasks() {
        taskHashMap.clear();
    }

    public Task getTask(int taskId) {
        return taskHashMap.get(taskId);
    }

    public Task addTask(Task newTask) {
        IdGenerator idGenerator = new IdGenerator();

        newTask.setId(idGenerator.id);
        newTask.setStatus(Status.NEW);

        taskHashMap.put(newTask.getId(),newTask);
        return newTask;
    }

    public Task updateTask(Task updatedTask) {
        Task currentTask = taskHashMap.get(updatedTask.getId());

        taskHashMap.put(updatedTask.getId(),currentTask);
        return currentTask;
    }

    public Task removeTask(Integer taskId) {
        Task removedTask = taskHashMap.get(taskId);

        taskHashMap.remove(taskId);
        return removedTask;
    }

    public HashMap<Integer, Epic> getAllEpics() {
        return epicHashMap;
    }

    public void removeAllEpics() {
        epicHashMap.clear();
    }

    public Epic getEpic(int epicId) {
        return epicHashMap.get(epicId);
    }

    public ArrayList<Subtask> getAllSubtasksInEpic(Epic epic) {
        return epic.getSubtasks();
    }

    public Epic addEpic(Epic newEpic) {
        IdGenerator idGenerator = new IdGenerator();

        newEpic.setId(idGenerator.id);

        if (newEpic.isStatusNew()) {
            newEpic.setStatus(Status.NEW);
        } else {
            newEpic.setStatus(Status.IN_PROGRESS);
        }

        epicHashMap.put(newEpic.getId(),newEpic);
        return newEpic;
    }

    public Task updateEpic(Epic updatedEpic) {
        Epic currentEpic = epicHashMap.get(updatedEpic.getId());

        epicHashMap.put(updatedEpic.getId(),currentEpic);
        return currentEpic;
    }

    public Epic removeEpic(Integer epicId) {
        Epic removedEpic = epicHashMap.get(epicId);

        epicHashMap.remove(epicId);
        return removedEpic;
    }

    public HashMap<Integer, Subtask> getAllSubtasks() {
        return subtaskHashMap;
    }

    public void removeAllSubtasks() {
        subtaskHashMap.clear();
    }

    public void removeAllSubtasksInEpic(Epic epic) {
        epic.removeAllSubtasks();
    }

    public Subtask getSubtask(int subtaskId) {
        return subtaskHashMap.get(subtaskId);
    }

    public Subtask addSubtask(Subtask newSubtask) {
        IdGenerator idGenerator = new IdGenerator();

        newSubtask.setId(idGenerator.id);
        newSubtask.setStatus(Status.NEW);

        Epic epic = epicHashMap.get(newSubtask.getEpicId());
        epic.addNewSubtask(newSubtask);

        subtaskHashMap.put(newSubtask.getId(),newSubtask);
        return newSubtask;
    }

    public Subtask updateSubtask(Subtask updatedSubtask) {
        Subtask currentSubtask = subtaskHashMap.get(updatedSubtask.getId());
        Epic currentEpic = epicHashMap.get(currentSubtask.getEpicId());

        if (currentEpic.isStatusDone()) {
            currentEpic.setStatus(Status.DONE);
        } else if (currentEpic.isStatusNew()) {
            currentEpic.setStatus(Status.NEW);
        } else {
            currentEpic.setStatus(Status.IN_PROGRESS);
        }
        subtaskHashMap.put(updatedSubtask.getId(),currentSubtask);
        return currentSubtask;
    }

    public Subtask removeSubtask(Integer subtaskId) {
        Subtask removedSubtask = subtaskHashMap.get(subtaskId);
        Epic epic = epicHashMap.get(removedSubtask.getEpicId());

        epic.deleteSubtask(removedSubtask);
        subtaskHashMap.remove(subtaskId);
        return removedSubtask;
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "taskHashMap=" + taskHashMap +
                '}';
    }
}
