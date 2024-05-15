package banking;

import java.util.Arrays;
import java.util.Random;

public class Account {
    private String pinNumber;
    private String issuer = "400000";
    private final int[] cardNumber = new int[16];
    private int balance = 0;

    public Account() {
        Random random = new Random();
        //generate pin
        int pin = random.nextInt(1000);
        setPinNumber(pin);
        //generate card number
        int cardNum = random.nextInt(1000000000);
        setCardNumber(cardNum);
    }

    public String getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(int pin) {
        this.pinNumber = String.format("%04d", pin);
    }

    public String getCardNumber() {
        StringBuilder string = new StringBuilder();
        for (int i : this.cardNumber) {
            string.append(i);
        }
        return string.toString();
    }

    public void setCardNumber(int cardNumber) {
        issuer += cardNumber;
        for (int i = 0; i < issuer.length() ; i++) {
            this.cardNumber[i] = Character.getNumericValue(issuer.toCharArray()[i]);
        }
        determineCheckSum(this.cardNumber);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void accountInformation() {
        System.out.println("Your card number:");
        System.out.println(getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(getPinNumber());
    }

    public void determineCheckSum(int[] cardNumber) {
        int sum;
        int checksum = 0;
        int[] temp = Arrays.copyOf(cardNumber, cardNumber.length);
        for (int i = 0; i < temp.length ; i++) {
            if (i%2 == 0) {
                temp[i] = temp[i] * 2;
            }
            if (temp[i] > 9) {
                temp[i] = temp[i] - 9;
            }
        }
        sum = Arrays.stream(temp).sum();
        for (int i = 0; i < 10; i++) {
            if ((sum + i)%10 == 0) {
                checksum = i;
                break;
            }
        }
        System.out.println(checksum);
        cardNumber[cardNumber.length-1] = checksum;
    }
    public static boolean isValidCard(String number) {
        int[] cardNum = new int[number.length()-1];
        int total = 0;
        String[] sArray = number.split("");
        for (int i = 0; i < cardNum.length; i++) {
            cardNum[i] = Integer.parseInt(sArray[i]);
        }
        int chksm =  Integer.parseInt(sArray[number.length()-1]);
        for (int j = 0; j < cardNum.length; j++) {
            if (j%2 == 0) {
                cardNum[j] = cardNum[j] *2;
            }
            if (cardNum[j] > 9) {
                cardNum[j] = cardNum[j] - 9;
            }
            total += cardNum[j];
        }
        total += chksm;
    return total%10 == 0;
    }

}
