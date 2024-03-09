package tracker.controllers;

import org.junit.jupiter.api.Test;
import tracker.model.Task;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void shouldBePositiveIfManagerIsCompleted() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task","TaskD");
        manager.addTask(task);

        assertNotNull(manager);
        assertNotNull(manager.getTask(task.getId()));
        assertNotNull(manager.getHistory());
    }
}