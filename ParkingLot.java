import java.util.ArrayList;
import java.util.Comparator;

public class ParkingLot {
    public int capcty_cons;
    public SinglyLinkedList waiting;
    public SinglyLinkedList ready;
    public int limit;

    public ParkingLot(int capcty_cons, int limit) {
        this.capcty_cons = capcty_cons;
        this.limit = limit;
        waiting=new SinglyLinkedList();
        ready=new SinglyLinkedList();
    }

    @Override
    public String toString() {
        return "ParkingLot{" +
                "capcty_cons=" + capcty_cons +
                ", waiting=" + waiting +
                ", ready=" + ready +
                ", limit=" + limit +
                '}';
    }
}
