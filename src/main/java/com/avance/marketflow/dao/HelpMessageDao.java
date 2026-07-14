package com.avance.marketflow.dao;

import com.avance.marketflow.model.HelpMessage;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class HelpMessageDao {
    private final AtomicLong ids = new AtomicLong(1);
    private final List<HelpMessage> messages = new ArrayList<>();

    public List<HelpMessage> findAll() {
        return messages;
    }

    public HelpMessage save(String name, String email, String message) {
        HelpMessage helpMessage = new HelpMessage(ids.getAndIncrement(), name, email, message, LocalDateTime.now());
        messages.add(0, helpMessage);
        return helpMessage;
    }

    public Optional<HelpMessage> findById(long id) {
        return messages.stream().filter(message -> message.getId() == id).findFirst();
    }
}
