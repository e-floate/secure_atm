
/**
 * BankAccount class
 * This class has instance variables for the account number, password and balance, and methods
 * to withdraw, deposit, check balance etc. This class also handles the ecryption of
 * of the sensitive instace variable accPasswd
*/

import java.security.SecureRandom;  
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Arrays;
public class BankAccount
{
    /**
     * Instance variables
     */
    
    private int accNumber = 0; //Account number
    private int accPasswd = 0; //Plaintext account password
    private String accPWCypher; //Ciphertext account password
    private int balance = 0; //Account balance
    private SecretKey aesKey; //Encryption Key
    private byte[] initVector; //Initialisation Vector
   
    /**
     * Default Constructor for BankAccount class
     * Initialises some encryption variables
     */
    public BankAccount() throws Exception
    {
        byte[] initVector = Symmetric.createInitializationVector();
        aesKey = Symmetric.createAESKey();
    }

    /**
     * Constructor that takes the previous parameters plus an
     * AES Encryption key, and initialisation vector.
     *  Auto encrypts plain text passcode and deletes it
     */
    public BankAccount(int a, SecretKey k, byte[] v, int p, int b) throws Exception {
        accNumber = a;
        aesKey = k;
        initVector = v;
        accPasswd = p;
        balance = b;
        
        autoEncrypt();
    }
    /**
     * Getter method for AES key
     */
    public SecretKey getKey() {
        return aesKey;
    }
    /**
     * Getter method for encrypted Passcode
     */
    public String getCypher() {
        return accPWCypher ;
    }
    /**
     * Getter method for initialisation vector
     */
    public byte[] getInitVector() {
        return initVector;
    }
    /**
     * Getter method for account number
     */
    public int getAccNumber() {
        return accNumber;
    }
    /**
     * Getter method for plaintext account password
     */
    public int getAccPw() {
        return accPasswd;
    }
    
    /**
     * Getter Method for Balance
     */
    public int getBalance() 
    { 
        Debug.trace( "LocalBank::getBalance" ); 
        return balance;
    }
    
    /**
     * Method to encrypt plain text passcode using the 
     * instances AES key and initialisation vector
     */
    public String encryptPasswd(int accPasswd, SecretKey aesKey, byte[] initVector) throws Exception {
        String accPasswdStr = Integer.toString(accPasswd);
        byte[] cypherText = Symmetric.do_AESEncryption(
                accPasswdStr,
                aesKey,
                initVector);
        
        return Arrays.toString(cypherText);
    }
    
    /**
     * Method that automatically encrypts any plain-text passcode and
     * deletes the plain-text version
     */
    public void autoEncrypt() throws Exception {
        while (accPasswd != 0) {
            accPWCypher = encryptPasswd(accPasswd, aesKey, initVector);
            accPasswd = 0;
        }
    }
    /**
     * Method to withdraw money from account if the
     * amount is below the account balance
     * Return true if successful
     */
    public boolean withdraw( int amount ) 
    { 
        Debug.trace( "BankAccount::withdraw: amount =" + amount ); 

        if (amount < 0 || balance < amount) {
            return false;
        } else {
            balance = balance - amount;
            return true; 
        }
    }
    
    /**
     * Method to deposit money into account if the
     * amount is above zero.
     * Return true if successful
     */
    public boolean deposit( int amount )
    { 
        Debug.trace( "LocalBank::deposit: amount = " + amount ); 
        if (amount < 0) {
            return false;
        } else {
            balance = balance + amount;  // add amount to balance
            return true; 
        }
    }

    
    /**
     * Method to change the account password, before
     * encrypting it and removing the plain-text version
     */
    public void changePassword(int pw) throws Exception {
        Debug.trace( "LocalBank::changePassword" ); 
        accPasswd = pw;
        autoEncrypt();
    }   
}
