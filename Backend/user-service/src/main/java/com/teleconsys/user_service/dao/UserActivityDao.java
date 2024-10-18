package com.teleconsys.user_service.dao;

import com.teleconsys.user_service.entity.UserActivity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserActivityDao extends MongoRepository<UserActivity, String> {
    List<UserActivity> findByUserId(String userId);
}
