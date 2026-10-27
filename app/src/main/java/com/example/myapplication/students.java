package com.example.myapplication;

class Student {

    String firstName;
    String lastName;
    String email;
    String studentClass;
    String major;
    boolean notifications;
    boolean terms;

    public Student(String firstName, String lastName,
                   String email, String studentClass,
                   String major, boolean notifications,
                   boolean terms) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.studentClass = studentClass;
        this.major = major;
        this.notifications = notifications;
        this.terms = terms;
    }
}
