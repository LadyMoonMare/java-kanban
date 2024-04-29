package tracker.controllers;

import org.junit.jupiter.api.Test;
import tracker.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    @Test
    void shouldBePositiveIfEmptyFileSaveAndLoadSuccess () throws IOException {
        try {
            File file = File.createTempFile("temp","csv", new File( "/resources") );
            File file1 = File.createTempFile("temp1","csv", new File( "/resources") );
            FileBackedTaskManager fb = new FileBackedTaskManager(file.toPath());
            assertNotNull(fb);
            FileBackedTaskManager fb1 = FileBackedTaskManager.loadFromFile(file1);
            assertNotNull(fb1);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    @Test
    void shouldBePositiveIfTasksSaveIsCorrect() throws IOException {
        try {
            File file = File.createTempFile("temp","csv", new File( "/resources") );
            Task task = new Task("task1", "desc1");
            Epic epic = new Epic("epic1","epicDesk");
            Subtask subtask = new Subtask("subtask1", "subtaskDesc",
                    Status.NEW, epic.getId());
            FileBackedTaskManager fb = new FileBackedTaskManager(file.toPath());
            assertNotNull(fb);

            fb.addTask(task);
            fb.getTask(task.getId());
            fb.addEpic(epic);
            fb.getEpic(epic.getId());
            fb.addSubtask(subtask);
            fb.getSubtask(subtask.getId());

            String refer = String.valueOf(task.getId()) + TaskType.TASK + task.getTaskName()
                    + task.getStatus() + task.getTaskDescription();
            refer = refer + String.valueOf(epic.getId()) + TaskType.EPIC + epic.getTaskName()
                    + epic.getStatus() + epic.getTaskDescription();
            refer = refer + String.valueOf(subtask.getId()) + TaskType.SUBTASK + subtask.getTaskName()
                    + subtask.getStatus() + subtask.getTaskDescription()
                    + String.valueOf(subtask.getEpicId());
            refer = refer + String.valueOf(task.getId()) + String.valueOf(epic.getId())
                    + String.valueOf(subtask.getId());
            BufferedReader bf = new BufferedReader(new FileReader(file));
            String test = "";
            while (bf.ready()) {
                test = bf.readLine();
            }
            bf.close();
            assertEquals(refer,test);

            fb.removeEpic(epic.getId());
            refer = String.valueOf(task.getId()) + TaskType.TASK + task.getTaskName()
                    + task.getStatus() + task.getTaskDescription() + String.valueOf(task.getId());

            bf = new BufferedReader(new FileReader(file));
            test = "";
            while (bf.ready()) {
                test = bf.readLine();
            }
            bf.close();
            assertEquals(refer,test);

            task.setStatus(Status.DONE);
            fb.updateTask(task);
            refer = String.valueOf(task.getId()) + TaskType.TASK + task.getTaskName()
                    + Status.DONE + task.getTaskDescription() + String.valueOf(task.getId());

            bf = new BufferedReader(new FileReader(file));
            test = "";
            while (bf.ready()) {
                test = bf.readLine();
            }
            bf.close();
            assertEquals(refer,test);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    @Test
    void Test3() throws IOException {
        try {
            File file = File.createTempFile("temp","csv", new File( "/resources") );
            Task task = new Task("task1", "desc1");
            Epic epic = new Epic("epic1","epicDesk");
            Subtask subtask = new Subtask("subtask1", "subtaskDesc",
                    Status.NEW, epic.getId());
            FileBackedTaskManager fb = new FileBackedTaskManager(file.toPath());
            assertNotNull(fb);

            fb.addTask(task);
            fb.getTask(task.getId());
            fb.addEpic(epic);
            fb.getEpic(epic.getId());
            fb.addSubtask(subtask);
            fb.getSubtask(subtask.getId());

            FileBackedTaskManager fb1 = FileBackedTaskManager.loadFromFile(file);
            assertEquals(fb.getTasks(), fb1.getTasks());
            assertEquals(fb.getEpics(), fb1.getEpics());
            assertEquals(fb.getSubtasks(), fb1.getSubtasks());
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}
