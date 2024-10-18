package com.teleconsys.user_service.service;

import com.teleconsys.user_service.entity.UserActivity;
import com.teleconsys.user_service.dao.UserActivityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserActivityService {

    @Autowired
    private UserActivityDao userActivityDao;

    public void trackActivity(String userId, String action, String ipAddress, String details) {
        UserActivity activity = new UserActivity();
        activity.setUserId(userId);
        activity.setAction(action);
        activity.setTimestamp(LocalDateTime.now());
        activity.setIpAddress(ipAddress);
        activity.setDetails(details);

        userActivityDao.save(activity);
    }

    public List<UserActivity> getActivitiesByUserId(String userId) {
        return null;
    }
}
