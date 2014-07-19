package s2;

import java.io.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Giannouloudis Stergios
 * AEM:1877
 */
public class Server {

    public static final int PORT = 25200;
    public static ServerSocket serverSocket = null;
    public static Socket clientSocket = null;
    public static DataInputStream in = null;
    public static DataOutputStream out = null;
    public static ArrayList<Account> accs = new ArrayList();
    public static boolean pinCheck = false;
    public static int pin = 0;
    public static int cl = -1;

    public static void main(String[] args) throws IOException {


        createAccounts();

        System.out.println("Starting BankServer");
        System.out.println("-------------------");
        System.out.println("");
        
        //Dhmiourgia Server Socket
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Could not listen to the specified port.");
            System.exit(1);
        }

        //P1
        //Server constantly running
        while (true) {

            System.out.println("Waiting for connection...");

            //Socket connection
            try {
                clientSocket = serverSocket.accept();
                System.out.println("A client was connected!");

                //Streams
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());

                //P2
                //Repeat as long as a client is conected
                do {
                    pinCheck = in.readBoolean();
                    if (!pinCheck) {
                        pin = in.readInt();
                        System.out.println("Checking PIN...");
                        out.writeUTF("server> Checking PIN...");
                    }

                    //Chacking if the account exists
                    Account usersAccount = new Account();
                    usersAccount = getAccount(pin);
                    out.writeUTF("server> Account's number is: " + usersAccount.getAccountNo());

                    int usersChoice = in.readInt();
                    switch (usersChoice) {
                        case 1:
                            System.out.println("PIN checked");
                            break;

                        case 2:
                            System.out.println("\"Deposit\" at AccNo: " + usersAccount.getAccountNo() + ".");
                            int depAmmount = in.readInt();//diavazei to poso ths kata8eshs
                            usersAccount.deposit(depAmmount);
                            out.writeUTF("server> Το νεο υπολοιπο του λογαριασμου ειναι: " + usersAccount.getBalance());
                            break;

                        case 3:
                            System.out.println("\"Withdraw\" from AccNo: " + usersAccount.getAccountNo() + ".");
                            int withdrawAmmount = in.readInt();//diavazei to poso ths analhpshs
                            //Check for overdraft
                            if (usersAccount.getBalance() < withdrawAmmount) {
                                System.out.println("Overdraft!");
                                out.writeUTF("server> Το υπολοιπο του λογαριασμού σας δεν επαρκεί.\n" + "server> Υπολοιπο: " + usersAccount.getBalance());
                            } else {
                                usersAccount.withdraw(withdrawAmmount);
                                System.out.println("Succesfull withdraw from AccNo: " + usersAccount.getAccountNo() + ".");
                                out.writeUTF("server> Επιτυχης ανάληψη\n" + "server> Το νέο υπόλοιπο είναι: " + usersAccount.getBalance());
                            }
                            break;

                        case 4:
                            System.out.println("\"Update\"");
                            out.writeUTF("server> Το υπόλοιπο του λογαριασμού σας είναι: " + usersAccount.update());
                            break;

                        case 5:
                            System.out.println("Client chose to terminate the session");
                            System.out.println("-------------------------------------");
                            System.out.println("");
                            cl = 5;//flag to exit "do-loop"
                            break;

                        default:
                            System.out.println("Default case. Something went Wrong!");
                            break;
                    }
                } while (cl != 5);//end of do-loop
                cl = -1;//Reseting the flag for the next client!
            } catch (IOException e) {
                System.err.println("Failed to accept connection.");
                System.exit(1);
            } finally {

                clientSocket.close();
                in.close();
                out.close();
            }
        }//end of "while(true)".

    }

    /**
     * Returns the requested account
     * @param pin The requested account's PIN number
     * @return A specific account object
     */
    
    public static Account getAccount(int pins) {
        for (Account ac : accs) {

            if (pins == ac.getPin()) {
                return ac;
            } else {
                System.out.println("Could not find the specified account");
                //apagorevete na valeis edw return!
            }
        }
        return null;
    }
    

    /**
     * Creating the default accounts
     */
    public static void createAccounts() {
        //Creating the 4 accounts
        Account ac0 = new Account(0, 0, 0);
        Account ac1 = new Account(100, 1000, 10000);
        Account ac2 = new Account(200, 2000, 5000);
        Account ac3 = new Account(300, 3000, 6000);

        //Adding the default accounts to the list
        accs.add(ac1);
        accs.add(ac2);
        accs.add(ac3);
        accs.add(ac0);

    }

}
