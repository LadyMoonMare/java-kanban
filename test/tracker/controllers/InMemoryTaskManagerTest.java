package tracker.controllers;

import org.junit.jupiter.api.Test;
import tracker.model.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    @Test
    void shouldCreateTaskAndGenerateId() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", Status.NEW);

        manager.addTask(task);
        assertNotNull(task);

        Task task1 = new Task("Task", "Description", Status.NEW);

        manager.addTask(task1);
        assertNotEquals(task.getId(),task1.getId());
    }

    @Test
    void shouldBeNegativeIfTaskStatusChanged() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", Status.NEW);

        task.setStatus(Status.IN_PROGRESS);

        assertNotEquals(Status.NEW, task.getStatus());
    }

    @Test
    void shouldBeEmptyIfNoTasks() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", Status.NEW);
        ArrayList<Task> refer = new ArrayList<>();

        manager.addTask(task);
        manager.removeTask(task.getId());

        assertEquals(refer,manager.getAllTasks());
    }

    @Test
    void shouldBePositiveIfTaskIdIsNew() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", Status.NEW);

        manager.addTask(task);
        int refer = task.getId();
        manager.addTask(task);

        assertNotEquals(refer,task.getId());
    }

    @Test
    void shouldBePositiveIfTaskIdIsEqual() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", Status.NEW);

        manager.addTask(task);

        assertEquals(task.getId(),manager.getTask(task.getId()).getId());
    }

    @Test
    void shouldCreateEpicAndGenerateId() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Epic", "Description");

        manager.addEpic(epic);
        assertNotNull(epic);
        assertTrue(epic.getSubtasks().isEmpty());
    }

    @Test
    void shouldBeEmptyAndNewIfNoSubtasks() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Epic", "Description");
        ArrayList<Subtask> refer = new ArrayList<>();

        manager.addEpic(epic);

        assertEquals(refer,epic.getSubtasks());
        assertEquals(Status.NEW,epic.getStatus());
    }

    @Test
    void shouldBeNegativeIfEpicIsUpdated() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Epic", "Description");

        manager.addEpic(epic);
        epic.setTaskName("epic");

        assertNotEquals("Epic",epic.getTaskName());
    }

    @Test
    void shouldChangeStatusIfSubtaskStatusUpdated() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Epic", "Description");
        manager.addEpic(epic);
        Subtask subtask0 = new Subtask("Subtask0", "Description", Status.NEW,
                epic.getId());
        Subtask subtask1 = new Subtask("Subtask1", "Description", Status.NEW,
                epic.getId());

        manager.addSubtask(subtask0);
        manager.addSubtask(subtask1);
        assertEquals(Status.NEW,epic.getStatus());

        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);
        assertEquals(Status.IN_PROGRESS,epic.getStatus());

        subtask0.setStatus(Status.DONE);
        manager.updateSubtask(subtask0);
        assertEquals(Status.DONE,epic.getStatus());

        epic.removeAllSubtasks();
        manager.updateEpic(epic);
        assertEquals(Status.NEW,epic.getStatus());
    }

    @Test
    void shouldBePositiveIfEpicIdIsEqual() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Epic", "Description");

        manager.addEpic(epic);

        assertEquals(epic.getId(),manager.getEpic(epic.getId()).getId());
    }

    @Test
    void shouldBePositiveIfSubtaskIdIsEqual() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Epic", "Description");
        manager.addEpic(epic);
        Subtask subtask0 = new Subtask("Subtask0", "Description", Status.NEW,
                epic.getId());

        manager.addSubtask(subtask0);

        assertEquals(subtask0.getId(),manager.getSubtask(subtask0.getId()).getId());
    }

    @Test
    void shouldBePositiveIfCanGetAllTypesOfTasks() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", Status.NEW);
        manager.addTask(task);
        Epic epic = new Epic("Epic", "Description");
        manager.addEpic(epic);
        Subtask subtask0 = new Subtask("Subtask0", "Description", Status.NEW,
                epic.getId());
        manager.addSubtask(subtask0);

        assertNotNull(manager.getTask(task.getId()));
        assertEquals(task,manager.getTask(task.getId()));

        assertNotNull(manager.getEpic(epic.getId()));
        assertEquals(epic,manager.getEpic(epic.getId()));

        assertNotNull(manager.getSubtask(subtask0.getId()));
        assertEquals(subtask0,manager.getSubtask(subtask0.getId()));
    }

    @Test
    void shouldBePositiveIfTasksFieldsAreConstant() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", Status.NEW);
        String nameRefer = task.getTaskName();
        String referDescription = task.getTaskDescription();
        Status referStatus = task.getStatus();

        manager.addTask(task);

        assertEquals(nameRefer,manager.getTask(task.getId()).getTaskName());
        assertEquals(referDescription,manager.getTask(task.getId()).getTaskDescription());
        assertEquals(referStatus,manager.getTask(task.getId()).getStatus());
    }

    @Test
    void shouldBePositiveIfDeletionSuccess() {
        TaskManager manager = Managers.getDefault();
        HistoryManager manager1 = Managers.getDefaultHistory();

        Task task = new Task("Task", "Description", Status.NEW);
        manager.addTask(task);
        Epic epic = new Epic("Epic", "Description");
        manager.addEpic(epic);
        Subtask subtask0 = new Subtask("Subtask0", "Description", Status.NEW,
                epic.getId());
        manager.addSubtask(subtask0);

        manager.getTask(task.getId());
        manager.getEpic(epic.getId());
        manager.getSubtask(subtask0.getId());

        manager.removeTask(task.getId());
        assertNull(manager.getTask(task.getId()));

        manager.removeSubtask(subtask0.getId());
        assertNull(manager.getSubtask(subtask0.getId()));

        manager.removeEpic(epic.getId());
        assertNull(manager.getEpic(epic.getId()));
        assertTrue(manager.getAllSubtasksInEpic(epic).isEmpty());
    }
}