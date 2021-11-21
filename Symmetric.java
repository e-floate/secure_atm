import java.security.SecureRandom;
import java.util.Scanner;
  
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.util.Arrays;
/**
 * The symmetric class handles the symmetric encryption of data
 * A modified version of code from https://www.geeksforgeeks.org/symmetric-encryption-cryptography-in-java/
 */
public class Symmetric
{
    // instance variables - replace the example below with your own
    private static final String AES = "AES";
    private static final String AES_CIPHER = "AES/CBC/PKCS5PADDING";
    private static Scanner message;
    private static KeyGenerator keygen;
    private static byte[] finCipher;
    /**
     * Constructor for objects of class Symmetric
     */
    public Symmetric()
    {
        
    }

      
    /**
     * Generates a 256 bit key, uses the SecureRandom class
     * to generate cryptographically strong random number 
     */
    public static SecretKey createAESKey()
    {
        SecureRandom secureran
            = new SecureRandom();
        keyGenerator();
  
        keygen.init(256, secureran);
        SecretKey key
            = keygen.generateKey();
  
        return key;
    }
    
    /**
     * Creates a key generator using AES encryption
     */
    public static void keyGenerator() {
        try {
            keygen = KeyGenerator.getInstance(AES);
        } catch(Exception e) {
            System.out.println("Error, invalid cryptographic algorithm used");
        }
    }
    
    /** Function to initialize a vector
     * with an arbitrary value
     * 
     * Used to make the encryption stronger 
     */ 
    public static byte[] createInitializationVector()
    {
  
        // Used with encryption
        byte[] initVector
            = new byte[16];
        SecureRandom secureRan
            = new SecureRandom();
        secureRan.nextBytes(initVector);
        return initVector;
    }
    
    /**
     * Method that initialises the cipher
     */
   public static void cipherEncrypt(Cipher cipher,SecretKey key, IvParameterSpec ivPar) {
       try {
           cipher.init(Cipher.ENCRYPT_MODE, key, ivPar);
        } catch (Exception e) {
            System.out.println("Encryption Unsuccessful, please check your parameters \n include: plaintext, a Secret Key, and an Initialisation Vector");
        }
    }

    /**
     * Method that takes the plain text, secret key, and
     * initialisation vector and creates returns ciphertext
     */
    public static byte[] do_AESEncryption(
        String plainText,
        SecretKey secretKey,
        byte[] initializationVector)
        throws Exception
    {
        Cipher cipher
            = Cipher.getInstance(
                AES_CIPHER);
  
        IvParameterSpec ivParameterSpec
            = new IvParameterSpec(
                initializationVector);
  
        cipherEncrypt(cipher, secretKey ,ivParameterSpec );
  
        byte[] finCipher;
           
        finCipher = cipher.doFinal(plainText.getBytes());
        return finCipher;
    }
}

