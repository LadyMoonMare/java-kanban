package tracker.controllers;

import tracker.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    public Map<Integer,Node> sortedHistory = new HashMap<>();
    public Node head;
    public Node tail;

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
            head = oldTail.prev;
            oldTail.setNext(newNode);
        }
        sortedHistory.put(task.getId(),newNode);
        return newNode;
    }

    public Task getLast() {
        final Node curTail = tail;
        if (curTail == null)
            throw new NoSuchElementException();
        return tail.data;
    }

    private List<Task> getTasks() {
        List<Task> history = new ArrayList<>();
        int index = 0;
        for (Node node : sortedHistory.values()) {
            if (node.prev == null) {
                history.add(0,node.data);
                index++;
            } else {
                history.add(index,node.data);
                index++;
            }
        }
        return history;
    }

    private void removeNode(Node node) {
        if (tail == null) {
            head = node.prev;
        } else if (head == null) {
            tail = node.next;
        } else {
            node.setPrev(node.prev);
            node.setNext(node.next);
        }
        sortedHistory.remove(node.data.getId());
    }

    class Node {
        private final Task data;
        private Node next;
        private Node prev;

        public void setNext(Node next) {
            this.next = next;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}



