public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        //test 1: addTask
        Task task = new Task("Task", "Description");
        Task task1 = new Task("Task1", "Description1");
        manager.addTask(task);
        manager.addTask(task1);
        System.out.println(manager.getAllTasks().toString());

        //test 2: updateTask
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);
        System.out.println(manager.getAllTasks().toString());

        //test 3: removeTask
        manager.removeTask(task.getId());
        System.out.println(manager.getAllTasks().toString());

        //test 4: addEpic
        Epic epic = new Epic("Epic", "Description");

        manager.addEpic(epic);
        System.out.println(manager.getAllEpics().toString());

        //test 5: updateEpic
        epic.setTaskName("epic");
        manager.updateEpic(epic);
        System.out.println(manager.getAllEpics().toString());

        //test 6: addSubtask
        Subtask subtask0 = new Subtask("Subtask0", "Description",epic.getId());
        Subtask subtask1 = new Subtask("Subtask1", "Description",epic.getId());

        manager.addSubtask(subtask0);
        manager.addSubtask(subtask1);
        System.out.println(manager.getAllSubtasks().toString());
        System.out.println(manager.getAllEpics().toString());

        //test 7: updateSubtask
        subtask0.setStatus(Status.DONE);
        manager.updateSubtask(subtask0);
        System.out.println(manager.getAllEpics().toString());

        //test 8: if all subtasks are done
        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);
        System.out.println(manager.getAllEpics().toString());
    }


}
