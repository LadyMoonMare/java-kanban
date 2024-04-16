package tracker.controllers;

import tracker.model.Task;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private static final byte HISTORY_SIZE = 10;
    private static final byte HISTORY_OVERLOAD = 0;
    private List<Task> history = new LinkedList<>();

    @Override
    public void add (Task task){
        if (history.size() == HISTORY_SIZE) {
            history.remove(HISTORY_OVERLOAD);
        }
        history.add(task);
    }
    @Override
    public List<Task> getHistory() {
        return new LinkedList<>(history);
    }

    @Override
    public void remove(int id){
        history.remove(id);
    }
}

