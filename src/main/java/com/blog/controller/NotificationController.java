package com.blog.controller;

import com.blog.common.ResponseResult;
import com.blog.entity.Notification;
import com.blog.entity.User;
import com.blog.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /** 获取当前用户的通知列表和未读数 */
    @GetMapping("/list")
    public ResponseResult<Map<String, Object>> list(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Notification> list = notificationService.listByUser(user.getId());
        int unread = notificationService.unreadCount(user.getId());
        Map<String, Object> result = new HashMap<>();
        result.put("unread", unread);
        result.put("list", list);
        return ResponseResult.success(result);
    }

    /** 获取未读数量（用于导航栏角标） */
    @GetMapping("/unread")
    public ResponseResult<Map<String, Object>> unread(HttpSession session) {
        User user = (User) session.getAttribute("user");
        int count = notificationService.unreadCount(user.getId());
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        return ResponseResult.success(result);
    }

    /** 标记单条已读 */
    @PostMapping("/read")
    public ResponseResult<?> markRead(@RequestParam Long id) {
        notificationService.markRead(id);
        return ResponseResult.success(null);
    }

    /** 全部标记已读 */
    @PostMapping("/read-all")
    public ResponseResult<?> markAllRead(HttpSession session) {
        User user = (User) session.getAttribute("user");
        notificationService.markAllRead(user.getId());
        return ResponseResult.success(null);
    }
}
