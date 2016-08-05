package org.hackillinois.app2017.HackerHelp;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class Ticket {

    private String userName;
    private String issue;
    private String phoneNumber;

    public Ticket(String userName, String issue, String phoneNumber){
        this.userName = userName;
        this.issue = issue;
        this.phoneNumber = phoneNumber;
    }

    public String getUserName(){
        return userName;
    }

    public String getIssue(){
        return issue;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }
}
