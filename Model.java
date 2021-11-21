

/**
 * The model contains all of the functionality and business
 * logic of the application. In this case, it contains methods
 * to process logic after a button has been pressed such as: 
 * initialisation, changing passwords, withdrawing/depositing,
 * checking balance, and changing states.
 */
import javax.crypto.SecretKey;
public class Model 
{
    /**
     * The ATM can be in 1 of 5 states: waiting for account number,
     * waiting for password, logged in, password change, and password change confirmation.
     */
    private final String ACCOUNT_NO = "account_no";
    private final String PASSWORD = "password";
    private final String LOGGED_IN = "logged_in";
    private final String PASSWD_CHANGE = "password_change";
    private final String PASSWD_CHANGE_CONFIRM = "password_change_confirm";

    /**
     * Instance variables
     */
    private String state = ACCOUNT_NO;      // the state it is currently in
    int  number = 0;                // current number displayed in GUI (as a number, not a string)
    Bank  bank = null;              // The ATM talks to a bank, represented by the Bank object.
    private int accNumber = -1;             // Account number typed in
    private int accPasswd = -1;             // Password typed in
    // These three are what are shown on the View display
    String title = "Bank ATM";      // The contents of the title message
    String display1 = null;         // The contents of the Message 1 box (a single line)
    String display2 = null;         // The contents of the Message 2 box (may be multiple lines)

    // The other parts of the model-view-controller setup
    public View view;
    public Controller controller;

    /**
     * Model constructor
     */
    public Model(Bank b)
    {
        Debug.trace("Model::<constructor>");          
        bank = b;
    }

    /**
     * The initialise method is used for resetting the program
     * after an error, or whenever the ATM needs to return to its
     * ACCOUNT_NO state
     */
    public void initialise(String message) {
        setState(ACCOUNT_NO);
        number = 0;
        display1 = message; 
        display2 =  "Enter your account number\n" +
        "Followed by \"Ent\"";
    }
    
    
    
    /**
     * Public getter for account number
     */
    public int getAccNum() {
        return accNumber;
    }
    
    /**
     * Public setter for account number
     */
    public void setAccNum(int newAccNumber) {
        this.accNumber = newAccNumber;
    }
    
    /**
     * Public setter for account password
     */
    public void setAccPw(int newAccPw) {
        this.accPasswd = newAccPw;
    }
    
    /**
     * Public getter for account password
     */
    public int getAccPass() {
        return accPasswd;
    }
    /** Method to change state - mainly so we print a debugging message whenever 
     * the state changes
    */
    public void setState(String newState) 
    {
        if ( !state.equals(newState) ) 
        {
            String oldState = state;
            state = newState;
            Debug.trace("Model::setState: changed state from "+ oldState + " to " + newState);
        }
    }
    
    /**
     * Method to check that the length of an int (in this case the new password),
     * returns true if between 4-8 digits
     */
    
