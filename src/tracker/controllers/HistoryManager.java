package tracker.controllers;
import tracker.model.Task;

import java.util.List;
import java.util.Map;

public interface HistoryManager {
    void add (Task task);

    List<Task> getHistory();
    <T extends Task> void putInHistory(Map<Integer,T> taskMap, Integer taskId);
}
