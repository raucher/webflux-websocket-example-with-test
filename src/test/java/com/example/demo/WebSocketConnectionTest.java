package com.example.demo;
// import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.ws.ReactiveWebSocketHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebSocketConnectionTest {
    @LocalServerPort
    long port;

    @Autowired
    WebSocketHandler wsHandler;

    @Test
    void contextLoads() {
        assertThat(wsHandler)
                .isNotNull()
                .isInstanceOf(ReactiveWebSocketHandler.class);
        assertThat(port).isGreaterThan(0);
    }

    @Test
    void testWsConnection() {
        AtomicReference<String> lastMessage = new AtomicReference<>();

        WebSocketClient client = new ReactorNettyWebSocketClient();
        client.execute(
                URI.create("ws://localhost:" + port + "/event-emitter"),
                session -> session.send(
                                Mono.just(session.textMessage("event-spring-reactive-client-websocket")))
                        .thenMany(session.receive()
                                .take(1)
                                .map(WebSocketMessage::getPayloadAsText)
                                .doOnNext(lastMessage::set)
                                .log()
                        ).then()
        ).block(Duration.ofSeconds(2L));

        assertThat(lastMessage.get())
                .isNotNull()
                .isEqualTo("0");
    }

}