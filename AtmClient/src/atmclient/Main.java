/*
 * The Client
 */
package atmclient;

import java.io.*;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Giannouloudis Stergios
 * AEM: 1877
 */
public class Main {

    public static Scanner in = new Scanner(System.in);
    public static final int PORT = 25200;
    public static Socket clientSocket = null;
    public static DataOutputStream out = null;
    public static DataInputStream sin = null;
    public static boolean pinCheck = false;
    public static int pin = 0;
    public static int choice = -1;

    public static void main(String[] args) throws UnknownHostException, IOException, EOFException {

        //Creating Client's Socket
        try {
            clientSocket = new Socket("localhost", PORT);

            //Creating Streams
            sin = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            /*
            } catch (UnknownHostException e) {
            System.err.println("Unknown Host Exception at Clients main");
            System.exit(1);
            } catch (IOException e) {
            System.err.println("IO Exception at Clients main");
            System.exit(1);
            }
             */

            //Start
            do {
                menu();
                choice = getUsersChoice();

                switch (choice) {

                    case 1:
                        if (!pinCheck) {
                            out.writeBoolean(false);
                            System.out.println("Παρακαλώ εισάγετε το PIN");
                            try {
                                do {
                                    pin = in.nextInt();
                                } while (pin < 0);
                            } catch (InputMismatchException ex) {
                                System.out.println("Please give an integer");
                            }
                            out.writeInt(pin);

                            String pinCheckResponse = sin.readUTF();
                            System.out.println("" + pinCheckResponse);

                            String accountNoResponse = sin.readUTF();
                            System.out.println("" + accountNoResponse);

                            out.writeInt(choice);

                            pinCheck = true;
                        }
                        break;

                    case 2:

                        if (!pinCheck) {
                            out.writeBoolean(false);

                            pin = getPin();
                            out.writeInt(pin);

                            String pinCheckResponse = sin.readUTF();
                            System.out.println("" + pinCheckResponse);

                            String accountNoResponse = sin.readUTF();
                            System.out.println("" + accountNoResponse);

                            pinCheck = true;

                            deposit(choice);
                        } else {
                            out.writeBoolean(true);
                            sin.readUTF();
                            deposit(choice);
                        }
                        break;

                    case 3:
                        if (!pinCheck) {
                            out.writeBoolean(false);

                            pin = getPin();
                            out.writeInt(pin);

                            String pinCheckResponse = sin.readUTF();
                            System.out.println("" + pinCheckResponse);

                            String accountNoResponse = sin.readUTF();
                            System.out.println("" + accountNoResponse);

                            pinCheck = true;

                            withdraw(choice);
                        } else {
                            out.writeBoolean(true);
                            sin.readUTF();
                            withdraw(choice);
                        }
                        break;

                    case 4:
                        if (!pinCheck) {
                            out.writeBoolean(false);

                            pin = getPin();
                            out.writeInt(pin);

                            String pinCheckResponse = sin.readUTF();
                            System.out.println("" + pinCheckResponse);

                            String accountNoResponse = sin.readUTF();
                            System.out.println("" + accountNoResponse);

                            pinCheck = true;

                            out.writeInt(choice);
                            String informResponse = sin.readUTF();
                            System.out.println("" + informResponse);
                        } else {
                            out.writeBoolean(true);

                            sin.readUTF();

                            out.writeInt(choice);
                            String informResponse = sin.readUTF();
                            System.out.println("" + informResponse);
                        }

                        break;

                    case 5:
                        if (!pinCheck) {
                            out.writeBoolean(false);

                            pin = 0;
                            out.writeInt(pin);

                            //Diavazoun tis 2 grammes pou stelnei o server.
                            String pinCheckResponse =
                                    sin.readUTF();
                            String accountNoResponse =
                                    sin.readUTF();

                            pinCheck = true;

                            out.writeInt(choice);
                            System.out.println("Τερματισμος εφαρμογης.");
                        } else {

                            out.writeBoolean(true);
                            String accountNoResponse = sin.readUTF();

                            out.writeInt(choice);
                            System.out.println("Τερματισμος εφαρμογης.");
                        }
                        break;

                    default:
                        System.out.println("Default case. Something went wrong!");
                        break;

                }
            } while (choice != 5);

        } catch (UnknownHostException e) {
            System.err.println("Unknown Host Exception at Clients main");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("IO Exception at Clients main");
            System.exit(1);
        } finally {
            out.close();
            sin.close();
            clientSocket.close();
            in.close();
        }
    }

    /**
     * Displays the actions menu
     */
    public static void menu() {

        System.out.println("");
        System.out.println("Οι δνυατες ενέργειες είναι:");
        System.out.println("1. Επικύρωση κάρτας");
        System.out.println("2. Κατάθεση");
        System.out.println("3. Ανάληψη");
        System.out.println("4. Ενημέρωση");
        System.out.println("5. Έξοδος");
        System.out.println("------------");
    }

    /**
     * Takes the users input/choosen option
     * @return user's option
     */
    public static int getUsersChoice() {
        
        int choice2;
        try {
            do {
                System.out.println("Παρακαλώ επιλέξτε μεταξύ 1 και 5");
                System.out.println("");
                choice2 = in.nextInt();
            } while (choice2 < 0 || choice2 > 5);

            return choice2;
        } catch (InputMismatchException ex) {
            System.out.println("Please give an integer");
        }

        return 0;

    }

    /**
     * Requests the PIN number
     * so the prosses continues.
     * @return The pin. int.
     */
    public static int getPin() {
        System.out.println("Δεν έγινε επικύρωση κάρτας.");
        System.out.println("Παρακαλώ εισάγεται το PIN σας.");

        try {
            pin = in.nextInt();
        } catch (InputMismatchException ex) {
            System.out.println("Please give an integer");
        }
        return pin;
    }

    /**
     * Takes the users choice in order to send
     * it to the server.
     * Asks the ammount of the deposit and sends it to the server.
     * @param choice users choice
     * @throws IOException
     */
    public static void deposit(int choice) throws IOException {
        out.writeInt(choice);
        int depAmmount = 0;
        try {
            System.out.println("Δωστε το ποσο της καταθεσης (θετικος ακαιρεος).");
            do {
                depAmmount = in.nextInt();
            } while (depAmmount < 0);
        } catch (InputMismatchException ex) {
            System.out.println("Please give an integer");
        }
        out.writeInt(depAmmount);
        String depositeResponse = sin.readUTF();
        System.out.println("" + depositeResponse);
    }

    /**
     * Same as "deposit"
     * @param choice
     * @throws IOException
     */
    public static void withdraw(int choice) throws IOException {
        out.writeInt(choice);
        int withdrawAmmount = 0;
        try {
            System.out.println("Δωστε το ποσο της ανάληψης (θετικος ακαιρεος).");
            do {
                withdrawAmmount = in.nextInt();
            } while (withdrawAmmount < 0);
        } catch (InputMismatchException ex) {
            System.out.println("Please give an integer");
        }
        out.writeInt(withdrawAmmount);
        String withdrawResponse = sin.readUTF();
        System.out.println("" + withdrawResponse);
    }
}
