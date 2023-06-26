package com.example.demo.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class ReactiveWebSocketHandler implements WebSocketHandler {


    public Flux<Long> getIntervalFlux(Long intervalInMillis) {
        return Flux.interval(Duration.ofMillis(intervalInMillis));
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(getIntervalFlux(500L)
                        .map(l -> Long.toString(l))
                        .map(session::textMessage))
                .and(session.receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .log()
                );
    }
}
