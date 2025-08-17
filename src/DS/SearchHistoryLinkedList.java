package DS;

import java.util.ArrayList;

import static cli.General.RED;
import static cli.General.printColor;

public class SearchHistoryLinkedList {
    class Node {
        String data;
        Node next;
        Node prev;

        Node(String data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

     Node head;
     Node tail;
     int size;

    public SearchHistoryLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }


    public void add(String data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    // Add at beginning
    public void addFirst(String data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    // Remove by index
    public String remove(int index) {
        if (index < 0 || index >= size) return null;

        Node curr;
        if (index < size / 2) { // start from head
            curr = head;
            for (int i = 0; i < index; i++) curr = curr.next;
        } else { // start from tail
            curr = tail;
            for (int i = size - 1; i > index; i--) curr = curr.prev;
        }

        String removed = curr.data;
        unlink(curr);
        return removed;
    }

    // Remove by value (alias for removeByValue)
    public boolean remove(String s) {
        return removeByValue(s);
    }

    public boolean removeByValue(String data) {
        Node curr = head;
        while (curr != null) {
            if (curr.data.equals(data)) {
                unlink(curr);
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    // Internal unlink helper (handles head/tail logic safely)
    private void unlink(Node curr) {
        if (curr.prev != null) curr.prev.next = curr.next;
        else head = curr.next; // removed head

        if (curr.next != null) curr.next.prev = curr.prev;
        else tail = curr.prev; // removed tail

        // cleanup
        curr.next = curr.prev = null;

        size--;
    }

    // Get as ArrayList
    public ArrayList<String> getList() {
        ArrayList<String> list = new ArrayList<>();
        Node temp = head;
        while (temp != null) {
            list.add(temp.data);
            temp = temp.next;
        }
        return list;
    }

    public void clear() {
        head = tail = null;
        size = 0;
    }

    public String get(int index) {
        if (index < 0 || index >= size) return null;

        Node curr;
        if (index < size / 2) {
            curr = head;
            for (int i = 0; i < index; i++) curr = curr.next;
        } else {
            curr = tail;
            for (int i = size - 1; i > index; i--) curr = curr.prev;
        }
        return curr.data;
    }

    public boolean contains(String data) {
        Node temp = head;
        while (temp != null) {
            if (temp.data.equals(data)) return true;
            temp = temp.next;
        }
        return false;
    }

    public void removeLast() {
        if (isEmpty()) {
            printColor("Runtime error: List is empty", RED);
            return;
        }
        unlink(tail);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
