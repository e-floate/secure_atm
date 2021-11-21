import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.*;

import javafx.application.Application;
import javafx.stage.Stage;
import static org.junit.Assert.*;
import org.junit.*;


/**
 * The test class ModelTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class ModelTest //extends Main
{
     /**
     * Default constructor for test class ModelTest
     */
    public ModelTest()
    {
        // Nothing needed
        
    }

    
    
    Bank b = new Bank();
    Model mod = new Model(b);
    private int tmpAccNo = 33333;
    private int tmpAccPw = 22322;
    public Main m;
    
    
    
    @Test
    public void shouldChangeState() {
        mod.setState("account_no");
        int ACCOUNT_NO = mod.getAccNum();
        int ACCOUNT_PASS = mod.getAccPass();
        assertEquals("Test1",-1, ACCOUNT_NO);
        assertEquals("Test2",-1, ACCOUNT_PASS);
    }
    
    @Test
    public void shouldInitialise() {
        mod.initialise("Test");
        assertEquals("Test1",0,mod.number);
        assertEquals("Test2","Test",mod.display1);
        assertEquals("Test3","Enter your account number\n" +
        "Followed by \"Ent\"", mod.display2);
    }
    
    @Test
    public void shouldCheckPasswdLength() {
        Boolean a = mod.passwdLengthCheck(1234);
        Boolean b = mod.passwdLengthCheck(1234567890);
        Boolean c = mod.passwdLengthCheck(1);
        assertTrue("Test1",a);
        assertFalse("Test2",b);
        assertFalse("Test3",c);
    }
    
    @Test
    public void shouldPressEnter() throws Exception {
        
        //Stage win = m.window;
        
        //m.start();
        
        View  view  = new View();
        Controller controller  = new Controller();
        mod.view = view;
        mod.controller = controller;
        
        controller.model = mod;
        controller.view = view;
        
        view.model = mod;
        view.controller = controller;
        
        
        
        //view.start();
        
        mod.setState("account_no");
        mod.processEnter();
        assertEquals("Test1", "Now enter your password\n" +
                "Followed by \"Ent\"",mod.display2);
        
        b.makeBankAccount(tmpAccNo, Symmetric.createAESKey(), Symmetric.createInitializationVector(), tmpAccPw, 50);      
        mod.setState("password");
        mod.setAccNum(tmpAccNo);
        mod.setAccPw(tmpAccPw);
        mod.processEnter();
        assertEquals("Test2", "Accepted\n" +
                    "Now enter the transaction you require", mod.display2);
    }
    
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
        Model mod = null;
    }
}