package carsharing;

public class Customer {
    private int id;
    private String name;
    private Integer rented_car_id = null;

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

    public Integer getRented_car_id() {
        return rented_car_id;
    }

    public void setRented_car_id(Integer rented_car_id) {
        this.rented_car_id = rented_car_id;
    }
}
