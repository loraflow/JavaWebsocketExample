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
        String destUri = "wss://loraflow.io/v1/application/ws?appeui=1234657812345633&token=1v7pw4e565772e63066a09c23ab2c";
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
                //下行示例
                Gson gson = new Gson();
                //data 的数据长度必须是偶数的HEX编码，例如可以是"1234",不能是"123"
                //fport 在1-223之间
                String content = gson.toJson(new Message("4786e6ed002a0036", 12, "1111"));
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