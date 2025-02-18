public class SinglyLinkedList {
    public Node head = null;
    public Node tail = null;
    private int size = 0;
    public SinglyLinkedList() { }
    public int size() {

        return size;
    }
    public boolean isEmpty() {

        return size == 0;
    }
    public Truck first() {
        if (isEmpty())
            return null;
        return head.getElement();}
    public Truck last() {
        if (isEmpty())
            return null;
        return tail.getElement();

    }
    public void clear() {
        head = null; // Remove reference to head node
        tail = null; // Remove reference to tail node
        size = 0;    // Reset the size of the list to 0
    }

    public void addLast(Truck element) {
        Node newest = new Node( element,null);
        if (isEmpty())
            head = newest; // special case: previously empty list
        else
            tail.setNext(newest); // new node after existing tail
        tail=newest; //new node becomes the tail
        size++;
    }
    public Truck removeFirst() {
        if (isEmpty())
            return null;// nothing to remove
        Truck answer = head.getElement();
        head = head.getNext();
        size--;
        if (size == 0)
            tail = null;
        return answer;
    }


}
