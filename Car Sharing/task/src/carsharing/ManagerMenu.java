package carsharing;

import java.util.List;
import java.util.Scanner;

/**
 * Menu and methods for managers for handling companies and their cars
 */
public class ManagerMenu {
    Scanner scanner = new Scanner(System.in);
    DataSource dataSource;

    /**
     * gets the same datasource from menu for handling database
     * opens manager menu
     * @param dataSource
     */
    public ManagerMenu(DataSource dataSource) {
        this.dataSource = dataSource;
        managerMenu();
    }

    /**
     * Manager menu with three options to choose from
     */
    private void managerMenu() {
        while (true) {
            System.out.println("""
                                    1. Company list
                                    2. Create a company
                                    0. Back""");
            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                switch (input) {
                    case 1 -> printCompanyList(dataSource.queryCompanyList());
                    case 2 -> {
                        System.out.println("Enter the company name:");
                        String name = scanner.nextLine();
                        dataSource.addCompany(name);
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
     * prints a list of companies and lets the user choose a company
     * @param companies list of companies in database
     */
    private void printCompanyList(List<Company> companies) {
        if (!companies.isEmpty()) {
            System.out.println("Choose a company: ");
            for (Company company : companies) {
                System.out.println(company.getId() + ". " + company.getName());
            }
            System.out.println("0. Back");
            System.out.println();
            try {
                int i = scanner.nextInt();
                scanner.nextLine();
                if (i == 0) {
                    return;
                }
                carsOfCompanyMenu(companies.get(i - 1));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } else {
            System.out.println("The company list is empty!");
            System.out.println();
        }
    }

    /**
     * Menu for selected company with three options to choose from
     * @param company chosen company
     */
    private void carsOfCompanyMenu(Company company) {
        int counter = 0;
        while(true) {
            System.out.println("'" + company.getName() + "' company:");
            System.out.println("""
                    1. Car list
                    2. Create a car
                    0. Back""");
            try {
                int input = scanner.nextInt();
                scanner.nextLine();

                switch (input) {
                    case 1 -> printCars(dataSource.queryCarList(company.getId()));

                    case 2 -> {
                        System.out.println("Enter the car name:");
                        String name = scanner.nextLine();
                        dataSource.addCar(name, company.getId(), ++counter);
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
     * prints cars of the chosen company
     * @param cars list of cars from database filtered by company ID
     */
    private void printCars(List<Car> cars) {
        if (!cars.isEmpty()) {
            System.out.println("Car list: ");

            for (Car car : cars) {
                System.out.println((cars.indexOf(car) + 1) + ". " + car.getName());
            }
            System.out.println();

        } else {
            System.out.println("The car list is empty!");
            System.out.println();
        }
    }
}
