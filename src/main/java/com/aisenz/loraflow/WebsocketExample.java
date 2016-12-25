package com.aisenz.loraflow;

import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Example of a simple client
 */
public class WebsocketExample
{
    public static void main(String[] args)
    {
        String destUri = "wss://loraflow.io/v1/application/ws?appeui=1234657812345633&token=1v7pw4e565772e63066a09c23ab2c&order=desc";
        if (args.length > 0)
        {
            destUri = args[0];
        }
        SslContextFactory sslContextFactory = new SslContextFactory(true);
        WebSocketClient client = new WebSocketClient(sslContextFactory);
        SimpleSocketHandler socket = new SimpleSocketHandler();
        try
        {
            client.start();

            URI echoUri = new URI(destUri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            //Origin 必填
            request.setHeader("Origin","http://loraflow.io");
            client.connect(socket,echoUri,request);
            System.out.printf("Connecting to : %s%n",echoUri);
            // wait for closed socket connection.
            socket.awaitClose(15,TimeUnit.SECONDS);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        finally
        {
            try
            {
                client.stop();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}