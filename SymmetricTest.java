

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Scanner;
  
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.util.Arrays;

/**
 * The test class SymmetricTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class SymmetricTest
{
    public Symmetric sym = new Symmetric();
    private static SecretKey key;
    public static byte[] initVector;
    public static byte[] cipher;
    public static String plainText = "24356";
    
    /**
     * Default constructor for test class SymmetricTest
     */
    public SymmetricTest()
    {
    }

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
    public void shouldCreateKey() throws Exception {
        SecretKey key = sym.createAESKey();
        assertNotNull("Test1", key);
    }
    
    @Test
    public void shouldCreateInitVector() {
        initVector = sym.createInitializationVector();
        assertNotNull("Test1",initVector);
    }
    
    @Test
    public void shouldEncrypt() throws Exception {
        initVector = sym.createInitializationVector();
        SecretKey key = sym.createAESKey();
        cipher = sym.do_AESEncryption(plainText, key, initVector);
        assertNotNull("Test1",cipher);
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
