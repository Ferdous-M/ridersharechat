package com.example.ridesharechat.controller;

import com.example.ridesharechat.entity.Message;
import com.example.ridesharechat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import com.example.ridesharechat.entity.Message;
import com.example.ridesharechat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template;
    private final MessageService service;

    @GetMapping("/chat")
    public String chatPage() {
        return "chat"; // thymeleaf template
    }

    @MessageMapping("/send")
    public void send(Message message) {
        Message saved = service.save(message);

        System.out.println("Backend received: " + message);

        // Send to receiver
        template.convertAndSendToUser(
                message.getReceiverId().toString(),
                "/queue/messages",
                saved
        );

        // Send to sender
        template.convertAndSendToUser(
                message.getSenderId().toString(),
                "/queue/messages",
                saved
        );
    }

    @GetMapping("/api/chat/history")
    @ResponseBody
    public List<Message> history(@RequestParam Long user1, @RequestParam Long user2) {
        return service.history(user1, user2);
    }
}



