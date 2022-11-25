package carsharing;

import java.util.List;
import java.util.Scanner;

public class ManagerMenu {
    Scanner scanner = new Scanner(System.in);
    DataSource dataSource;

    public ManagerMenu(DataSource dataSource) {
        this.dataSource = dataSource;
        managerMenu();
    }

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
