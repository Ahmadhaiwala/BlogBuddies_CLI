package DS;

import Model.Blog;
import java.util.ArrayList;

public class BlogLinkedList {

    class Node {
        Blog data;
        Node next;
        Node prev; // for doubly linked list

        Node(Blog data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    Node head;
    Node tail;
    int size;

    public BlogLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public void add(Blog blog) {
        Node newNode = new Node(blog);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public Blog remove(int index) {
        if (index < 0 || index >= size) return null;

        Node removedNode;
        if (index == 0) {
            removedNode = head;
            head = head.next;
            if (head != null) head.prev = null;
            else tail = null; // list became empty
        } else if (index == size - 1) {
            removedNode = tail;
            tail = tail.prev;
            tail.next = null;
        } else {
            Node curr = head;
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
            removedNode = curr;
            curr.prev.next = curr.next;
            curr.next.prev = curr.prev;
        }

        size--;
        return removedNode.data;
    }

    public void addAll(ArrayList<Blog> blogs) {
        for (Blog b : blogs) {
            add(b);
        }
    }

    public ArrayList<Blog> getList() {
        ArrayList<Blog> result = new ArrayList<>();
        Node curr = head;
        while (curr != null) {
            result.add(curr.data);
            curr = curr.next;
        }
        return result;
    }

    public void clear() {
        head = tail = null;
        size = 0;
    }

    public Blog get(int index) {
        if (index < 0 || index >= size) return null;
        Node curr = head;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr.data;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
