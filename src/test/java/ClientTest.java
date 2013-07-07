import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 28.06.13
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */
public class ClientTest {

    Client client;
     @Before
     public void newClientAndTestData(){
         client = new Client();

     }

    @Test
    public void testConnect() throws Exception {
        client.connect(new ConnectData(new String[]{"connect","169.254.119.145:5463","test"}));

        Assert.assertTrue(client.serverSocket.isConnected()) ;

        client.connect(new ConnectData(new String[]{"connect","localhost:5463","test2"}));

        Assert.assertTrue(client.serverSocket.isConnected()) ;
        for(int i = 0 ; i < 100 ; i++){
            Client client =new Client() ;
            client.connect(new ConnectData(new String[]{"connect","localhost:5463","test" + i}));
            Assert.assertTrue(client.serverSocket.isConnected()) ;
            List<String> answer = client.messagingWithServer("ping");
            Assert.assertTrue(answer == null) ;
        }
    }

    @Test
    public void ping() {
      //  client.ping();
    }




}
