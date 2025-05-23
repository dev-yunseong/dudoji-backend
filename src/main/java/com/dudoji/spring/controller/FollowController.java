package com.dudoji.spring.controller;

import com.dudoji.spring.models.domain.PrincipalDetails;
import com.dudoji.spring.models.domain.User;
import com.dudoji.spring.service.FollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @GetMapping("/")
    public ResponseEntity<List<User>> getFollow(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        if (principalDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<User> result = followService.getFollowersById(principalDetails.getUid());
        log.info("getFollowersById({})", result);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteFollow(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long userId
    ) {
        if (principalDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (followService.deleteFollow(principalDetails.getUid(), userId)) {
            return ResponseEntity.status(HttpStatus.OK).body("Delete Success");
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Delete Failed");
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> createFollow(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long userId
    ) {
        if (principalDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (followService.createFollowById(principalDetails.getUid(), userId)) {
            return ResponseEntity.status(HttpStatus.OK).body("Create Success");
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Create Failed");
        }
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<User>> getRecommendedFriends(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam String email // TODO: Query? OR Path Variable?
    ) {
        if (principalDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<User> recommendedUsers = followService.getRecommendedFollow(email);
        return ResponseEntity.ok(recommendedUsers);
    }


    /**
     * ----
     * &#064;Deprecated
     */

    @Deprecated
    @GetMapping("/requests")
    public ResponseEntity<List<User>> getRequestFriends(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        if (principalDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<User> friendRequestList = followService.getFriendRequestList(principalDetails.getUid());
        return ResponseEntity.ok(friendRequestList);
    }

    @Deprecated
    @DeleteMapping("/requests/{senderId}")
    public ResponseEntity<Boolean> rejectFriend(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long senderId
    ) {
        if (principalDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if (followService.rejectFriend(senderId, principalDetails.getUid())) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    @Deprecated
    @PostMapping("/requests/{receiverId}")
    public ResponseEntity<Boolean> requestFriend(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long receiverId
    ){
        if (principalDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if (followService.requestFriend(principalDetails.getUid(), receiverId)) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }
}
