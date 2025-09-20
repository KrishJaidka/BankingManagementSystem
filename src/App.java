import java.util.InputMismatchException;
import java.util.Scanner;
class Account {
    int account_number;
    String account_holder_name;
    double balance;
    String email;
    String phone_number;
    static int base_account_no = 25000000; //to generate an account num from base

    void deposit(double amount){
        this.balance+=amount;
        System.out.println("Rs"+amount+" credited to account "+account_number+" successfully.");
        System.out.println("Updated account balance: "+balance);
    }

    void withdraw(double amount){
        this.balance-=amount;
        System.out.println("Rs"+amount+" debited from account "+account_number+" successfully.");
        System.out.println("Updated account balance: "+balance);
    }

    void displayAccountDetails(){
        System.out.println("======== BANK ACCOUNT DETAILS ========");
        System.out.println("Account holder name: "+account_holder_name);
        System.out.println("Account number: "+account_number);
        System.out.println("Current balance: "+balance);
        System.out.println("Phone number: "+phone_number);
        System.out.println("E-mail address: "+email);
        System.out.println("=======================================");
    }

    void updateContactDetails(String email,String phone_number){
        if (!email.isEmpty()) {
            this.email = email;
        }
        if (!phone_number.isEmpty()) {
            this.phone_number = phone_number;
        }
        System.out.println("Contact details for account "+account_number+" updated successfully.");
    }

    Account(String name, double amount, String email, String phone){
        this.account_holder_name = name;
        this.balance = amount;
        this.account_number = ++base_account_no;
        this.email = email;
        this.phone_number = phone;
        System.out.println("Account "+account_number+" created successfully.");
    }
}

class UserInterface {
    static Scanner input = new Scanner(System.in);
    static Account[] accounts = new Account[100]; // max 100 accounts can be stored.
    static int total_accounts = 0;

    static void mainMenu() {
        String option;
        do {
            System.out.println("========= BANKING APPLICATION =========");
            System.out.println("1) Create a new account");
            System.out.println("2) Deposit money");
            System.out.println("3) Withdraw money");
            System.out.println("4) View account details");
            System.out.println("5) Update contact details");
            System.out.println("6) Exit program");
            System.out.println("=======================================");
            System.out.print("Enter an option(1-6): ");
            option = input.nextLine();

            switch (option) {
                case "1":
                    createAccount();
                    break;
                case "2":
                    performDeposit();
                    break;
                case "3":
                    performWithdrawal();
                    break;
                case "4":
                    showAccountDetails();
                    break;
                case "5":
                    updateContact();
                    break;
                case "6":
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid option!");
                    break;
            }

        } while (!option.equals("6")); // runs until user doesn't exit program.
    }

    static void createAccount(){
        String name,email,phone;
        Double amount;

        // takes input and makes name field mandatory.
        do {
            System.out.println("Enter your full name:");
            name = input.nextLine();
            if (name.isEmpty()){
                System.out.println("Name field is required. Enter again.");
            }
        } while (name.isEmpty());

        // takes input and handles negative initial balance.
        while(true) {
            amount = inputAmount();
            if (amount != null){
                break;
            }
        }

        while(true) {
            System.out.println("Enter your e-mail address:");
            email = input.nextLine();
            if (isValidEmail(email)){
                break;
            }
        }
        while(true) {
            System.out.println("Enter your phone number:");
            phone = input.nextLine();
            if (isValidPhoneNumber(phone)){
                break;
            }
        }

        accounts[total_accounts] = new Account(name,amount,email,phone); // creates new account in array.
        total_accounts++;

        if (total_accounts > accounts.length){ // checks if array is full
            System.out.println("Array is full! Cannot create more accounts.");
            return;
        }
    }

    static void performDeposit(){
        Integer search_num = inputAccountNumber();
        Double amount = inputAmount();

        if (search_num == null) {
            return;
        }
        if (amount == null){
            return;
        }

        int index = findAccountIndex(search_num);
        if (index != -1) {
            accounts[index].deposit(amount);
        }
    }

    static void performWithdrawal(){
        Integer search_num = inputAccountNumber();
        Double amount = inputAmount();

        if (search_num == null) {
            return;
        }
        if (amount == null){
            return;
        }

        int index = findAccountIndex(search_num);
        if (index != -1) {
            accounts[index].withdraw(amount);
        }
    }

    static void showAccountDetails(){
        Integer search_num = inputAccountNumber();
        if (search_num == null) {
            return;
        }
        int index = findAccountIndex(search_num);
        if (index != -1) {
            accounts[index].displayAccountDetails();
        }
    }

    static void updateContact(){
        Integer search_num = inputAccountNumber();
        String phone,email;
        if (search_num == null) {
            return;
        }
        int index = findAccountIndex(search_num);
        if (index != -1){
            while(true) {
                System.out.println("Enter your e-mail address:");
                email = input.nextLine();
                if (isValidEmail(email)){
                    break;
                }
            }
            while(true) {
                System.out.println("Enter your phone number:");
                phone = input.nextLine();
                if (isValidPhoneNumber(phone)){
                    break;
                }
            }
            accounts[index].updateContactDetails(email,phone);
        }
    }

    // validates email address.
    static boolean isValidEmail(String email) {
        String[] parts = email.split("@");
        if (parts.length != 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
            System.out.println("Invalid e-mail address. Try again.");
            return false;
        }
        if (!parts[1].contains(".")) {
            System.out.println("Invalid e-mail address. Try again.");
            return false;
        }
        return true;
    }

    // validates phone number.
    static boolean isValidPhoneNumber(String phone){
        if (phone.length() != 10 ){
            System.out.println("Invalid number of digits. Try again.");
            return false;
        }
        for (int i=0;i<phone.length();i++){
            if (!Character.isDigit(phone.charAt(i))) {
                System.out.println("Invalid character in phone number. Try again.");
                return  false;
            }
        }
        return true;
    }

    // searches for target account index.
    static int findAccountIndex(int search_num){
        int index = 0;
        for (Account i: accounts){
            if (i == null){
                break;
            }
            if (i.account_number == search_num){
                return index;
            }
            index++;
        }
        System.out.println("No account found associated with " + search_num);
        return -1;
    }

    // takes account number input from user and validates it
    static Integer inputAccountNumber(){
        int search_num;
        try {
            System.out.println("Enter account number:");
            search_num = input.nextInt();
            input.nextLine(); // clears input buffer.
        } catch (InputMismatchException e){
            System.out.println("Error! invalid account number");
            input.nextLine(); // clears invalid input buffer.
            return null;
        }
        return search_num;
    }

    // takes amount input from user and validates it
    static Double inputAmount(){
        double amount;
        try {
            System.out.println("Enter amount (Rs):");
            amount = input.nextDouble();
            input.nextLine(); // clears input buffer.
            if (amount < 0) {
                System.out.println("Initial balance can't be negative.");
                return null;
            }
        } catch (InputMismatchException e){
            System.out.println("Input type must be Numbers.");
            input.nextLine(); // clears invalid input buffer.
            return null;
        }
        return amount;
    }

    public static void main(String[] args) {
        mainMenu();
    }
}
