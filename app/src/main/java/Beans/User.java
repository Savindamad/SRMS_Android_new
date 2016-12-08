package Beans;

import java.io.Serializable;

/**
 * Created by savinda on 11/4/16.
 */

public class User implements Serializable {
    String firstName,lastName,userID,type;

    public User(String firstName, String lastName, String userID,String type){
        this.firstName=firstName;
        this.lastName=lastName;
        this.userID=userID;
        this.type=type;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public String getFullName(){
        return firstName+" "+lastName;
    }

    public String getUserID() {
        return userID;
    }

    public String getType(){
        return type;
    }

}
