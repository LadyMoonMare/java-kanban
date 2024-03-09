package tracker.controllers;

import tracker.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private static final byte HISTORY_SIZE = 10;
    private static final byte HISTORY_OVERLOAD = 0;
    private List<Task> history = new ArrayList<>();

    @Override
    public void add (Task task){
        history.add(task);
    }
    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
    @Override
    public <T extends Task> void putInHistory(Map<Integer,T> taskMap, Integer taskId) {
        if (taskMap.containsKey(taskId)) {
            if (history.size() == HISTORY_SIZE) {
                history.remove(HISTORY_OVERLOAD);
                add(taskMap.get(taskId));
            } else {
                add(taskMap.get(taskId));
            }
        }
    }
}

