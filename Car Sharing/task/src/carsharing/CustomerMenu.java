package carsharing;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Menu for customers class
 */
public class CustomerMenu {
    Scanner scanner = new Scanner(System.in);
    DataSource dataSource;
    public static final String BACK = "0. Back";

    /**
     * creates menu object, initializes datasource to handle database and
     * prints a list of customers
     * @param dataSource
     */
    public CustomerMenu(DataSource dataSource) {
        this.dataSource = dataSource;
        printCustomerList(dataSource.queryCustomerList());
    }

    /**
     * prints a list of customers and lets the user choose one from them
     * @param customers
     */
    private void printCustomerList(List<Customer> customers) {
        if (!customers.isEmpty()) {
            System.out.println("Choose a customer: ");
            for (Customer customer : customers) {
                System.out.println(customer.getId() + ". " + customer.getName());
            }
            System.out.println(BACK);
            System.out.println();

            try {
                int i = scanner.nextInt();
                scanner.nextLine();
                if (i == 0) {
                    return;
                }
                customerMenu(i);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("The customer list is empty!");
            System.out.println();
        }
    }

    /**
     * menu for customers to handle car renting
     * user can choose from four options: renting a car, returning a car, checking rented car
     * or going back to main menu
     * @param customer_id ID of chosen customer
     * @throws SQLException if query would be unsuccessful
     */
    private void customerMenu(int customer_id) throws SQLException {
        Customer customer = dataSource.queryCustomerById(customer_id);
        while (true) {
            System.out.println("""
                    1. Rent a car
                    2. Return a rented car
                    3. My rented car
                    0. Back""");

            try {
                int input = scanner.nextInt();
                scanner.nextLine();

                switch (input) {
                    case 1 -> rentACar(customer);
                    case 2 -> {
                        if (customer.getRented_car_id() == null) {
                            System.out.println("You didn't rent a car!");
                        } else {
                            dataSource.updateCarAvailable(false, customer.getRented_car_id());
                            dataSource.updateCustomerWithCarId(null, customer.getId());
                            customer.setRented_car_id(null);
                            System.out.println("You've returned a rented car!");
                        }
                    }
                    case 3 -> {
                        if (customer.getRented_car_id() == null) {
                            System.out.println("You didn't rent a car!");
                        } else {
                            Car car = dataSource.queryCarById(customer.getRented_car_id());
                            Company company = dataSource.queryCompanyById(car.getCompany_id());
                            System.out.println("Your rented car:");
                            System.out.println(car.getName());
                            System.out.println("Company:");
                            System.out.println(company.getName());
                        }
                    }
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Wrong input!");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Handling renting a car. Checks if customer has already rented a car, and if not
     * rents a car by updating database with correct values
     * @param customer chosen customer
     * @throws SQLException if updating was unsuccessful
     */
    private void rentACar(Customer customer) throws SQLException {
        if (customer.getRented_car_id() != null) {
            System.out.println("You've already rented a car!");
            return;
        }
        Company company = getCompanyFromList(dataSource.queryCompanyList());
        if (company == null) {
            return;
        }

        Car car = getCarFromList(dataSource.queryAvailableCarList(company.getId()));
        if (car == null) {
            rentACar(customer);
        } else {
            dataSource.updateCustomerWithCarId(car.getId(), customer.getId());
            dataSource.updateCarAvailable(false, car.getId());
            car.setIs_available(false);
            customer.setRented_car_id(car.getId());
            System.out.println("You rented '" + car.getName() + "'");
        }
    }

    /**
     * prints a list of companies and lets the user choose from them
     * @param companies list of companies from database
     * @return returns chosen company
     */
    private Company getCompanyFromList(List<Company> companies) {
        if (!companies.isEmpty()) {
            System.out.println("Choose a company: ");
            for (Company company : companies) {
                System.out.println(company.getId() + ". " + company.getName());
            }
            System.out.println(BACK);
            System.out.println();
            try {
                int i = scanner.nextInt();
                scanner.nextLine();
                if (i == 0) {
                    return null;
                }
                return companies.get(i - 1);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }

        } else {
            System.out.println("The company list is empty!");
            System.out.println();
            return null;
        }
    }

    /**
     * Lists available cars of the chosen company and lets the user choose from them
     * @param cars list of available cars of a chosen company
     * @return returns chosen car
     */
    private Car getCarFromList(List<Car> cars) {
            if (!cars.isEmpty()) {
                System.out.println("Choose a car: ");

                for (Car car : cars) {
                    System.out.println((cars.indexOf(car) + 1) + ". " + car.getName());
                }
                System.out.println(BACK);
                try {
                    int i = scanner.nextInt();
                    scanner.nextLine();
                    if (i == 0) {
                        return null;
                    }
                    return cars.get(i - 1);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return null;
                }

            } else {
                System.out.println("The car list is empty!");
                System.out.println();
                return null;
            }

    }
}
