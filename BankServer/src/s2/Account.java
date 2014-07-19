/*
 * Class: Account
 *        Represents a bank account
 */

package s2;

/**
 *
 * @author Giannouloudis Stergios
 */
public class Account {

    private int pin;
    private int accountNo;
    private int balance;

    /**
     * Default Constructor
     */
    public Account(){
        pin = 0;
        accountNo = 0;
        balance = 0;

    }
    /**
     * Constructor
     *
     * @param pin   Users PIN
     * @param accNo Users cocount umber
     * @param bal   Accounts balance
     */
    public Account(int pin, int accNo, int bal){
        this.pin = pin;
        this.accountNo = accNo;
        this.balance = bal;
    }




    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    @Override
    public String toString(){
        String ok = "Account's data:\n" +
                    "PIN:    \t" + pin + "\n" +
                    "Nubmer: \t" + accountNo + "\n" +
                    "Balance:\t" + balance + "\n" +
                    "-----------------\n";
        /*
        System.out.println("Account's data:\n");
        System.out.println("PIN:\t" + pin);
        System.out.println("Nubmer:\t" + accountNo);
        System.out.println("Balance:\t" + balance);
        System.out.println("-----------------");
         */
        return ok;
    }

    /**
     * Performs a deposit on a users account
     * @param amount The amount of money to be deposited
     * @return A message confirming the success/fail of the transaction
     */
    public String deposit(int amount){

        if( amount >0){
            balance = balance + amount;
            return "Deposit succesfull";
        }
        else{
            return "Give a positive number";
        }

    }

    /**
     * Performs a withdraw from a users account
     * Checks if the account's balance is enough.
     * @param amount The amount of money to be withdrawn
     * @return A message confirming the success/fail of the transaction
     */
    public String withdraw(int amount){

        if ( (balance - amount) > 0 ){
            balance = balance - amount;
            return "Withdraw succesfull";
        }
        else{
            return "The balance of your account is not enough for this transaction.";
        }

    }

    /**
     * Informs the user for his accounts balance
     * @return The balance
     */
    public int update(){
        return balance;
    }

}
