package tracker.controllers;

import tracker.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer,Node> sortedHistory = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        remove(task.getId());
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
        sortedHistory.put(task.getId(),newNode);
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
        int index = 0;
        for (Node node : sortedHistory.values()) {
            if (node.getPrev() == null) {
                history.add(0,node.getData());
                index++;
            } else {
                history.add(index,node.getData());
                index++;
            }
        }
        return history;
    }

    private void removeNode(Node node) {
        if (tail == null) {
            head = node.getPrev();
        } else if (head == null) {
            tail = node.getNext();
        } else {
            head = node.getPrev();
            tail = node.getNext();
        }
        sortedHistory.remove(node.getData().getId());
    }
}
class Node {
    private Task data;
    private Node next;
    private Node prev;

    public Node(Node prev, Task data, Node next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public Task getData() {
        return data;
    }

    public Node getNext() {
        return next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}


