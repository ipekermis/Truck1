import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.List;

public class AVL{
    public class TreeNode { // Inner class for tree node
        public ParkingLot element;
        public TreeNode left;
        public TreeNode right;
        public TreeNode(ParkingLot e) {
            element = e;
            left = null;
            right = null;
            height = 1;
        }
        public int height;
    }
    public TreeNode root;
    protected int size;
    public int height(TreeNode node){
        if (node == null)
            return 0;
        return node.height;
    }
    public boolean insert(ParkingLot e) {
        if (root == null) {
            root = new TreeNode(e); // Create a new root
        } else {
            root = insertAndBalance(root, e);
        }
        size++;
        return true;
    }

    // Recursive method to insert element and rebalance on the way up
    private TreeNode insertAndBalance(TreeNode node, ParkingLot e) {
        if (node == null) return new TreeNode(e);

        if (e.capcty_cons < node.element.capcty_cons) {
            node.left = insertAndBalance(node.left, e);
        } else if (e.capcty_cons > node.element.capcty_cons) {
            node.right = insertAndBalance(node.right, e);
        } else {
            return node; // Duplicate value; do not insert
        }

        // Update height and balance the node
        return rebalance(node,e);
    }
    private TreeNode rebalance(TreeNode node, ParkingLot e) {
        if (node == null)
            return node;
        node.height = 1 + Math.max(height(node.left), height(node.right)); //Update height of this ancestor node
        int balance = getBalance(node);
        //case 1 Left Left Case
        if (balance > 1 && e.capcty_cons<(node.left.element.capcty_cons) ) {
            return rightRotate(node);
        }
        //case 2 Right Right Case
        if (balance < -1 && e.capcty_cons>(node.right.element.capcty_cons)) {
            return leftRotate(node);
        }
        //case 3 Left Right Case
        if (balance > 1 && e.capcty_cons>(node.left.element.capcty_cons)) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        //case 4 Right Left Case
        if (balance < -1 && e.capcty_cons<(node.right.element.capcty_cons) ) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;


    }

    public boolean delete(ParkingLot e) {
        if (search(e.capcty_cons) == null) return false;
        root = deleteAndBalance(root, e);
        size--;
        return true;
    }

    // Recursive method to delete element and rebalance on the way up
    private TreeNode deleteAndBalance(TreeNode node, ParkingLot e) {
        if (node == null) return null;

        // Traverse the tree to find the node to delete
        if (e.capcty_cons < node.element.capcty_cons) { // If the key to be deleted is smaller
            // than the node's key, then it lies in
            // left subtree
            node.left = deleteAndBalance(node.left, e);
        } else if (e.capcty_cons > node.element.capcty_cons) {// If the key to be deleted is greater
            // than the node's key, then it lies in
            // right subtree
            node.right = deleteAndBalance(node.right, e);
        } else {


            // Node with only one child or no child
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                // Node with two children: get the in-order predecessor
                TreeNode rightMost = findMax(node.left);
                node.element = rightMost.element;
                node.left = deleteAndBalance(node.left, rightMost.element);
            }
        }

        // Update height and rebalance the node on the path up
        return rebalance(node);
    }



    // Helper method to find the maximum element in a subtree
    private TreeNode findMax(TreeNode node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    // Rebalance method updated to avoid needing the element parameter
    private TreeNode rebalance(TreeNode node) {
        if (node == null)
            return node;

        // Update height
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Check balance
        int balance = getBalance(node);

        // Perform rotations to maintain AVL properties
        if (balance > 1 && getBalance(node.left) >= 0) {  // Left Left Case
            return rightRotate(node);
        }
        if (balance < -1 && getBalance(node.right) <= 0) {  // Right Right Case
            return leftRotate(node);
        }
        if (balance > 1 && getBalance(node.left) < 0) {  // Left Right Case
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && getBalance(node.right) > 0) {  // Right Left Case
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }
    public TreeNode rightRotate(TreeNode y) {// A utility function to left rotate
        // subtree rooted with x
        TreeNode x = y.left;
        TreeNode T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = 1 + Math.max(height(y.left),
                height(y.right));
        x.height = 1 + Math.max(height(x.left),
                height(x.right));

        // Return new root
        return x;
    }
    public TreeNode leftRotate(TreeNode x) {
        TreeNode y = x.right;
        TreeNode T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        x.height = 1 + Math.max(height(x.left),
                height(x.right));
        y.height = 1 + Math.max(height(y.left),
                height(y.right));

        // Return new root
        return y;
    }
    public int getBalance(TreeNode N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }

    public ParkingLot search(int e){
        TreeNode current = root;
        while (current != null) {
            if (e<current.element.capcty_cons) { // e is smaller than current
                current = current.left;
            } else if (e>current.element.capcty_cons) { // e is bigger than current
                current = current.right; }
            else
                return current.element; // e is found
        }return null;

    }public ParkingLot findSuccessor(int x) {
        TreeNode successor = null;  // Track the smallest value larger than x
        TreeNode current = root;

        // Traverse the tree to search for x or the closest larger value
        while (current != null) {
            if (x < current.element.capcty_cons) {
                // Current node is a candidate for successor
                successor = current;
                current = current.left;
            } else {
                // Move right since we need a larger value
                current = current.right;
            }
        }

        // At this point, successor holds the smallest value greater than x, if any
        return (successor != null) ? successor.element : null;
    }public ParkingLot findInOrderPredecessor(int x) {
        TreeNode predecessor = null;  // Track the largest value smaller than x
        TreeNode current = root;

        // Traverse the tree to search for x or the closest smaller value
        while (current != null) {
            if (x > current.element.capcty_cons) {
                // Current node is a candidate for predecessor
                predecessor = current;
                current = current.right;
            } else {
                // Move left since we need a smaller value
                current = current.left;
            }
        }
        // At this point, predecessor holds the largest value less than x, if any
        return (predecessor != null) ? predecessor.element : null;
    }public ArrayList<ParkingLot> getNodesGreaterThan(int value) {
        ArrayList<ParkingLot> result = new ArrayList<>();
        findGreaterNodes(root, value, result);
        return result;
    }

    // Rekürsif olarak ağacı dolaşan yardımcı fonksiyon
    private void findGreaterNodes(TreeNode node, int value, List<ParkingLot> result) {
        if (node == null) return;

        if (node.element.capcty_cons > value) {
            result.add(node.element);
            findGreaterNodes(node.left, value, result); // Left subtree has values greater than `value`
        }

        findGreaterNodes(node.right, value, result); // All right subtree nodes will be checked as needed
    }public boolean isIn(ParkingLot e){
        TreeNode current = root;
        while (current != null) {
            if (e.capcty_cons<current.element.capcty_cons) { // e is smaller than current
                current = current.left;
            } else if (e.capcty_cons>current.element.capcty_cons) { // e is bigger than current
                current = current.right;
            }
            else
                return true; // e is found
        }
       return false; // e is not in the tree
    }









}
