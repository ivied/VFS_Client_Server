import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Serv
 * Date: 28.06.13
 * Time: 17:03
 * To change this template use File | Settings | File Templates.
 */
public class ConnectDataTest {
    ArrayList<String[]> commandList = new ArrayList<String[]>();
    @Before
    public void setUp() throws Exception {
        commandList.add(new String []{"connect","169.254.119.145[:5463]","lol"});
        commandList.add(new String []{"connect","",""});
        commandList.add(new String []{"connect"});
        commandList.add(new String []{"connect","169.254.119.145[:5f63]","lol"}) ;
        commandList.add(new String []{"connect","169.254.119.145[:5f63]","333"});
    }

    @Test
    public void ConnectData (){
        for(String [] command : commandList){
        ConnectData connectData = new ConnectData(command);

        }

    }
}
