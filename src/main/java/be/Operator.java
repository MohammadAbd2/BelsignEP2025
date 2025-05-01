package be;

public class Operator {
    private int id;
    private String name;

    public Operator(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Operator [id=" + id + ", name=" + name + "]";
    }
}
