package DS;

import Model.Blog;

import java.util.ArrayList;

public class BlogLinkedList {
     class Node {
        Blog data;
        Node next;

        Node(Blog data) {
            this.data = data;
            this.next = null;
        }
    }

     Node head;
     int size;
     Node tail;

    public BlogLinkedList() {
        head = null;
        size = 0;
    }


    public void add(Blog blog) {
        Node newNode = new Node(blog);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public Blog remove(int index) {
        if (index < 0 || index >= size) return null;

        Blog removed;
        if (index == 0) {
            removed = head.data;
            head = head.next;
            if (head == null) tail = null; // list became empty
        } else {
            Node prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
            }
            removed = prev.next.data;
            prev.next = prev.next.next;

            if (prev.next == null) tail = prev; // removed last node
        }
        size--;
        return removed;
    }

    public void addAll(ArrayList<Blog> la){
        for(Blog b: la){
            add(b);
        }
   }
   public ArrayList<Blog> getList(){
        Node temp=head;
        ArrayList<Blog> bl= new ArrayList<>();
        while(temp!=null){
            bl.add(temp.data);
            temp=temp.next;
        }
        return bl;
   }
    public void clear() {
        head = null;
        tail = null;
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
