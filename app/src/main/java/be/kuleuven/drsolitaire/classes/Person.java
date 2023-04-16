package be.kuleuven.drsolitaire.classes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class representation of Person.
 */

// @NG
public class Person {

    private int id;
    private String username;
    private String password;
    private int age;
    private String gender;
    private int level;
    private int tabletLevel;

    public Person() {
    }

    public Person(JSONObject obj) {
        try {
            this.id = obj.getInt("id");
            this.username = obj.getString("username");
            this.password = obj.getString("password");
            this.age = obj.getInt("age");
            this.gender = obj.getString("gender");
            this.level = obj.getInt("playLevel");
            this.tabletLevel = obj.getInt("tabletLevel");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for Person objects already in the database
     */
    public Person(String username, String password, int age, String gender, int level, int tabletLevel) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.level = level;
        this.tabletLevel = tabletLevel;
    }

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTabletLevel() {
        return tabletLevel;
    }

    public void setTabletLevel(int tabletLevel) {
        this.tabletLevel = tabletLevel;
    }

}

