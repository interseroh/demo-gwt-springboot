package com.lofidewanto.demo.client.ui.person;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

class VPerson {

    @Size(min = 5, max = 50)
    private String name;

    @Min(0)
    @Max(100)
    private int age;

    public VPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

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
