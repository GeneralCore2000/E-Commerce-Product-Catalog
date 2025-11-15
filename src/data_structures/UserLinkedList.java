package data_structures;

import models.users.User;

public class UserLinkedList {
    private Node head;

    public void clear() {
        head = null;
    }

    public void add(User user) {
        Node newNode = new Node(user);
        if (head == null) {
            head = newNode;
            return;
        }
        Node temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = newNode;
    }

    public Node getHead() {
        return head;
    }

    public static class Node {
        public User user;
        public Node next;

        public Node(User user) {
            this.user = user;
            next = null;
        }
    }
}
