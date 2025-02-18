
public class Node {
    private Truck element;
    private Node next;
    public Node(Truck element, Node next) { this.element = element;
        this.next = next; }
    public Truck getElement() { return element; }
    public Node getNext() { return next; }
    public void setNext(Node next) { this.next = next; }
}

