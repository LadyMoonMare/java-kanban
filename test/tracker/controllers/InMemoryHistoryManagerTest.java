package tracker.controllers;

import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.Subtask;
import tracker.model.Task;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class InMemoryHistoryManagerTest {

    @Test
    void shouldCreateHistory() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", Status.NEW);
        manager.addTask(task);

        assertTrue(manager.getHistory().isEmpty());

        manager.getTask(task.getId());
        Task[] referArray = {task};
        assertNotNull(manager.getHistory());
        assertArrayEquals(referArray,manager.getHistory().toArray());
    }

    @Test
     void shouldSaveOnlyOneCopyOfObjectInHistory() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", Status.NEW);
        manager.addTask(task);

        manager.getTask(task.getId());
        List<Task> refer = manager.getHistory();
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);
        manager.getTask(task.getId());

        assertEquals(refer, manager.getHistory());
    }

    @Test
    void shouldBePositiveIfClearanceSuccess() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", Status.NEW);
        Task task1 = new Task("Task1", "Description", Status.NEW);
        Epic epic = new Epic("Epic", "Description");
        manager.addEpic(epic);
        Subtask subtask0 = new Subtask("Subtask0", "Description", Status.NEW,
                epic.getId());

        manager.addTask(task);
        manager.addTask(task1);
        manager.addSubtask(subtask0);

        Task[] referArray = {task1, task, epic, subtask0};
        manager.getTask(task.getId());
        manager.getTask(task1.getId());
        manager.getTask(task1.getId());
        manager.getTask(task.getId());
        manager.getEpic(epic.getId());
        manager.getEpic(epic.getId());
        manager.getSubtask(subtask0.getId());
        manager.getSubtask(subtask0.getId());


        assertArrayEquals(referArray, manager.getHistory().toArray());

        manager.removeTask(task.getId());
        Task[] newReferArray = {task1, epic, subtask0};
        assertArrayEquals(newReferArray, manager.getHistory().toArray());

        manager.removeTask(task1.getId());
        Task[] referEpicArray = {epic, subtask0};
        assertArrayEquals(referEpicArray, manager.getHistory().toArray());

        manager.removeSubtask(subtask0.getId());
        Task[] newReferEpicArray = {epic};
        assertArrayEquals(newReferEpicArray, manager.getHistory().toArray());
        assertTrue(epic.getSubtasks().isEmpty());

        manager.removeEpic(epic.getId());
        assertTrue(manager.getHistory().isEmpty());
    }
}