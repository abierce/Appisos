package com.Appisos.apps.DB;

import java.util.List;

/**
 * Created by apps on 28/09/2015.
 */
public class Users {
    private List<UsersMain> UsersMain;

    public List<UsersMain> getUsersList() {
        return UsersMain;
    }

    public void setUsersList(List<UsersMain> UsersMain) {
        this.UsersMain = UsersMain;
    }


    public class UsersMain {
        private String idUser;
        private List<User> user;

        public String getIdUser() {
            return idUser;
        }

        public void setIdUser(String idUser) {
            this.idUser = idUser;
        }

        public List<User> getUserList() {
            return user;
        }

        public void setUserList(List<User> user) {
            this.user = user;
        }
    }

    public class User {
        private String nameUser;
        private String passUser;
        private String name;
        private String surname;
        private String mail;
        private String info;
        private String phone;
        private String sex;
        private String image;
        private String profile;

        public String getNameUser() {
            return nameUser;
        }

        public void setNameUser(String nameUser) {
            this.nameUser = nameUser;
        }

        public String getPassUser() {
            return passUser;
        }

        public void setPassUser(String passUser) {
            this.passUser = passUser;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getPhone(){
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

    }

}
