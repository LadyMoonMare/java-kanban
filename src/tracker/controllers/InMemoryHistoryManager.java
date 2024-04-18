package tracker.controllers;

import tracker.model.Task;
import tracker.util.Node;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Node> linkedHistory = new LinkedList<>();
    private Map<Integer,Node> sortedHistory = new HashMap<>();
    Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        if (sortedHistory.containsKey(task.getId())) {
            removeNode(sortedHistory.get(task.getId()));
            sortedHistory.remove(task.getId());
        }
        Node taskNode = linkLast(task);
        sortedHistory.put(task.getId(),taskNode);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        if (sortedHistory.containsKey(id)) {
            removeNode(sortedHistory.get(id));
            sortedHistory.remove(id);
        }
    }

    private Node linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }
        linkedHistory.add(newNode);
        return newNode;
    }

    public Task getLast() {
        final Node curTail = tail;
        if (curTail == null)
            throw new NoSuchElementException();
        return tail.getData();
    }

    private List<Task> getTasks() {
        List<Task> history = new ArrayList<>();
        for (Node node : linkedHistory) {
            history.add(node.getData());
        }
        return history;
    }

    private void removeNode(Node node) {
        linkedHistory.remove(node);
    }
}

