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
}