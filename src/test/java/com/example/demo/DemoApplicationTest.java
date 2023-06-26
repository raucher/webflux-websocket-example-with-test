package com.example.demo;
// import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.ws.ReactiveWebSocketHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.socket.WebSocketHandler;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DemoApplicationTest {
    @Autowired
    WebSocketHandler wsHandler;

    @Test
    void contextLoads() {
        assertThat(wsHandler)
                .isNotNull()
                .isInstanceOf(ReactiveWebSocketHandler.class);
    }
}