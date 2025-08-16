package DS;

import java.util.ArrayList;

public class SearchStack {
     ArrayList<String> ls; // store stack elements
     int top;

    public SearchStack() {
        ls = new ArrayList<>();
        top = -1; // stack empty
    }


   public ArrayList<String > getList(){
        ArrayList<String> ab= new ArrayList<>();
        for(String i:ls){
            ab.add(i);
        }
        return  ab;
   }
    public void push(String item) {
        ls.add(item);
        top++;
    }

    // Pop element from stack
    public String pop() {
        if (isEmpty()) {
            System.out.println("Stack is empty!");
            return null;
        }
        String item = ls.remove(top);
        top--;
        return item;
    }


    public String peek() {
        if (isEmpty()) {
            System.out.println("Stack is empty!");
            return null;
        }
        return ls.get(top);
    }


    public boolean isEmpty() {
        return top == -1;
    }


    public int size() {
        return top + 1;
    }
}
