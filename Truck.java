public class Truck {
    public int ID;
    public int max_capacity;
    public int loaded=0;

    public Truck(int ID) {
        this.ID = ID;
    }
    public void removeTruck() {
        this.ID = 0;            // or another default value, like -1 if 0 is valid
        this.max_capacity = 0;
        this.loaded = 0;
    }

    public Truck(int ID, int max_capacity) {
        this.ID = ID;
        this.max_capacity = max_capacity;
    }

    public Truck() {
    }
}
