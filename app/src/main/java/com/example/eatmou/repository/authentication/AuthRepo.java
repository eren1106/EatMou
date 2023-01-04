package com.example.eatmou.repository.authentication;

public interface AuthRepo {
    boolean uploadAppLock(String pass, boolean exist);
}
