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
            oldTail.next = newNode;
        }
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
        Node curr;
        for (Node node : sortedHistory.values()) {
            if ((node.prev == null)) {
                head = node;
            }
        }
        curr = head;
        while (curr != null) {
            history.add(curr.data);
            curr = curr.next;
        }
        return history;
    }

    private void removeNode(Node node) {
        if (node != null) {
            if (node.prev != null) {
                node.prev.next = node.next;
            }  else {
                head = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            } else {
                tail = node.prev;
            }
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



