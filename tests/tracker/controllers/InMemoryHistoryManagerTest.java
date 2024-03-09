package tracker.controllers;

import org.junit.jupiter.api.Test;
import tracker.model.Status;
import tracker.model.Task;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class InMemoryHistoryManagerTest {
    @Test
    public void shouldSaveAllCopiesOfObjectInHistory() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", Status.NEW);
        manager.addTask(task);
        manager.getTask(task.getId());

        List<Task> refer = manager.getHistory();
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);
        manager.getTask(task.getId());

        assertNotEquals(refer, manager.getHistory());
    }

    @Test
    public void shouldBePositiveIfHistorySizeIs10AndLess() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", Status.NEW);
        manager.addTask(task);
        for (int i = 0; i < 9; i++) {
            manager.getTask(task.getId());
        }

        assertEquals(9, manager.getHistory().size());

        manager.getTask(task.getId());
        assertEquals(10, manager.getHistory().size());

        manager.getTask(task.getId());
        assertEquals(10, manager.getHistory().size());
    }
}