package com.lofidewanto.demo.client.ui.person;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class VPerson {

    private int id;

    @Size(min = 5, max = 50)
    private String name;

    @Min(0)
    @Max(100)
    private int age;

    public VPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public String getNickName() { return name.substring(0, min(5, name.length() -1));}

    public boolean isRetired() {return age > 65;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
