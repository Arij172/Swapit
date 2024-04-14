package com.example.swapit;

public class user {

        public String fullName;
        public String phone;
        public String password;

        public user() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public user(String fullName, String phone, String password) {
            this.fullName = fullName;
            this.phone = phone;
            this.password = password;
        }
    }


