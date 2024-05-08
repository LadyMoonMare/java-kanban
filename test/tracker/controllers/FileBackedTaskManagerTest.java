package tracker.controllers;

import org.junit.jupiter.api.Test;
import tracker.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest {
        @Override
        protected TaskManager createTaskManager() {
                return new FileBackedTaskManager(new File( "./test/resources/empty.csv").toPath());
        }

    @Test
    void shouldBePositiveIfEmptyFileSaveAndLoadSuccess () throws IOException {
            File file = new File( "./test/resources/empty.csv");
            File file1 = new File( "./test/resources/empty1.csv");
            FileBackedTaskManager fb = new FileBackedTaskManager(file.toPath());
            assertNotNull(fb);
            FileBackedTaskManager fb1 = FileBackedTaskManager.loadFromFile(file1);
            assertNotNull(fb1);
    }

    @Test
    void shouldBePositiveIfTasksSaveIsCorrect() throws IOException {
            File file =  new File( "./test/resources/save.csv");
            FileBackedTaskManager fb = new FileBackedTaskManager(file.toPath());
            Task task = new Task("task1", "desc1", Status.NEW,
                    LocalDateTime.parse("12:30 20.04.24",
                            DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")), Duration.ofMinutes(40));
            Epic epic = new Epic("epic1","epicDesk",
                    LocalDateTime.parse("12:40 20.04.24",
                            DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")), Duration.ofMinutes(50));
            final String COLUMNS = "id, type, name, status, description, epic, start, duration";

            fb.addTask(task);
            fb.getTask(task.getId());
            fb.addEpic(epic);
            fb.getEpic(epic.getId());
            Subtask subtask = new Subtask("subtask1", "subtaskDesc",
                    Status.NEW, epic.getId(), LocalDateTime.parse("12:40 21.04.24",
                    DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")), Duration.ofMinutes(60));
            assertNotNull(fb);

            fb.addSubtask(subtask);
            fb.getSubtask(subtask.getId());

            String refer = COLUMNS;
            refer = refer + String.valueOf(task.getId()) + "," + TaskType.TASK + ","  + task.getTaskName()
                    + "," + task.getStatus() + ","  + task.getTaskDescription() + ", ,"
                    + task.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")) + ","
                    + task.getDuration().toMinutes();
            refer = refer + String.valueOf(epic.getId()) + ","  + TaskType.EPIC + ","  + epic.getTaskName()
                    + "," + epic.getStatus() + ","  + epic.getTaskDescription() + ", ,"
                    + epic.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")) + ","
                    + epic.getDuration().toMinutes();
            refer = refer + String.valueOf(subtask.getId()) + ","  + TaskType.SUBTASK + ","
                    + subtask.getTaskName() + ","  + subtask.getStatus() + ","  +
                    subtask.getTaskDescription() + ","  + String.valueOf(subtask.getEpicId()) + ","
                    + subtask.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")) + ","
                    + subtask.getDuration().toMinutes();;
            StringBuilder history = new StringBuilder();
            for (Task t : fb.manager.getHistory()) {
                 history.append(t.getId()).append(",");
            }
            refer = refer + history;

            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder test = new StringBuilder();
            while (br.ready()) {
                test.append(br.readLine());
            }
            br.close();
            assertEquals(refer, test.toString());

            fb.removeEpic(epic.getId());
            refer = COLUMNS + String.valueOf(task.getId()) + "," + TaskType.TASK + "," + task.getTaskName()
                    + ","+ task.getStatus() + "," + task.getTaskDescription() + ", ,"
                    + task.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")) + ","
                    + task.getDuration().toMinutes();
            history = new StringBuilder();
            for (Task t : fb.manager.getHistory()) {
                    history.append(t.getId()).append(",");
            }
            refer = refer + history;

            br = new BufferedReader(new FileReader(file));
            test = new StringBuilder();
            while (br.ready()) {
                test.append(br.readLine());
            }
            br.close();
            assertEquals(refer, test.toString());

            task.setStatus(Status.DONE);
            fb.updateTask(task);
            refer = COLUMNS + String.valueOf(task.getId()) + "," + TaskType.TASK + "," + task.getTaskName()
                    + ","+ Status.DONE + "," + task.getTaskDescription() + ", ,"
                    + task.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")) + ","
                    + task.getDuration().toMinutes();
            history = new StringBuilder();
            for (Task t : fb.manager.getHistory()) {
                    history.append(t.getId()).append(",");
            }
            refer = refer + history;

            br = new BufferedReader(new FileReader(file));
            test = new StringBuilder();
            while (br.ready()) {
                test.append(br.readLine());
            }
            br.close();
            assertEquals(refer, test.toString());
    }

    @Test
    void shouldBePositiveIfLoadSuccess() throws IOException {
            File file = new File( "./test/resources/load.csv");
            FileBackedTaskManager fb = new FileBackedTaskManager(file.toPath());
            Task task = new Task("task1", "desc1", Status.NEW,
                    LocalDateTime.parse("12:30 20.04.24",
                            DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")), Duration.ofMinutes(40));
            Epic epic = new Epic("epic1","epicDesk",
                    LocalDateTime.parse("12:40 20.04.24",
                            DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")), Duration.ofMinutes(50));
            fb.addTask(task);
            fb.getTask(task.getId());
            fb.addEpic(epic);
            fb.getEpic(epic.getId());

            Subtask subtask = new Subtask("subtask1", "subtaskDesc",
                    Status.NEW, epic.getId(), LocalDateTime.parse("12:40 21.04.24",
                    DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")), Duration.ofMinutes(60));
            assertNotNull(fb);
            assertNotNull(epic.getStatus());

            fb.addSubtask(subtask);
            fb.getSubtask(subtask.getId());

            FileBackedTaskManager fb1 = FileBackedTaskManager.loadFromFile(file);
            assertEquals(fb.getTasks().toString(), fb1.getTasks().toString());
            assertEquals(fb.getEpics().toString(), fb1.getEpics().toString());
            assertEquals(fb.getSubtasks().toString(), fb1.getSubtasks().toString());
            assertEquals(fb.manager.getHistory().toString(), fb1.manager.getHistory().toString());

    }
}
