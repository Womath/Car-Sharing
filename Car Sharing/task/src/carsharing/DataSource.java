package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataSource {
    public static final String DB_NAME = "carsharing";
    public static final String CONNECTION_STRING = "jdbc:h2:./src/carsharing/db/" + DB_NAME;

    //Some frequently used printable sentences
    public static final String TABLE_IS_CREATED = "Table is created.";
    public static final String COULDNT_CREATE_TABLE = "Couldn't create table: ";
    public static final String COULDNT_SHOW_QUERY = "Couldn't show query: ";

    //Table and column names
    public static final String TABLE_COMPANY = "COMPANY";
    public static final String COMPANY_ID = "ID";
    public static final String COMPANY_NAME = "NAME";
    public static final String TABLE_CAR = "CAR";
    public static final String CAR_ID = "ID";
    public static final String CAR_NAME = "NAME";
    public static final String CAR_COMPANY_ID = "COMPANY_ID";
    public static final String CAR_AVAILABLE = "AVAILABLE";
    public static final String CAR_ORDER_IN_COMPANY = "ORDER_IN_COMPANY";
    public static final String TABLE_CUSTOMER = "CUSTOMER";
    public static final String CUSTOMER_ID = "ID";
    public static final String CUSTOMER_NAME = "NAME";
    public static final String CUSTOMER_RENTED_CAR_ID = "RENTED_CAR_ID";

    //commonly used SQL keywords with spaces
    public static final String SELECT_ALL_FROM = "SELECT * FROM ";
    public static final String ORDER_BY = " ORDER BY ";
    public static final String WHERE = " WHERE ";
    public static final String INSERT_INTO = "INSERT INTO ";
    public static final String SELECT = "SELECT ";
    public static final String FROM = " FROM ";
    public static final String CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS ";
    public static final String INTEGER_PRIMARY_KEY_AUTO_INCREMENT = " INTEGER PRIMARY KEY AUTO_INCREMENT, ";

    //SQL statements
    public static final String QUERY_ALL_COMPANY = SELECT_ALL_FROM + TABLE_COMPANY +
            ORDER_BY + COMPANY_ID + " ASC";
    public static final String QUERY_ALL_CAR_BY_COMPANY_ID = SELECT_ALL_FROM + TABLE_CAR +
            WHERE + TABLE_CAR + "." + CAR_COMPANY_ID + " = ?" +
            ORDER_BY + CAR_ID + " ASC";
    public static final String ADD_COMPANY = INSERT_INTO + TABLE_COMPANY + "(" + COMPANY_NAME + ") VALUES(?)";
    public static final String QUERY_COMPANY_BY_NAME = SELECT + COMPANY_NAME + FROM + TABLE_COMPANY +
            WHERE + COMPANY_NAME + " = ?";
    public static final String ADD_CAR = INSERT_INTO + TABLE_CAR +
            "(" + CAR_NAME + ", " + CAR_COMPANY_ID + ", " + CAR_ORDER_IN_COMPANY + ") VALUES(?, ?, ?)";
    public static final String QUERY_CAR_BY_NAME = SELECT + CAR_NAME + FROM + TABLE_CAR +
            WHERE + CAR_NAME + " = ?";
    public static final String QUERY_CUSTOMER = SELECT + CUSTOMER_NAME + FROM + TABLE_CUSTOMER +
            WHERE + CUSTOMER_NAME + " = ?";
    public static final String ADD_CUSTOMER = INSERT_INTO + TABLE_CUSTOMER +
            "(" + CUSTOMER_NAME + ", " + CUSTOMER_RENTED_CAR_ID + ") VALUES(?, NULL)";
    public static final String QUERY_ALL_CUSTOMER = SELECT_ALL_FROM + TABLE_CUSTOMER +
            ORDER_BY + CUSTOMER_ID + " ASC";
    public static final String UPDATE_CUSTOMER_WITH_CAR_ID = "UPDATE " + TABLE_CUSTOMER + " SET " + CUSTOMER_RENTED_CAR_ID +
            " = ?" + WHERE + CUSTOMER_ID + " = ?";
    public static final String QUERY_CAR_BY_ID = SELECT_ALL_FROM + TABLE_CAR +
            WHERE + TABLE_CAR + "." + CAR_ID + " = ?";
    public static final String QUERY_COMPANY_BY_ID = SELECT_ALL_FROM + TABLE_COMPANY +
            WHERE + TABLE_COMPANY + "." + COMPANY_ID + " = ?";
    public static final String QUERY_CUSTOMER_BY_ID = SELECT_ALL_FROM + TABLE_CUSTOMER +
            WHERE + TABLE_CUSTOMER + "." + CUSTOMER_ID + " = ?";
    public static final String QUERY_AVAILABLE_CAR_BY_COMPANY_ID = SELECT_ALL_FROM + TABLE_CAR +
            WHERE + TABLE_CAR + "." + CAR_COMPANY_ID + " = ?" +
            " AND " + TABLE_CAR + "." + CAR_AVAILABLE + " = TRUE" +
            ORDER_BY + CAR_ID + " ASC";
    public static final String UPDATE_CAR_AVAILABLE = "UPDATE " + TABLE_CAR + " SET " + CAR_AVAILABLE +
            " = ?" + WHERE + CAR_ID + " = ?";

    //connection
    private Connection conn;

    //prepared statements
    private PreparedStatement companyQuery;
    private PreparedStatement companyAdder;
    private PreparedStatement carQuery;
    private PreparedStatement carAdder;
    private PreparedStatement carByCompanyIdQuery;
    private PreparedStatement customerQuery;
    private PreparedStatement customerAdder;
    private PreparedStatement carIdInCustomerUpdater;
    private PreparedStatement carByIdQuery;
    private PreparedStatement companyByIdQuery;
    private PreparedStatement customerByIdQuery;
    private PreparedStatement availableCarByCompanyIdQuery;
    private PreparedStatement carAvailableUpdater;


    public DataSource () {
        //it does nothing except creating an object, open method does the work
    }

    public boolean open() {
        try {
            this.conn = DriverManager.getConnection(CONNECTION_STRING);
            conn.setAutoCommit(true);
            createCompanyTable();
            createCarTable();
            createCustomerTable();
            companyAdder = conn.prepareStatement(ADD_COMPANY, Statement.RETURN_GENERATED_KEYS);
            companyQuery = conn.prepareStatement(QUERY_COMPANY_BY_NAME);
            carByCompanyIdQuery = conn.prepareStatement(QUERY_ALL_CAR_BY_COMPANY_ID);
            carAdder = conn.prepareStatement(ADD_CAR, Statement.RETURN_GENERATED_KEYS);
            carQuery = conn.prepareStatement(QUERY_CAR_BY_NAME);
            customerQuery = conn.prepareStatement(QUERY_CUSTOMER);
            customerAdder = conn.prepareStatement(ADD_CUSTOMER, Statement.RETURN_GENERATED_KEYS);
            carIdInCustomerUpdater = conn.prepareStatement(UPDATE_CUSTOMER_WITH_CAR_ID);
            carByIdQuery = conn.prepareStatement(QUERY_CAR_BY_ID);
            companyByIdQuery = conn.prepareStatement(QUERY_COMPANY_BY_ID);
            customerByIdQuery = conn.prepareStatement(QUERY_CUSTOMER_BY_ID);
            availableCarByCompanyIdQuery = conn.prepareStatement(QUERY_AVAILABLE_CAR_BY_COMPANY_ID);
            carAvailableUpdater = conn.prepareStatement(UPDATE_CAR_AVAILABLE);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public boolean close() {
        try {
            if (carAvailableUpdater != null) {
                carAvailableUpdater.close();
            }
            if (availableCarByCompanyIdQuery != null) {
                availableCarByCompanyIdQuery.close();
            }
            if (customerByIdQuery != null) {
                customerByIdQuery.close();
            }
            if (carByIdQuery != null) {
                carByIdQuery.close();
            }
            if (companyByIdQuery != null) {
                companyByIdQuery.close();
            }
            if (carIdInCustomerUpdater != null) {
                carIdInCustomerUpdater.close();
            }
            if (customerAdder != null) {
                customerAdder.close();
            }
            if (customerQuery != null) {
                customerQuery.close();
            }
            if (carQuery != null) {
                carQuery.close();
            }
            if (carAdder != null) {
                carAdder.close();
            }
            if (carByCompanyIdQuery != null) {
                carByCompanyIdQuery.close();
            }
            if (companyQuery != null) {
                companyQuery.close();
            }
            if (companyAdder != null) {
                companyAdder.close();
            }
            if (conn != null) {
                conn.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
            return false;
        }
    }

    public boolean createCompanyTable() {
        try (Statement statement = conn.createStatement()) {
            System.out.println("Crating COMPANY table...");
            String sql = CREATE_TABLE_IF_NOT_EXISTS + TABLE_COMPANY +
                    " (" + COMPANY_ID + INTEGER_PRIMARY_KEY_AUTO_INCREMENT +
                    COMPANY_NAME + " VARCHAR UNIQUE NOT NULL)";
            statement.execute(sql);
            sql = " ALTER TABLE company ALTER COLUMN id RESTART WITH 1";
            statement.execute(sql);
            System.out.println(TABLE_IS_CREATED);
            return true;
        } catch (SQLException e) {
            System.out.println(COULDNT_CREATE_TABLE + e.getMessage());
            return false;
        }
    }

    public boolean createCarTable() {
        try (Statement statement = conn.createStatement()) {
            System.out.println("Crating CAR table...");
            String sql = CREATE_TABLE_IF_NOT_EXISTS + TABLE_CAR +
                    " (" + CAR_ID + INTEGER_PRIMARY_KEY_AUTO_INCREMENT +
                    CAR_NAME + " VARCHAR UNIQUE NOT NULL, " +
                    CAR_COMPANY_ID + " INT NOT NULL, " +
                    CAR_ORDER_IN_COMPANY + " INT NOT NULL, " +
                    CAR_AVAILABLE + " BOOLEAN NOT NULL DEFAULT TRUE, " +
                    "CONSTRAINT FK_COMPANY FOREIGN KEY (" + CAR_COMPANY_ID + ") " +
                    "REFERENCES " + TABLE_COMPANY + "(" + COMPANY_ID + "))";
            statement.execute(sql);
            System.out.println(TABLE_IS_CREATED);
            return true;
        } catch (SQLException e) {
            System.out.println(COULDNT_CREATE_TABLE + e.getMessage());
            return false;
        }
    }

    public boolean createCustomerTable() {
        try (Statement statement = conn.createStatement()) {
            System.out.println("Creating CUSTOMER table...");
            String sql = CREATE_TABLE_IF_NOT_EXISTS + TABLE_CUSTOMER +
                    " (" + CUSTOMER_ID + INTEGER_PRIMARY_KEY_AUTO_INCREMENT +
                    CUSTOMER_NAME + " VARCHAR NOT NULL, " +
                    CUSTOMER_RENTED_CAR_ID + " INT, " +
                    "UNIQUE (" + CUSTOMER_NAME + "), " +
                    "CONSTRAINT FK_CAR FOREIGN KEY (" + CUSTOMER_RENTED_CAR_ID + ") " +
                    "REFERENCES " + TABLE_CAR + "(" + CAR_ID + "))";
            statement.execute(sql);
            System.out.println(TABLE_IS_CREATED);
            return true;
        } catch (SQLException e) {
            System.out.println(COULDNT_CREATE_TABLE + e.getMessage());
            return false;
        }
    }

    public List<Company> queryCompanyList() {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(QUERY_ALL_COMPANY)) {

            List<Company> companies = new ArrayList<>();
            while (results.next()) {
                Company company = new Company();
                company.setId(results.getInt(COMPANY_ID));
                company.setName(results.getString(COMPANY_NAME));
                companies.add(company);
            }

            return companies;
        } catch (SQLException e) {
            System.out.println(COULDNT_SHOW_QUERY + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Car> queryCarList(int i) {
        try {
            carByCompanyIdQuery.setInt(1, i);
            ResultSet results = carByCompanyIdQuery.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (results.next()) {
                Car car = new Car();
                car.setId(results.getInt(CAR_ID));
                car.setName(results.getString(CAR_NAME));
                car.setCompany_id(results.getInt(CAR_COMPANY_ID));
                car.setOrder_in_company(results.getInt(CAR_ORDER_IN_COMPANY));
                car.setIs_available(results.getBoolean(CAR_AVAILABLE));
                cars.add(car);
            }
            return cars;
        } catch (SQLException e) {
            System.out.println(COULDNT_SHOW_QUERY + e.getMessage());
            return Collections.emptyList();
        }
    }

    public int addCompany(String name) throws SQLException {
        companyQuery.setString(1, name);
        ResultSet results = companyQuery.executeQuery();
        if (results.next()) {
            return results.getInt(1);
        } else {
            companyAdder.setString(1, name);
            int affectedRows = companyAdder.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert company");
            }

            ResultSet generatedKeys = companyAdder.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get ID for company");
            }
        }
    }

    public int addCar(String name, int company_id, int orderInCompany) throws SQLException {
        carQuery.setString(1, name);
        ResultSet results = carQuery.executeQuery();
        if (results.next()) {
            return results.getInt(1);
        } else {
            carAdder.setString(1, name);
            carAdder.setInt(2, company_id);
            carAdder.setInt(3, orderInCompany);
            int affectedRows = carAdder.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert car");
            }

            ResultSet generatedKeys = carAdder.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get ID for car");
            }
        }
    }

    public int addCustomer(String name) throws SQLException {
        customerQuery.setString(1, name);
        ResultSet results = customerQuery.executeQuery();
        if (results.next()) {
            return results.getInt(1);
        } else {
            customerAdder.setString(1, name);
            int affectedRows = customerAdder.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert customer");
            }

            ResultSet generatedKeys = customerAdder.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get ID for customer");
            }
        }
    }

    public List<Customer> queryCustomerList() {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(QUERY_ALL_CUSTOMER)) {

            List<Customer> customers = new ArrayList<>();
            while (results.next()) {
                Customer customer = new Customer();
                customer.setId(results.getInt(CUSTOMER_ID));
                customer.setName(results.getString(CUSTOMER_NAME));
                customer.setRented_car_id(results.getInt(CUSTOMER_RENTED_CAR_ID));
                customers.add(customer);
            }

            return customers;
        } catch (SQLException e) {
            System.out.println(COULDNT_SHOW_QUERY + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void updateCustomerWithCarId(Integer car_id, int customer_id) throws SQLException {
        carIdInCustomerUpdater.setObject(1, car_id);
        carIdInCustomerUpdater.setInt(2, customer_id);
        int affectedRows = carIdInCustomerUpdater.executeUpdate();

        if (affectedRows != 1) {
            throw new SQLException("Couldn't update car ID in customer table");
        }
    }

    public Company queryCompanyById(int company_id) throws SQLException{
        companyByIdQuery.setInt(1, company_id);
        ResultSet results = companyByIdQuery.executeQuery();

        Company company = new Company();
        while(results.next()) {
            company.setId(results.getInt(COMPANY_ID));
            company.setName(results.getString(COMPANY_NAME));
        }

        return company;
    }

    public Car queryCarById(int car_id) throws SQLException{
        carByIdQuery.setInt(1, car_id);
        ResultSet results = carByIdQuery.executeQuery();

        Car car = new Car();
        while (results.next()) {
            car.setId(results.getInt(CAR_ID));
            car.setName(results.getString(CAR_NAME));
            car.setCompany_id(results.getInt(CAR_COMPANY_ID));
            car.setOrder_in_company(results.getInt(CAR_ORDER_IN_COMPANY));
            car.setIs_available(results.getBoolean(CAR_AVAILABLE));
        }

        return car;
    }

    public Customer queryCustomerById(int customer_id) throws SQLException{
        customerByIdQuery.setInt(1, customer_id);
        ResultSet results = customerByIdQuery.executeQuery();

        Customer customer = new Customer();
        while (results.next()) {
            customer.setId(results.getInt(CUSTOMER_ID));
            customer.setName(results.getString(CUSTOMER_NAME));
            customer.setRented_car_id(results.getObject(CUSTOMER_RENTED_CAR_ID, Integer.class));
        }

        return customer;
    }

    public List<Car> queryAvailableCarList(int i) {
        try {
            availableCarByCompanyIdQuery.setInt(1, i);
            ResultSet results = availableCarByCompanyIdQuery.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (results.next()) {
                Car car = new Car();
                car.setId(results.getInt(CAR_ID));
                car.setName(results.getString(CAR_NAME));
                car.setCompany_id(results.getInt(CAR_COMPANY_ID));
                car.setOrder_in_company(results.getInt(CAR_ORDER_IN_COMPANY));
                car.setIs_available(results.getBoolean(CAR_AVAILABLE));
                cars.add(car);
            }
            return cars;
        } catch (SQLException e) {
            System.out.println(COULDNT_SHOW_QUERY + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void updateCarAvailable(boolean is_available, int car_id) throws SQLException {
        carAvailableUpdater.setBoolean(1, is_available);
        carAvailableUpdater.setInt(2, car_id);
        int affectedRows = carAvailableUpdater.executeUpdate();

        if (affectedRows != 1) {
            throw new SQLException("Couldn't update car ID in customer table");
        }

    }
}
