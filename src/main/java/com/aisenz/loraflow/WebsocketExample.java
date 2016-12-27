package com.aisenz.loraflow;

import com.google.gson.Gson;
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
        //url 中加入feedback=true 可以收到消息回应
        String destUri = "wss://loraflow.io/v1/application/ws?appeui=1234657812345633&token=1v7pw4e565772e63066a09c23ab2c&feedback=true";
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

            URI connectUri = new URI(destUri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            //Origin 必填
            request.setHeader("Origin","http://loraflow.io");
            client.connect(socket,connectUri,request);
            System.out.printf("Connecting to : %s%n",connectUri);
            while(true)
            {
                Thread.sleep(1000*1);
                if(socket.getSession()==null)
                {
                    continue;
                }
                //下行
                Gson gson = new Gson();
                String content = gson.toJson(new Message("4786e6ed002a0036", 12, "11111"));
                System.out.println(content);
                socket.getSession().getRemote().sendString(content+'\n');
                Thread.sleep(1000*10);
            }
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