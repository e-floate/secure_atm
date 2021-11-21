

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class BankAccountTest.
 */
public class BankAccountTest
{
    /**
     * Default constructor for test class BankAccountTest
     */
    public BankAccountTest() throws Exception
    {
    }
    Bank b = new Bank();
    BankAccount x = b.makeBankAccount(10001, Symmetric.createAESKey(), Symmetric.createInitializationVector(), 11111, 100);
    boolean z = b.addBankAccount(x);
    
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }
    
    @Test
    public void shouldAddAccount() {
        assertTrue("Test1",z);
    }
    
    @Test
    public void shouldAutoEncryptPassword() {
        assertEquals("Test1",0,x.getAccPw());
        assertNotNull("Test2",x.getCypher());
    }
    
    @Test
    public void shouldWithdraw() {
        x.withdraw(25);
        int b = x.getBalance();
        assertEquals("Test1",75,b);
        boolean y = x.withdraw(9999);
        assertFalse("Test2",y);
    }
    
    @Test
    public void shouldDeposit() {
        int b = x.getBalance();
        x.deposit(5);
        int c = x.getBalance();
        int d = (c-b);
        assertEquals("Test1",5,d);
        boolean y = x.deposit(-9);
        assertFalse("Test2",y);
    }
    
    @Test
    public void shouldChangePassword() throws Exception {
        String p = x.getCypher();
        x.changePassword(55555);
        String l = x.getCypher();
        assertNotEquals("Test1",l,p);
        int k = x.getAccPw();
        assertEquals("Test2",0,k);
    }
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }
}
