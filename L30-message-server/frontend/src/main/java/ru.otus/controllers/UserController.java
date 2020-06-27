package ru.otus.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.domain.User;
import ru.otus.front.FrontendService;

@RestController
@Slf4j
public class UserController {
    private final SimpMessagingTemplate messagingTemplate;
    private final FrontendService frontendService;

    public UserController(SimpMessagingTemplate messagingTemplate, FrontendService frontendService) {
        this.messagingTemplate = messagingTemplate;
        this.frontendService = frontendService;
        log.info("User controller created...");
    }

    @MessageMapping("/add")
    public void getMessage(@RequestParam(value = "user") User user) {
        final var userFromJson = user;

        frontendService.createUser(id -> {
            if ( !id.isEmpty() )
                frontendService.getUsersData(users ->
                        messagingTemplate.convertAndSend("/topic/list", users));
        }, userFromJson);
    }

    @SubscribeMapping("/list")
    public void sendUsers() {
        frontendService.getUsersData(s ->
                messagingTemplate.convertAndSend("/topic/list", s));
    }
}
