package com.aisenz.loraflow;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 * Basic Client Socket
 */
@WebSocket(maxTextMessageSize = 64* 1024)
public class SimpleSocketHandler
{
    private final CountDownLatch closeLatch;
    private final CountDownLatch connectLatch;
    @SuppressWarnings("unused")
    private Session session;

    public SimpleSocketHandler()
    {
        this.closeLatch = new CountDownLatch(1);
        this.connectLatch = new CountDownLatch(1);
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException
    {
        return this.closeLatch.await(duration,unit);
    }

    public boolean awaitConnect(int duration, TimeUnit unit) throws InterruptedException
    {
        return this.connectLatch.await(duration,unit);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason)
    {
        System.out.printf("Connection closed: %d - %s%n",statusCode,reason);
        this.session = null;
        this.closeLatch.countDown(); // trigger latch
    }

    @OnWebSocketConnect
    public void onConnect(Session session)
    {
        System.out.printf("Got connect: %s%n",session);
        this.session = session;
        this.connectLatch.countDown(); // trigger connect
//        try
//        {
//            Future<Void> fut;
//            fut = session.getRemote().sendStringByFuture("Hello");
//            fut.get(2,TimeUnit.SECONDS); // wait for send to complete.
//
//            fut = session.getRemote().sendStringByFuture("Thanks for the conversation.");
//            fut.get(2,TimeUnit.SECONDS); // wait for send to complete.
//
//            session.close(StatusCode.NORMAL,"I'm done");
//        }
//        catch (Throwable t)
//        {
//            t.printStackTrace();
//        }
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }


    @OnWebSocketMessage
    public void onMessage(String msg)
    {
        System.out.printf("Got msg: %s%n",msg);
    }
}