package com.service.microservice.utils;

import com.service.microservice.dto.ClientDTO;
import com.service.microservice.model.User;

public class Extraction {


    public static User getUser(ClientDTO client) {
        User user=new User();
        user.setUserId(client.getUserId());
        user.setName(client.getName());
        user.setEmail(client.getEmail());
        user.setPhoneNumber(client.getPassword());
        user.setAddress(client.getPassword());
        user.setPassword(client.getPassword());
        user.setRole(client.getRole());
        user.setSex(client.getSex());
        user.setBirthday(client.getBirthday());
        user.setCreatedAt(client.getCreatedAt());
        user.setUpdatedAt(client.getUpdatedAt());
        user.setEmailNotifications(client.getEmailNotifications());
        user.setPushNotifications(client.getPushNotifications());
        user.setSmsNotifications(client.getSmsNotifications());
        return user;
    }



}
