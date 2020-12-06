package io.helidon.example.jms;

import java.util.concurrent.Flow;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

@Path("example")
@ApplicationScoped
public class SseResource {


    /**
     * Process send.
     *
     * @param msg message to process
     */
    @Path("/send/{msg}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getSend(@PathParam("msg") String msg) {
        msgBean.process(msg);
    }

    /**
     * Consume event.
     *
     * @param eventSink sink
     * @param sse       event
     */
    @GET
    @Path("sse")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void listenToEvents(@Context Flow.Subscriber<String> sink) {

    }
}
