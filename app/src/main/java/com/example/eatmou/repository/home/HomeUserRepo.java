package com.example.eatmou.repository.home;

import com.example.eatmou.model.Users;

import java.util.List;

public interface HomeUserRepo {
    List<Users> getAllUser();
    Users getUser(String userID);
}
