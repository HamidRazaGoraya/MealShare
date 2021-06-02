package com.raza.mealshare.Registration.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class LoginModel {
    public LoginModel() {
    }

    @Expose
    @SerializedName("data")
    private Data data;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("code")
    private int code;
    @Expose
    @SerializedName("success")
    private boolean success;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class Data {
        @Expose
        @SerializedName("user")
        private User user;
        @Expose
        @SerializedName("accessToken")
        private String accessToken;
        @Expose
        @SerializedName("id")
        private int id;
        @Expose
        @SerializedName("accessTokenKey")
        private String accessTokenKey;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAccessTokenKey() {
            return accessTokenKey;
        }

        public void setAccessTokenKey(String accessTokenKey) {
            this.accessTokenKey = accessTokenKey;
        }
    }

    public static class User {
        @Expose
        @SerializedName("isJoin")
        private String isJoin;
        @Expose
        @SerializedName("updatedDate")
        private String updatedDate;
        @Expose
        @SerializedName("createdDate")
        private String createdDate;
        @Expose
        @SerializedName("isDelete")
        private int isDelete;
        @Expose
        @SerializedName("preferFoot")
        private String preferFoot;
        @Expose
        @SerializedName("bestPosition2")
        private String bestPosition2;
        @Expose
        @SerializedName("bestPosition")
        private String bestPosition;
        @Expose
        @SerializedName("editedBy")
        private int editedBy;
        @Expose
        @SerializedName("lastLoginDate")
        private String lastLoginDate;
        @Expose
        @SerializedName("avatar")
        private String avatar;
        @Expose
        @SerializedName("email")
        private String email;
        @Expose
        @SerializedName("phone")
        private String phone;
        @Expose
        @SerializedName("birthday")
        private String birthday;
        @Expose
        @SerializedName("gender")
        private String gender;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("role")
        private String role;
        @Expose
        @SerializedName("userCode")
        private String userCode;
        @Expose
        @SerializedName("id")
        private int id;
        @Expose
        @SerializedName("allowedNotifify")
        private String allowedNotifify;

        public String getAllowedNotifify() {
            return allowedNotifify;
        }

        public void setAllowedNotifify(String allowedNotifify) {
            this.allowedNotifify = allowedNotifify;
        }

        public String getIsJoin() {
            return isJoin;
        }

        public void setIsJoin(String isJoin) {
            this.isJoin = isJoin;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
        }

        public String getPreferFoot() {
            return preferFoot;
        }

        public void setPreferFoot(String preferFoot) {
            this.preferFoot = preferFoot;
        }

        public String getBestPosition2() {
            return bestPosition2;
        }

        public void setBestPosition2(String bestPosition2) {
            this.bestPosition2 = bestPosition2;
        }

        public String getBestPosition() {
            return bestPosition;
        }

        public void setBestPosition(String bestPosition) {
            this.bestPosition = bestPosition;
        }

        public int getEditedBy() {
            return editedBy;
        }

        public void setEditedBy(int editedBy) {
            this.editedBy = editedBy;
        }

        public String getLastLoginDate() {
            return lastLoginDate;
        }

        public void setLastLoginDate(String lastLoginDate) {
            this.lastLoginDate = lastLoginDate;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
