package DS;

import java.util.ArrayList;

public class StackInteger {
     int top;
     ArrayList<Integer> ls;

    public StackInteger() {
        this.ls = new ArrayList<>();
        this.top = -1;
    }

    public void push(int x) {
        ls.add(x);
        top++;
    }

    public int pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack underflow! Nothing to pop.");
        }
        int val = ls.remove(top);
        top--;
        return val;
    }

    public int peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty! Nothing to peek.");
        }
        return ls.get(top);
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public int size() {
        return ls.size();
    }
}
