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

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    // Serve chat page
    @GetMapping("/chat")
    public String chatPage() {
        return "chat";
    }

    // WebSocket send
    @MessageMapping("/send")
    public void sendMessage(Message message) {
        Message saved = messageService.saveMessage(message);

        // Send to **receiver**
        messagingTemplate.convertAndSend("/queue/messages/" + message.getReceiverId(), saved);

        // Send to **sender** so it appears instantly in their chat
        messagingTemplate.convertAndSend("/queue/messages/" + message.getSenderId(), saved);
    }


    // REST API for chat history
    @GetMapping("/api/chat/history")
    @ResponseBody
    public List<Message> getHistory(@RequestParam Long user1, @RequestParam Long user2) {
        return messageService.getChatHistory(user1, user2);
    }
}

