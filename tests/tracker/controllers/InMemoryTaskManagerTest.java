package tracker.controllers;

import org.junit.jupiter.api.Test;
import tracker.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    @Test
    public void shouldCreateTaskAndGenerateId() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", Status.NEW);

        manager.addTask(task);

        assertNotNull(task.getId());
    }

    @Test
    public void shouldBeNegativeIfTaskStatusChanged() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", Status.NEW);

        task.setStatus(Status.IN_PROGRESS);

        assertNotEquals(Status.NEW, task.getStatus());
    }

    @Test
    public void shouldBeEmptyIfNoTasks() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", Status.NEW);
        ArrayList<Task> refer = new ArrayList<>();

        manager.addTask(task);
        manager.removeTask(task.getId());

        assertEquals(refer,manager.getAllTasks());
    }

    @Test
    public void shouldBePositiveIfTaskIdIsNew() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", Status.NEW);

        manager.addTask(task);
        int refer = task.getId();
        manager.addTask(task);

        assertNotEquals(refer,task.getId());
    }

    @Test
    public void shouldBePositiveIfTaskIdIsEqual() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", Status.NEW);

        manager.addTask(task);

        assertEquals(task.getId(),manager.getTask(task.getId()).getId());
    }

    @Test
    public void shouldCreateEpicAndGenerateId() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Epic", "Description");

        manager.addEpic(epic);

        assertNotNull(epic.getId());
    }

    @Test
    public void shouldBeEmptyAndNewIfNoSubtasks() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Epic", "Description");
        ArrayList<Subtask> refer = new ArrayList<>();

        manager.addEpic(epic);

        assertEquals(refer,epic.getSubtasks());
        assertEquals(Status.NEW,epic.getStatus());
    }

    @Test
    public void shouldBeNegativeIfEpicIsUpdated() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Epic", "Description");

        manager.addEpic(epic);
        epic.setTaskName("epic");

        assertNotEquals("Epic",epic.getTaskName());
    }

    @Test
    public void shouldChangeStatusIfSubtaskStatusUpdated() {
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
    public void shouldBePositiveIfEpicIdIsEqual() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Epic", "Description");

        manager.addEpic(epic);

        assertEquals(epic.getId(),manager.getEpic(epic.getId()).getId());
    }

    @Test
    public void shouldBePositiveIfSubtaskIdIsEqual() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Epic", "Description");
        manager.addEpic(epic);
        Subtask subtask0 = new Subtask("Subtask0", "Description", Status.NEW,
                epic.getId());

        manager.addSubtask(subtask0);

        assertEquals(subtask0.getId(),manager.getSubtask(subtask0.getId()).getId());
    }

    @Test
    public void shouldBePositiveIfCanGetAllTypesOfTasks() {
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
    public void shouldBePositiveIfTasksFieldsAreConstant() {
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

}