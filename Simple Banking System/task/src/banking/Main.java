package banking;

import java.util.Scanner;

import static banking.Account.isValidCard;

public class Main {

    public static void main(String[] args) {
        AccountDB ops = new AccountDB(args[1]);
        String menu = "1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit\n";
        String subMenu = "1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit\n";
        int choice;
        Scanner scanner = new Scanner(System.in);
        Account user;

        do {
            System.out.print(menu);
            choice = scanner.nextInt();
            switch (choice) {
                case 1 :
                    //start account creation
                    user = new Account();
                     if (!ops.containsRecord(user.getCardNumber(), user.getPinNumber())) {
                         ops.insertData(user.getCardNumber(), user.getPinNumber());
                         System.out.println("Your card has been created");
                         user.accountInformation();
                    } else {
                        user = new Account();
                        ops.insertData(user.getCardNumber(), user.getPinNumber());
                        System.out.println("Your card has been created");
                        user.accountInformation();
                    }
                    break;
                    //end account creation
                case 2 :
                    //start login dialog
                    System.out.println("Enter your card number:");
                    String cardNumber = scanner.next();
                    if (!isValidCard(cardNumber)) {
                        System.out.println("Probably you made mistake in the card number. Please try again!");
                        break;
                    }
                    if (!ops.accountNumberExists(cardNumber)) {
                        System.out.println("Such a card does not exist.");
                        break;
                    }
                    System.out.println("Enter your PIN:");
                    String pin = scanner.next();
                    if (ops.containsRecord(cardNumber, pin)) {
                        int sub;
                        System.out.println("You have successfully logged in!");
                        do {
                            System.out.println(subMenu);
                            sub = scanner.nextInt();

                           switch (sub) {
                               case 1 :
                                   ops.printBalance(cardNumber, pin);
                                   break;
                               case 2:
                                   //add to balance
                                   System.out.println("Enter income: ");
                                   int amount = scanner.nextInt();
                                   ops.addToBalance(cardNumber, amount);
                                   System.out.println("Income was added!");
                                   break;
                               case 3:
                                   //transfer from one account to another
                                   System.out.println("Enter card number: ");
                                   String transferTo = scanner.next();
                                   //check if card # is valid and is in DB
                                   if (!isValidCard(transferTo)) {
                                       System.out.println("Probably you made mistake in the card number. Please try again!");
                                       break;
                                   }
                                   if (!ops.accountNumberExists(transferTo)) {
                                       System.out.println("Such a card does not exist.");
                                       break;
                                   }
                                   System.out.println("Enter how much money you want to transfer:");
                                   int transferAmount = scanner.nextInt();
                                   ops.transferAction(cardNumber,transferTo, transferAmount);
                                   break;
                               case 4:
                                   ////delete record from table
                                   ops.closeAccount(cardNumber);
                                   System.out.println("The account has been closed!");
                                   break;
                               case 5 :
                                  System.out.println("You have successfully logged out!");
                                  cardNumber = "";
                                  pin = "";
                                  break;
                               case 0 :
                                   System.out.println("Bye!");
                                   return;
                           }
                       } while (sub != 5 && sub != 4);

                    } else {
                        System.out.println("Wrong card number or PIN!");
                    }
                    //end login dialog
                    break;
                case 0 :
                    System.out.println("Bye!");
                    return;
            }
        } while (choice != 0);

    }
}
