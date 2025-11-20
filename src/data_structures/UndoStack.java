package data_structures;

public class UndoStack {
    private Node top;

    public static class UndoAction {
        private final QueueOrders.Queue fulfilledOrder;
        private final int previousStock;

        public UndoAction(QueueOrders.Queue fulfilledOrder, int previousStock) {
            this.fulfilledOrder = fulfilledOrder;
            this.previousStock = previousStock;
        }

        public QueueOrders.Queue getFulfilledOrder() {
            return fulfilledOrder;
        }

        public int getPreviousStock() {
            return previousStock;
        }
    }

    public void push(UndoAction action) {
        Node newNode = new Node(action);
        newNode.next = top;
        top = newNode;
    }

    public UndoAction pop() {
        if (isEmpty()) {
            return null;
        }
        UndoAction action = top.action;
        top = top.next;
        return action;
    }

    public UndoAction peek() {
        return isEmpty() ? null : top.action;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        int count = 0;
        Node current = top;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

    private static class Node {
        UndoAction action;
        Node next;

        Node(UndoAction action) {
            this.action = action;
            this.next = null;
        }
    }
}
