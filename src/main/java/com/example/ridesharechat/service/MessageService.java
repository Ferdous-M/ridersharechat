package com.example.ridesharechat.service;


import com.example.ridesharechat.entity.Message;
import com.example.ridesharechat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository repository;

    public Message save(Message message) {
        return repository.save(message);
    }

    public List<Message> history(Long user1, Long user2) {
        return repository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByTimestampAsc(
                user1, user2, user2, user1
        );
    }
}

