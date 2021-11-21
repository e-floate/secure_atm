/**
 * Bank class - simple implementation of a bank, with a list of bank accounts, and
 * a current account that we are logged in to. This class contains methods that link
 * the logic in the Model class with the account object. These include: logging in, 
 * withdrawing/depositing money, changing password etc.
*/
import javax.crypto.SecretKey;
import java.util.Arrays;
public class Bank
{
    /**
     * Instance variables
     */
    int maxAccounts = 10;       // maximum number of accounts the bank can hold
    int numAccounts = 0;        // the number of accounts currently in the bank
    BankAccount[] accounts = new BankAccount[maxAccounts];  // array to hold the bank accounts
    BankAccount account = null; // currently logged in acccount ('null' if no-one is logged in)

    /**
     * Default constructor method
     */
    public Bank()
    {
        Debug.trace( "Bank::<constructor>"); 
    }

    /**
     * Constructor method that takes the parameters: account number, encryption key,
     *  initialisation vector, plain text password, and balance.
     */
    public BankAccount makeBankAccount(int a, SecretKey k, byte[] v, int p, int b) throws Exception
    {
        return new BankAccount(a, k, v, p, b);
    }
    
    /**
     * A method that checks whether the maximum amount of accounts
     * has been reached, if not, add the account to the accounts[] array.
     *  Incriment the amount of bank accounts by 1.
     */
    public boolean addBankAccount(BankAccount a)
    {
        if (numAccounts < maxAccounts) {
            accounts[numAccounts] = a;
            numAccounts++ ;
            Debug.trace( "Bank::addBankAccount: added " + 
                         a.getAccNumber() + " £"+ a.getBalance());
            return true;
        } else {
            Debug.trace( "Bank::addBankAccount: can't add bank account - too many accounts"); 
            return false;
        }                
    }
    
    /**
     * The login method for the bank class.
     *  Firstly it logs the user out, then it loops through each
     *  BankAccount in the array accounts[], comparing the account number that the user input with that account. 
     *  If the account numbers match, the program
     *  then retrieves the encryption key and intialisation vector from the current account.
     *   Then, the method performs an AES encryption using the those variables and the password
     *   entered by the user. If the cipher text just generated matches the cipher text stored
     *   in the account, we know the passwords match, and the user logs in successfully.
     */
    public boolean login(int newAccNumber, int newAccPasswd) throws Exception
    { 
   
        Debug.trace( "Bank::login: accNumber = " + newAccNumber);       
        logout(); // logout of any previous account
        
        // search the array to find a bank account with matching account number
        for (BankAccount b: accounts) {
            if (b.getAccNumber() == newAccNumber) {
               // found the right account
               SecretKey key; 
               byte[] vec;
               byte[] cypher;
               String newAccPasswdStr;
               
               key = b.getKey();
               vec = b.getInitVector(); 
               
               newAccPasswdStr = Integer.toString(newAccPasswd);
               cypher = Symmetric.do_AESEncryption(newAccPasswdStr, key, vec);
               
               String cypherString = Arrays.toString(cypher);
               String newCypherString = b.getCypher();
               
               if(cypherString.equals(newCypherString)) {
                   Debug.trace( "Bank::login: logged in, accNumber = " + newAccNumber ); 
                   account = b;
                   return true;
               }
            }
        }    

        // not found - return false
        account = null;
        return false;

    }
    
    

    /**
     * Logout the user if they are logged in
     */
    public void logout() 
    {
        if (loggedIn())
        {
            Debug.trace( "Bank::logout: logging out, accNumber = " + account.getAccNumber());
            account = null;
        }
    }
    
    /**
     * Test whether the user is logged in, if so, return true
     */
    public boolean loggedIn()
    {
        if (account == null)
        {
            return false;
        } else {
            return true;
        }
    }   
    
    /**
     * Calls the account deposit() method if logged in
     */
    public boolean deposit(int amount) 
    {
        if (loggedIn()) {
            return account.deposit(amount);   
        } else {
            return false;
        }
    }
    
    /**
     * Calls the account withdraw() method if logged in
     */
    public boolean withdraw(int amount) 
    {
        if (loggedIn()) {
            return account.withdraw(amount);   
        } else {
            return false;
        }
    }
    
    /**
     * Calls the account balance() method if logged in
     */
    public int getBalance() 
    {
        if (loggedIn()) {
            return account.getBalance();   
        } else {
            return -1; // use -1 as an indicator of an error
        }
    }
    
    /**
     * Calls the account changePassword() method if logged in
     */
    public void changePassword(int accPasswd) throws Exception {
        Debug.trace( "LocalBank::changePassword" ); 
        if (loggedIn()) {
            account.changePassword(accPasswd);
        }
    } 
}