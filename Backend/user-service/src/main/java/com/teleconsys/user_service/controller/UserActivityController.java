package com.teleconsys.user_service.controller;

import com.teleconsys.user_service.entity.UserActivity;
import com.teleconsys.user_service.service.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tracking")
public class UserActivityController {

    @Autowired
    private UserActivityService userActivityService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserActivity>> getUserActivities(@PathVariable String userId) {
        List<UserActivity> activities = userActivityService.getActivitiesByUserId(userId);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }
}