    public boolean passwdLengthCheck(int pw) {
        int length = (int) (Math.log10(pw) + 1);
        if (length < 4 || length >= 9) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * The rest of the methods are Called by the Controller to affect the Model,
     * when buttons are pressed in the GUI
     */
    
    /**
     * Process a number key
     */
    public void processNumber(String label)
    {
        // a little magic to turn the first char of the label into an int
        // and update the number variable with it
        char c = label.charAt(0);
        number = number * 10 + c-'0';           // Build number 
        // show the new number in the display
        display1 = "" + number;
        display();  // update the GUI
    }

    /**
     * Clear button, clear the display
     */
    public void processClear()
    {
        // clear the number stored in the model
        number = 0;
        display1 = "";
        display();  // update the GUI
    }

    
    
    /**
     * Enter button. Execute different logic based on the
     * current state.
     * 
     *  ACCOUNT_NO: Store the user input and save it to accNumber. Set state to PASSWORD
     * and change the display. 
     * 
     *  PASSWORD: Store the user input and save it to accPasswd. Clear the display,
     * continue with bank.login() method. If successful, set state to LOGGED_IN. 
     * 
     *  PASSWD_CHANGE: Store the user input and save it to accPasswd. Clear the display,
     * and perform the passwdLengthCheck() method passing accPasswd as a parameter,
     * If the new password passes the check, change the state to PASSWD_CHANGE_CONFIRM. 
     * 
     *  PASSWD_CHANGE_CONFIRM: Checks the user input against the password entered in the
     * PASSWD_CHANGE state, if they match, continue with the bank.changePassword() method. 
     * 
     *  LOGGED_IN: If the enter button is pressed in this state,
     * do nothing
     */
    public void processEnter() throws Exception
    {
        switch ( state )
        {
            case ACCOUNT_NO:
                accNumber = number;
                number = 0;
                setState(PASSWORD);
                display1 = "";
                display2 = "Now enter your password\n" +
                "Followed by \"Ent\"";
                break;
            case PASSWORD:
                accPasswd = number;
                number = 0;
                display1 = "";
                /**
                 * Execute the login() method passing accNumber and accPasswd as parameters
                 */
                if ( bank.login(accNumber, accPasswd) )
                {
                    setState(LOGGED_IN);
                    display2 = "Accepted\n" +
                    "Now enter the transaction you require";
                } else {
                    initialise("Unknown account/password");
                }
                break;
            case PASSWD_CHANGE:
                accPasswd = number;
                number = 0;
                if (passwdLengthCheck(accPasswd)) {
                    setState(PASSWD_CHANGE_CONFIRM);
                    display2 = "Now confirm your passcode\n" + "Followed by \"Ent\"";
               } else {
                   display2 = "Please enter a Passcode between 4-8 digits in length";
               }
                
            break;
            case PASSWD_CHANGE_CONFIRM:

                if (number == accPasswd) {
                    //Replace the previous password with the new one
                    bank.changePassword(accPasswd);
                    setState(LOGGED_IN);
                    display2 = "Passcode Changed Sucessfully";
                } else {
                    setState(PASSWD_CHANGE);
                    display1 = "";
                    display2 = "Passcodes did not match \n" + "Please enter a new passcode";
                }
            break;
            
            case LOGGED_IN:     
            default: 
        }  
        display();  // update the GUI
    }

    /**
     * Withdraw button, checks if the user is logged in
     * and that the amount to withdraw is not higher than the current
     * balance. If those conditions are met: execute the bank.withdraw() method.
     */
    public void processWithdraw()
    {
        if (state.equals(LOGGED_IN) ) {            
            if ( bank.withdraw( number ) )
            {
                display2 =   "Withdrawn: " + number;
            } else {
                display2 =   "You do not have sufficient funds";
            }
            number = 0;
            display1 = "";           
        } else {
            initialise("You are not logged in");
        }
        display();  // update the GUI
    }

    /**
     * Deposit button, checks if the user is logged in,
     * if so, process the bank.deposit() method.
     */
    public void processDeposit()
    {
        if (state.equals(LOGGED_IN) ) {
            bank.deposit( number );
            display1 = "";
            display2 = "Deposited: " + number;
            number = 0;
        } else {
            initialise("You are not logged in");
        }
        display();  // update the GUI
    }

    /**
     * Balance button, check if the user is logged in,
     * if so, execute the bank.getBalance() method.
     */
    public void processBalance()
    {
        if (state.equals(LOGGED_IN) ) {
            number = 0;
            display2 = "Your balance is: " + bank.getBalance();
        } else {
            initialise("You are not logged in");
        }
        display();  // update the GUI
    }
    
    /**
     * Password Change - Check if logged in, if so, enter the password change state
     */
    public void processPass()
    {
        if (state.equals(LOGGED_IN) ) {
            setState(PASSWD_CHANGE);
            display2 = "Please enter a new password";
        } else {
            initialise("Please log in to change password");
        }
        display();  // update the GUI
    }
    
    /**
     * Finish button, checks if the user is logged in, if so,
     * log the user out.
     */
    public void processFinish()
    {
        if (state.equals(LOGGED_IN) ) {
            // go back to the log in state
            setState(ACCOUNT_NO);
            number = 0;
            display2 = "Welcome: Enter your account number";
            bank.logout();
        } else {
            initialise("You are not logged in");
        }
        display();  // update the GUI
    }

    /**
     * Unknown key inputs intialises and prints "Invalid command"
     */
    public void processUnknownKey(String action)
    {
        // unknown button, or invalid for this state - reset everything
        Debug.trace("Model::processUnknownKey: unknown button \"" + action + "\", re-initialising");
        // go back to initial state
        initialise("Invalid command");
        display();
    }

    /**
     * How the model communicates with the view, executes the view's
     * update() method.
     */
    public void display()
    {
        Debug.trace("Model::display");
        view.update();
    }
}
