package carsharing;

import java.util.Scanner;

/**
 * Main menu of this Car renting program
 */
public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final DataSource dataSource;

    /**
     * Constructor - initializes dataSource and opens it after
     * @param dataSource holds connection to database and database statements
     */
    public Menu(DataSource dataSource) {
        this.dataSource = dataSource;
        dataSource.open();
    }

    /**
     * Main menu of the program with four options to choose from
     * Can jump into manager menu, customer menu or create a new customer
     */
    public void loginMenu() {

        while (true) {
            System.out.println("""
                    1. Log in as a manager
                    2. Log in as a customer
                    3. Create a customer
                    0. Exit""");

            try {
                int input = scanner.nextInt();
                scanner.nextLine();

                switch (input) {
                    case 1 -> new ManagerMenu(dataSource);
                    case 2 -> new CustomerMenu(dataSource);
                    case 3 -> {
                        System.out.println("Enter the customer name:");
                        String name = scanner.nextLine();
                        dataSource.addCustomer(name);
                    }
                    case 0 -> {
                        System.out.println("Goodbye...");
                        dataSource.close();
                        System.exit(0);
                    }
                    default -> System.out.println("Wrong input!");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
