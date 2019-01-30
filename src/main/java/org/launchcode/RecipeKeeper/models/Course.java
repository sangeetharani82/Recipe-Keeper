package org.launchcode.RecipeKeeper.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Course {
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=3, max=15)
    private String courseName;

    @OneToMany
    @JoinColumn(name = "course_id")
    private List<Recipe> recipes = new ArrayList<>();

    public Course(){}

    public Course(String courseName) {
        this.courseName = courseName;
    }

    public int getId() {
        return id;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
