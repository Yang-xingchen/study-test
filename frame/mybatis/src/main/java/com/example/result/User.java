package com.example.result;

import java.io.Serializable;

public class User implements Serializable {

    protected Long id;
    protected String name;
    protected Role role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role=" + role +
                '}';
    }

    public static class Man extends User {
        @Override
        public String toString() {
            return "Man{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", role=" + role +
                    '}';
        }
    }

    public static class Woman extends User {
        @Override
        public String toString() {
            return "Woman{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", role=" + role +
                    '}';
        }
    }
}
