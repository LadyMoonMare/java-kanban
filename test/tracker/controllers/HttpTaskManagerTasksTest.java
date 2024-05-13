package tracker.controllers;
import com.google.gson.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import tracker.model.*;
import tracker.server.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

 class HttpTaskManagerTasksTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
    }
    @BeforeEach
    public void setUp() {
        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 1", tasksFromManager.get(0).getTaskName(), "Некорректное имя задачи");
    }

     @Test
     void testUpdateTask() throws IOException, InterruptedException {
         Task task = new Task("Test 2", "Testing task 1",
                 Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addTask(task);
         task.setStatus(Status.IN_PROGRESS);
         String taskJson = gson.toJson(task);

         HttpClient client = HttpClient.newBuilder()
                 .version(HttpClient.Version.HTTP_1_1)
                 .build();

         URI url = URI.create("http://localhost:8080/tasks");
         HttpRequest request = HttpRequest.newBuilder().
                 uri(url).
                 POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(201, response.statusCode());

         List<Task> tasksFromManager = manager.getAllTasks();

         assertNotNull(tasksFromManager, "Задачи не возвращаются");
         assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
         assertEquals("IN_PROGRESS", tasksFromManager.get(0).getStatus().toString(), "Некорректное имя задачи");
     }

     @Test
     void testAddInvalidTimeTask() throws IOException, InterruptedException {
         Task task = new Task("Task", "Description", Status.NEW,
                 LocalDateTime.parse("12:30 20.04.24",
                         DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")), Duration.ofMinutes(40));
         manager.addTask(task);
         Task task1 = new Task("Task1", "Description", Status.NEW,
                 LocalDateTime.parse("12:30 20.04.24",
                         DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")), Duration.ofMinutes(10));
         String taskJson = gson.toJson(task1);

         HttpClient client = HttpClient.newBuilder()
                 .version(HttpClient.Version.HTTP_1_1)
                 .build();

         URI url = URI.create("http://localhost:8080/tasks");
         HttpRequest request = HttpRequest.newBuilder().
                 uri(url).
                 POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(406, response.statusCode());
     }

     @Test
     void testGetTask() throws IOException, InterruptedException {
         Task task = new Task("Test 1", "Testing task 1",
                 Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addTask(task);
         String taskJson = gson.toJson(task);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/tasks");
         HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(200, response.statusCode());

         assertNotNull(manager.getHistory(), "Задачи не возвращаются");
         assertEquals("["+taskJson+"]",response.body(), "Некорректный вывод задач");
     }

     @Test
     void testGetTaskByID() throws IOException, InterruptedException {
         Task task = new Task("Test 2", "Testing task 2",
                 Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addTask(task);
         String taskJson = gson.toJson(task);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
         HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(200, response.statusCode());

         assertNotNull(manager.getHistory(), "Задачи не возвращаются");
         assertEquals(taskJson,response.body(), "Некорректный вывод задач");
     }

     @Test
     void testGetTaskByNonExistentID() throws IOException, InterruptedException {
         Task task = new Task("Test 3", "Testing task 3",
                 Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addTask(task);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/tasks/" + 10);
         HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(404, response.statusCode());
     }

     @Test
     void testDeleteTask() throws IOException, InterruptedException {
         Task task = new Task("Test 4", "Testing task 4",
                 Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addTask(task);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
         HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(201, response.statusCode());

         List<Task> tasksFromManager = manager.getAllTasks();

         assertTrue(tasksFromManager.isEmpty(), "Задача не удалена");
         assertTrue(manager.getHistory().isEmpty(), "Задача не удалена из истории");
         assertTrue(manager.getPrioritizedTasks().isEmpty(), "Задача не удалена из списка приоритетных");
     }

     @Test
     void testDeleteNonExistentTask() throws IOException, InterruptedException {
         Task task = new Task("Test 5", "Testing task 5",
                 Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addTask(task);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/tasks/" + 10);
         HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(404, response.statusCode());
     }

     @Test
     void testAddEpic() throws IOException, InterruptedException {
         Epic epic = new Epic("Test 6", "Testing epic 1");
         String taskJson = gson.toJson(epic);

         HttpClient client = HttpClient.newBuilder()
                 .version(HttpClient.Version.HTTP_1_1)
                 .build();

         URI url = URI.create("http://localhost:8080/epics");
         HttpRequest request = HttpRequest.newBuilder().
                 uri(url).
                 POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(201, response.statusCode());

         List<Epic> epicsFromManager = manager.getAllEpics();

         assertNotNull(epicsFromManager, "Задачи не возвращаются");
         assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
         assertEquals("Test 6", epicsFromManager.get(0).getTaskName(), "Некорректное имя задачи");
     }

     @Test
     void testGetEpic() throws IOException, InterruptedException {
         Epic epic = new Epic("Test 6", "Testing epic 1"
                 , LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addEpic(epic);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/epics");
         HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(200, response.statusCode());

         assertNotNull(manager.getHistory(), "Задачи не возвращаются");
         assertEquals(manager.getAllEpics().toString(),response.body(), "Некорректный вывод задач");
     }

     @Test
     void testGetEpicByID() throws IOException, InterruptedException {
         Epic epic = new Epic("Test 7", "Testing epic 2"
                 , LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addEpic(epic);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
         HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(200, response.statusCode());

         assertNotNull(manager.getHistory(), "Задачи не возвращаются");
         assertEquals(epic.toString(),response.body(), "Некорректный вывод задач");
     }

     @Test
     void testGetEpicByNonExistentID() throws IOException, InterruptedException {
         Epic epic = new Epic("Test 8", "Testing epic 2"
                 , LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addEpic(epic);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/epics/" + 10);
         HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(404, response.statusCode());
     }

     @Test
     void testAddSubtask() throws IOException, InterruptedException {
         Epic epic = new Epic("Test 7", "Testing epic 2"
                 , LocalDateTime.parse("12:30 20.04.24",
                 DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")), Duration.ofMinutes(40));
         manager.addEpic(epic);
         Subtask subtask = new Subtask("Test 8", "Testing epic 2",
                 Status.NEW, epic.getId(), LocalDateTime.now(), Duration.ofMinutes(5));
         String taskJson = gson.toJson(subtask);

         HttpClient client = HttpClient.newBuilder()
                 .version(HttpClient.Version.HTTP_1_1)
                 .build();

         URI url = URI.create("http://localhost:8080/subtasks");
         HttpRequest request = HttpRequest.newBuilder().
                 uri(url).
                 POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(201, response.statusCode());

         List<Subtask> subtasksFromManager = manager.getAllSubtasks();

         assertNotNull(subtasksFromManager, "Задачи не возвращаются");
         assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
         assertEquals("Test 8", subtasksFromManager.get(0).getTaskName(), "Некорректное имя задачи");
     }

     @Test
     void testUpdateSubtask() throws IOException, InterruptedException {
         Epic epic = new Epic("Test 7", "Testing epic 2"
                 , LocalDateTime.parse("12:30 20.04.24",
                 DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")), Duration.ofMinutes(40));
         manager.addEpic(epic);
         Subtask subtask = new Subtask("Test 8", "Testing epic 2",
                 Status.NEW, epic.getId(), LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addSubtask(subtask);
         subtask.setStatus(Status.IN_PROGRESS);
         String taskJson = gson.toJson(subtask);

         HttpClient client = HttpClient.newBuilder()
                 .version(HttpClient.Version.HTTP_1_1)
                 .build();

         URI url = URI.create("http://localhost:8080/subtasks");
         HttpRequest request = HttpRequest.newBuilder().
                 uri(url).
                 POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(201, response.statusCode());

         List<Subtask> subtasksFromManager = manager.getAllSubtasks();

         assertNotNull(subtasksFromManager, "Задачи не возвращаются");
         assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
         assertEquals("IN_PROGRESS", subtasksFromManager.get(0).getStatus().toString(), "Некорректное имя задачи");
     }

     @Test
     void testAddInvalidTimeSubtask() throws IOException, InterruptedException {
         Epic epic = new Epic("Test 7", "Testing epic 2"
                 , LocalDateTime.parse("12:30 20.04.24",
                 DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")), Duration.ofMinutes(40));
         manager.addEpic(epic);
         Subtask subtask = new Subtask("Test 8", "Testing epic 2",
                 Status.NEW, epic.getId(), LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addSubtask(subtask);
         Subtask subtask1 = new Subtask("Test 8", "Testing epic 2",
                 Status.NEW, epic.getId(), LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addSubtask(subtask);
         String taskJson = gson.toJson(subtask1);

         HttpClient client = HttpClient.newBuilder()
                 .version(HttpClient.Version.HTTP_1_1)
                 .build();

         URI url = URI.create("http://localhost:8080/subtasks");
         HttpRequest request = HttpRequest.newBuilder().
                 uri(url).
                 POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(406, response.statusCode());
     }

     @Test
     void testGetSubtasksInEpic() throws IOException, InterruptedException {
         Epic epic = new Epic("Test 7", "Testing epic 2"
                 , LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addEpic(epic);
         Subtask subtask = new Subtask("Test 8", "Testing epic 2",
                 Status.NEW, epic.getId(), LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addSubtask(subtask);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/epics/" + epic.getId() +"/subtasks");
         HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(200, response.statusCode());

         List<Subtask> subtasksFromEpic = epic.getSubtasks();
         assertEquals(subtasksFromEpic.toString(), response.body(), "Некорректный вывод задач");
     }

     @Test
     void testGetSubtasksFromNonExistentEpic() throws IOException, InterruptedException {
         Epic epic = new Epic("Test 7", "Testing epic 2"
                 , LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addEpic(epic);
         Subtask subtask = new Subtask("Test 8", "Testing epic 2",
                 Status.NEW, epic.getId(), LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addSubtask(subtask);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/epics/" + 10 +"/subtasks");
         HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(404, response.statusCode());
     }

     @Test
     void testDeleteEpic() throws IOException, InterruptedException {
         Epic epic = new Epic("Test 8", "Testing epic 2"
                 , LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addEpic(epic);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
         HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(201, response.statusCode());

         List<Epic> epicsFromManager = manager.getAllEpics();

         assertTrue(epicsFromManager.isEmpty(), "Задачи не удаляются");
         assertTrue(manager.getPrioritizedTasks().isEmpty(), "Задачи не удаляются из приоритетного списка");
         assertTrue(manager.getHistory().isEmpty(),"Задачи не удаляются из истории");
         assertTrue(epic.getSubtasks().isEmpty(), "Подзадачи не удалены");
     }

     @Test
     void testDeleteNonExistedEpic() throws IOException, InterruptedException {
         Epic epic = new Epic("Test 8", "Testing epic 2"
                 , LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addEpic(epic);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/epics/" + 10);
         HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(404, response.statusCode());
     }

     @Test
     void testGetSubtask() throws IOException, InterruptedException {
         Epic epic = new Epic("Test 3", "Testing subtask 1"
                 , LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addEpic(epic);
         Subtask subtask = new Subtask("Test 3", "Testing subtask 1",
                 Status.NEW, epic.getId(), LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addSubtask(subtask);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/subtasks");
         HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(200, response.statusCode());

         assertNotNull(manager.getHistory(), "Задачи не возвращаются");
         assertEquals(manager.getAllSubtasks().toString(),response.body(), "Некорректный вывод задач");
     }


     @Test
     void testGetSubtaskById() throws IOException, InterruptedException {
         Epic epic = new Epic("Test 3", "Testing subtask 1"
                 , LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addEpic(epic);
         Subtask subtask = new Subtask("Test 3", "Testing subtask 1",
                 Status.NEW, epic.getId(), LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addSubtask(subtask);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
         HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(200, response.statusCode());

         assertNotNull(manager.getHistory(), "Задачи не возвращаются");
         assertEquals(subtask.toString() ,response.body(), "Некорректный вывод задач");
     }

     @Test
     void testGetSubtaskByNonExistentId() throws IOException, InterruptedException {
         Epic epic = new Epic("Test 3", "Testing subtask 1"
                 , LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addEpic(epic);
         Subtask subtask = new Subtask("Test 3", "Testing subtask 1",
                 Status.NEW, epic.getId(), LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addSubtask(subtask);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/subtasks/" + 10);
         HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(404, response.statusCode());
     }

     @Test
     void testDeleteSubtaskById() throws IOException, InterruptedException {
         Epic epic = new Epic("Test 3", "Testing subtask 1"
                 , LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addEpic(epic);
         Subtask subtask = new Subtask("Test 3", "Testing subtask 1",
                 Status.NEW, epic.getId(), LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addSubtask(subtask);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
         HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(201, response.statusCode());

         List<Subtask> subtasksFromManager = manager.getAllSubtasks();

         assertTrue(subtasksFromManager.isEmpty(), "Задачи не удаляются");
         assertTrue(manager.getPrioritizedTasks().isEmpty(), "Задачи не удаляются из приоритетного списка");
         assertTrue(manager.getHistory().isEmpty(),"Задачи не удаляются из истории");
     }

     @Test
     void testDeleteSubtaskByNonExistedId() throws IOException, InterruptedException {
         Epic epic = new Epic("Test 3", "Testing subtask 1"
                 , LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addEpic(epic);
         Subtask subtask = new Subtask("Test 3", "Testing subtask 1",
                 Status.NEW, epic.getId(), LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addSubtask(subtask);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/subtasks/" + 10);
         HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(404, response.statusCode());
     }

     @Test
     void testGetHistory() throws IOException, InterruptedException {
         Task task = new Task("Test 1", "Testing task 1",
                 Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addTask(task);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/history");
         HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(200, response.statusCode());

         assertNotNull(manager.getHistory(), "Задачи не возвращаются");
         assertEquals(manager.getHistory().toString(),response.body(), "Некорректный вывод задач");
     }

     @Test
     void testGetPrioritized() throws IOException, InterruptedException {
         Task task = new Task("Test 1", "Testing task 1",
                 Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
         manager.addTask(task);

         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/prioritized");
         HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(200, response.statusCode());

         assertNotNull(manager.getPrioritizedTasks(), "Задачи не возвращаются");
         assertEquals(manager.getPrioritizedTasks().toString(),response.body(), "Некорректный вывод задач");
     }
}
