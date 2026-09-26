import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ATMSimulation {

    private static Map<String, double[]> accounts = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        accounts.put("1234", new double[]{1111, 50000.00});
        accounts.put("5678", new double[]{2222, 75000.00});
        accounts.put("9012", new double[]{3333, 120000.00});

        System.out.println("========================================");
        System.out.println("       WELCOME TO ATM SIMULATOR        ");
        System.out.println("========================================");

        while (true) {
            String cardNumber = authenticateUser();
            if (cardNumber == null) {
                System.out.println("Maximum attempts reached. Card blocked.");
                continue;
            }
            showMenu(cardNumber);
        }
    }

    private static String authenticateUser() {
        int attempts = 3;

        while (attempts > 0) {
            System.out.print("\nEnter Card Number: ");
            String cardNumber = scanner.nextLine().trim();

            if (!accounts.containsKey(cardNumber)) {
                attempts--;
                System.out.println("Invalid card number. Attempts remaining: " + attempts);
                continue;
            }

            System.out.print("Enter PIN: ");
            String pinInput = scanner.nextLine().trim();

            try {
                int pin = Integer.parseInt(pinInput);
                if (pin == (int) accounts.get(cardNumber)[0]) {
                    System.out.println("\nAuthentication successful!");
                    return cardNumber;
                } else {
                    attempts--;
                    System.out.println("Incorrect PIN. Attempts remaining: " + attempts);
                }
            } catch (NumberFormatException e) {
                attempts--;
                System.out.println("Invalid PIN format. Attempts remaining: " + attempts);
            }
        }
        return null;
    }

    private static void showMenu(String cardNumber) {
        boolean sessionActive = true;

        while (sessionActive) {
            System.out.println("\n========================================");
            System.out.println("            ATM MAIN MENU              ");
            System.out.println("========================================");
            System.out.println("  1. Check Balance");
            System.out.println("  2. Deposit Money");
            System.out.println("  3. Withdraw Money");
            System.out.println("  4. Transfer Money");
            System.out.println("  5. Change PIN");
            System.out.println("  6. Mini Statement");
            System.out.println("  7. Exit");
            System.out.println("========================================");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    checkBalance(cardNumber);
                    break;
                case "2":
                    deposit(cardNumber);
                    break;
                case "3":
                    withdraw(cardNumber);
                    break;
                case "4":
                    transfer(cardNumber);
                    break;
                case "5":
                    changePin(cardNumber);
                    break;
                case "6":
                    miniStatement(cardNumber);
                    break;
                case "7":
                    System.out.println("\nThank you for using the ATM. Goodbye!");
                    sessionActive = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void checkBalance(String cardNumber) {
        double balance = accounts.get(cardNumber)[1];
        System.out.println("\n----------------------------------------");
        System.out.println("  Account: XXXX-" + cardNumber);
        System.out.printf("  Available Balance: Rs. %.2f%n", balance);
        System.out.println("----------------------------------------");
    }

    private static void deposit(String cardNumber) {
        System.out.print("\nEnter deposit amount: Rs. ");
        String input = scanner.nextLine().trim();

        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) {
                System.out.println("Amount must be greater than zero.");
                return;
            }
            if (amount > 100000) {
                System.out.println("Maximum deposit limit is Rs. 100000 per transaction.");
                return;
            }
            accounts.get(cardNumber)[1] += amount;
            System.out.printf("%nRs. %.2f deposited successfully.%n", amount);
            System.out.printf("New Balance: Rs. %.2f%n", accounts.get(cardNumber)[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
        }
    }

    private static void withdraw(String cardNumber) {
        System.out.print("\nEnter withdrawal amount: Rs. ");
        String input = scanner.nextLine().trim();

        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) {
                System.out.println("Amount must be greater than zero.");
                return;
            }
            if (amount % 100 != 0) {
                System.out.println("Amount must be a multiple of 100.");
                return;
            }
            if (amount > 25000) {
                System.out.println("Maximum withdrawal limit is Rs. 25000 per transaction.");
                return;
            }
            double balance = accounts.get(cardNumber)[1];
            if (amount > balance) {
                System.out.println("Insufficient balance.");
                return;
            }
            accounts.get(cardNumber)[1] -= amount;
            System.out.printf("%nRs. %.2f withdrawn successfully.%n", amount);
            System.out.printf("Remaining Balance: Rs. %.2f%n", accounts.get(cardNumber)[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
        }
    }

    private static void transfer(String cardNumber) {
        System.out.print("\nEnter recipient card number: ");
        String recipient = scanner.nextLine().trim();

        if (!accounts.containsKey(recipient)) {
            System.out.println("Recipient account not found.");
            return;
        }
        if (recipient.equals(cardNumber)) {
            System.out.println("Cannot transfer to your own account.");
            return;
        }

        System.out.print("Enter transfer amount: Rs. ");
        String input = scanner.nextLine().trim();

        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) {
                System.out.println("Amount must be greater than zero.");
                return;
            }
            double balance = accounts.get(cardNumber)[1];
            if (amount > balance) {
                System.out.println("Insufficient balance.");
                return;
            }
            accounts.get(cardNumber)[1] -= amount;
            accounts.get(recipient)[1] += amount;
            System.out.printf("%nRs. %.2f transferred successfully to XXXX-%s.%n", amount, recipient);
            System.out.printf("Remaining Balance: Rs. %.2f%n", accounts.get(cardNumber)[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
        }
    }

    private static void changePin(String cardNumber) {
        System.out.print("\nEnter current PIN: ");
        String currentInput = scanner.nextLine().trim();

        try {
            int currentPin = Integer.parseInt(currentInput);
            if (currentPin != (int) accounts.get(cardNumber)[0]) {
                System.out.println("Incorrect current PIN.");
                return;
            }

            System.out.print("Enter new PIN (4 digits): ");
            String newInput = scanner.nextLine().trim();
            int newPin = Integer.parseInt(newInput);

            if (newInput.length() != 4) {
                System.out.println("PIN must be exactly 4 digits.");
                return;
            }

            System.out.print("Confirm new PIN: ");
            String confirmInput = scanner.nextLine().trim();
            int confirmPin = Integer.parseInt(confirmInput);

            if (newPin != confirmPin) {
                System.out.println("PINs do not match.");
                return;
            }

            accounts.get(cardNumber)[0] = newPin;
            System.out.println("PIN changed successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid PIN format.");
        }
    }

    private static void miniStatement(String cardNumber) {
        double balance = accounts.get(cardNumber)[1];
        System.out.println("\n========================================");
        System.out.println("          MINI STATEMENT               ");
        System.out.println("========================================");
        System.out.println("  Account : XXXX-" + cardNumber);
        System.out.println("  Type    : Savings Account");
        System.out.println("  Status  : Active");
        System.out.printf("  Balance : Rs. %.2f%n", balance);
        System.out.println("========================================");
    }
}
