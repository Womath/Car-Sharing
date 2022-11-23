package carsharing;

public class Car {
    private int id;
    private String name;
    private int company_id;
    private int order_in_company;
    private boolean is_available;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getOrder_in_company() {
        return order_in_company;
    }

    public void setOrder_in_company(int order_in_company) {
        this.order_in_company = order_in_company;
    }

    public boolean isIs_available() {
        return is_available;
    }

    public void setIs_available(boolean is_available) {
        this.is_available = is_available;
    }
}
