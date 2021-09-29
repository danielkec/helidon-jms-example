package io.helidon.example.jms;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

import io.helidon.common.reactive.BufferedEmittingPublisher;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.FlowAdapters;

import com.rabbitmq.jms.admin.RMQConnectionFactory;

@Path("example")
@ApplicationScoped
public class MessagingResource {

    private final BufferedEmittingPublisher<String> emitter = BufferedEmittingPublisher.create();
    private SseBroadcaster sseBroadcaster;
    private Sse sse;

    @Context
    public void setSse(Sse sse) {
        this.sse = sse;
        this.sseBroadcaster = sse.newBroadcaster();
    }

    @javax.enterprise.inject.Produces
    @ApplicationScoped
    @Named("rabbit-mq-factory")
    public RMQConnectionFactory connectionFactory() {
        RMQConnectionFactory connectionFactory = new RMQConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        return connectionFactory;
    }

    @Incoming("from-jms")
    public void fromRabbit(String payload) {
        sseBroadcaster.broadcast(sse.newEvent(payload));
    }

    @Outgoing("to-jms")
    public PublisherBuilder<Message<String>> toRabbit() {
        // Redirect emitter stream to rabbit
        return ReactiveStreams.fromPublisher(FlowAdapters.toPublisher(emitter))
                .map(Message::of);
    }

    @Path("/send")
    @POST
    public void getSend(String msg) {
        // Emit new message
        emitter.emit(msg);
    }

    @GET
    @Path("sse")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void listenToEvents(@Context SseEventSink sink) {
        sseBroadcaster.register(sink);
    }
}
