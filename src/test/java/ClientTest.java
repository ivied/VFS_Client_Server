import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.net.Socket;

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
        client.connect(new ConnectData(new String[]{"connect","169.254.119.145[:5463]","test"}));

        Assert.assertTrue(client.serverSocket.isConnected()) ;
    }

    @Test
    public void ping() {
      //  client.ping();
    }




}
