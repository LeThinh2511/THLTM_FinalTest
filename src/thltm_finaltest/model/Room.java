/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thltm_finaltest.model;

import java.io.Serializable;

/**
 *
 * @author thinhle
 */
public class Room implements Serializable {
    String name;
    Teacher firstTeacher;
    Teacher secondTeacher;

    public Room() {
    }

    public Room(String name) {
        this.name = name;
    }

    public Room(String name, Teacher firstTeacher, Teacher secondTeacher) {
        this.name = name;
        this.firstTeacher = firstTeacher;
        this.secondTeacher = secondTeacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getFirstTeacher() {
        return firstTeacher;
    }

    public void setFirstTeacher(Teacher firstTeacher) {
        this.firstTeacher = firstTeacher;
    }

    public Teacher getSecondTeacher() {
        return secondTeacher;
    }

    public void setSecondTeacher(Teacher secondTeacher) {
        this.secondTeacher = secondTeacher;
    }
}
