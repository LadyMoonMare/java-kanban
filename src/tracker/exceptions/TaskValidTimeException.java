package tracker.exceptions;

import tracker.model.Task;

import java.time.format.DateTimeFormatter;

public class TaskValidTimeException extends RuntimeException {
    final String notValidTime;

    public TaskValidTimeException (Task task) {
        super();
        notValidTime = task.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yy"));
    }

    void getMessage(String notValidTime) {
        System.out.println(notValidTime + "некорректно введено время начала.");
    }
}
