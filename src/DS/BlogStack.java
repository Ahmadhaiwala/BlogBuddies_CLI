package DS;

import Model.Blog;

import java.util.ArrayList;

import static cli.General.GREEN;
import static cli.General.printColor;

public class BlogStack {
    ArrayList<Blog> ls;
    int top;
    int capacity;
    public BlogStack(int size){
         this.capacity=size;
         ls= new ArrayList<>();
         top=-1;
     }
    public void push(Blog item){
         ls.add(item);
         top++;

     }
     public Boolean isEmpty(){
        return  ls.isEmpty();
     }
     public ArrayList<Blog> retuernList(){
        return ls;
     }
     public Blog pop(){
         if(ls.isEmpty()){
             return null;
         }
         Blog b=ls.remove(top);
         top--;
         return b;

     }
    public Blog peek(){
         return ls.get(top);
     }
}
