package textFileTest;

import java.util.*;

public class Student {
    private String ID;  //用String类，方便将ID转换成为字符串
    private String name;
    private String sex;
    private  Double height;  //用Double类，方便将height转换成字符串
    private Date birthday;
    private int year, month, day;
    private String birth_place;

    public Student() {
    }

    public Student(String ID, String name, String sex, Double Height, int years, int months, int days, String birth_place) {
        this.ID = ID;
        this.name = name;
        this.sex = sex;
        this.height = Height;
        this.year = years;
        this.month = months;
        this.day = days;
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        birthday = calendar.getTime();
        this.birth_place = birth_place;
    }
    public int getYear(){return year;}
    public int getMonth() {return month;}
    public int getDay() {return day;}

    public void setYear(int year) {
        this.year = year;
        setBirthday(getYear(), getMonth(), getDay());
    }

    public void setDay(int day) {
        this.day = day;
        setBirthday(getYear(), getMonth(), getDay());
    }

    public void setMonth(int month) {
        this.month = month;
        setBirthday(getYear(), getMonth(), getDay());
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public Double getHeight() {
        return height;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getBirth_place() {
        return birth_place;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public void setBirthday(int years, int months, int days) {
        year = years;
        month = months;
        day = days;
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        this.birthday = calendar.getTime();
    }

    public void setBirth_place(String birth_place) {
        this.birth_place = birth_place;
    }

    //将一个学生的信息打印出来到文件里
    public String toString()
    {
        String str = this.ID+","+this.name+","+this.sex+","+this.height+","+this.birthday+","+this.birth_place;
        return str;
    }
}
