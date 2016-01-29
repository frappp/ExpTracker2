package expense;


public class User {
    private String firstName;
    private String lastName;
    private String userName;
    private int userID;
    public User(String first, String last, String name, int id) {
        firstName = first;
        lastName = last;
        userName = name;
        userID = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserID(int id){
        userID = id;
    }

    public int getUserID(){
        return userID;
    }

}
