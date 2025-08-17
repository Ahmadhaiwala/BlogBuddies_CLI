package Model;

import java.time.LocalDateTime;
import java.util.Date;
import cli.General;

import static cli.General.*;

public class User {
    public int Userid;
    public String User_Name;
    public String Password;
    public String fullName;
    public String email;
    public String bio;
    public Date joinedOn;

    public User(String User_Name, String Password, String FullName, String email, String bio, Date joinedon){
        this.User_Name=User_Name;
        this.Password=Password;
        this.fullName=FullName;
        this.email=email;
        this.bio=bio;
        this.joinedOn =joinedon;

    }
    public User(int userid,String User_Name, String Password, String FullName, String email, String bio, Date joinedon){
        this.Userid=userid;
        this.User_Name=User_Name;
        this.Password=Password;
        this.fullName=FullName;
        this.email=email;
        this.bio=bio;
        this.joinedOn =joinedon;

    }



    public void getuser(){
     printColor("UserName:",BLUE);
     printColor(User_Name,CYAN);
        printColor("Fullname:",BLUE);
        printColor(fullName,CYAN);
        printColor("email:",BLUE);
        printColor(email,CYAN);
        printColor("bio:",BLUE);
        printColor(User_Name,CYAN);
        printColor("joinedOn:",BLUE);
        printColor(User_Name,CYAN);

    }
    public void getUsername(){
        printColor(User_Name,CYAN);
    }


}
