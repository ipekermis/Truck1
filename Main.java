import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    public static void main(String[] args)throws FileNotFoundException {
        

        AVL parks = new AVL();
        AVL notfull=new AVL();//AVL tree for parking lots which is not full
        AVL redy=new AVL();//AVL tree for parking lots which has at least one truck in its waiting section
        AVL forready=new AVL();//AVL tree for trucks which has  at least one truck in its ready section




        File inFile = new File(args[0]);
        File outFile = new File(args[1]);
        PrintStream outstream;
        try {
            outstream = new PrintStream(outFile);

        }catch(FileNotFoundException e2) {
            e2.printStackTrace();
            return;
        }
        Scanner reader;
        try {
            reader = new Scanner(inFile);
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find input file");
            return;
        }while (reader.hasNextLine()) {
            String line = reader.nextLine();
            String[] parts = line.split(" ");

            if(parts[0].equals("create_parking_lot")){

                create_parking_lot(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),parks,notfull);


            }else if (parts[0].equals("delete_parking_lot")){
                delete_parking_lot(Integer.parseInt(parts[1]),parks,notfull,redy,forready);

            }else if (parts[0].equals("add_truck")){
                int answ=add_truck(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),parks,notfull,redy);
                outstream.println(answ);
            }
            else if (parts[0].equals("ready")){
                String answ=ready(Integer.parseInt(parts[1]),parks,redy,forready);
                outstream.println(answ);


            } else if (parts[0].equals("load")) {
                String answ=load(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),parks,notfull,redy,forready);
                outstream.println(answ);
            }  else if (parts[0].equals("count")) {
                int ans=count(Integer.parseInt(parts[1]),parks);
                outstream.println(ans);
            }
        }reader.close();
        outstream.close();



        




    }
    public static int add_truck(int truck_id, int capacity,AVL parks,AVL notfull,AVL redy) {

        ParkingLot n=notfull.search(capacity);//searching for given capacity parking lot

        if(n==null){//if there is no parking lot with the specified capacity constraint
            // then look for largest parking lot with a capacity smaller than its own

            n=notfull.findInOrderPredecessor(capacity);
            if(n==null)
                return -1;//if largest parking lot with a capacity smaller than its own also not found then return -1
        }
            n.waiting.addLast(new Truck(truck_id,capacity));

            redy.insert(n);//insert this parking lot in redy since now it has at least one truck in its waiting section

            if(n.waiting.size()+n.ready.size()==n.limit){
                notfull.delete(n);//after insertion if it is full remove this parking lot from this tree
            }


        return n.capcty_cons;

    }
    public static void  create_parking_lot (int capacity_constraint, int truck_limit,AVL parks,AVL notfull){
        ParkingLot n=parks.search(capacity_constraint);//searching for given capacity parking lot
        if(n==null)//if given capacity parking lot is not exist then create it by inserting parking lot to AVL tree
        parks.insert(new ParkingLot(capacity_constraint,truck_limit));
        n=parks.search(capacity_constraint);
        notfull.insert(n);// at first all parking lots are empty so they have free space


    }
    public static void delete_parking_lot(int capacity_constraint, AVL parks,AVL notfull,AVL redy,AVL forready) {
        ParkingLot n = parks.search(capacity_constraint);//searching for given capacity parking lot

        if (n != null) {
            // Clear the 'ready' list by removing and unload each truck
            while (!n.ready.isEmpty()) {
                Truck truck = n.ready.removeFirst(); // Remove the first truck
                truck.loaded=0; // Unload the truck
            }

            // Clear the 'waiting' list by removing and  unload each truck
            while (!n.waiting.isEmpty()) {
                Truck truck = n.waiting.removeFirst(); // Remove the first truck
                truck.loaded = 0; // Unload the truck
            }

            // Remove the parking lot from the AVL trees
            parks.delete(n);
            redy.delete(n);
            notfull.delete(n);
            forready.delete(n);

        }
    }
    public static String ready(int capacity, AVL parks,AVL redy,AVL forready) {

        ParkingLot r=redy.search(capacity);//looking only for parking lots which has at least one truck in itss waiting section



        // If no exact match is found, find the smallest parking lot with a capacity greater than the given value
        if(r==null){//if there is no parking lot with the specified capacity constraint
            // then look for smallest parking lot with a capacity greater than its own
            r=redy.findSuccessor(capacity);
            if(r==null)
                return "-1";//if smallest parking lot with a capacity greater than its own also not found then return -1


        }
        Truck n=r.waiting.removeFirst();
        if(r.waiting.isEmpty())
            redy.delete(r);
        //if found a suitable parking lot add the earliest truck in the waiting section
        // into to the end of the ready section
        r.ready.addLast(n);
        forready.insert(r);//now this parking lot has a truck in its ready section

        return n.ID+" " +r.capcty_cons;//remove truck from waiting section and return
        // its ID and parking lot's capacity constraint




    }
    public static int count(int capacity,AVL parks) {
        ArrayList<ParkingLot> result = parks.getNodesGreaterThan(capacity);//creating a arraylist which keeps the parking
        // lots whose capacity constraint is greater than given capacity
        int c=0;
        if(result==null){
            return -1;//if there is no such a parking lot exist return -1
        }
        for(ParkingLot n:result){//iterating through result arraylist and count the number of trucks in current parking lot

            c=c+n.ready.size()+n.waiting.size();


        }

        return c;


    }
    public static String load (int capacity,int load_amount,AVL parks,AVL notfull,AVL redy,AVL forready) {
        StringBuilder st=new StringBuilder();

        int total = 0;
        ParkingLot n=forready.search(capacity);//searching for given capacity parking lot which has trucks in ready section
        if(n==null){
            n=forready.findSuccessor(capacity);//if there is no parking lot with the specified capacity constraint
            // then look for smallest parking lot with a capacity greater than its own
            if(n==null) {
                return "-1";//if smallest parking lot with a capacity greater than its own also not found then return -1
            }
        }
        int newcap = n.capcty_cons;//changing the capacity to current parking lots capacity
        Node tmp = n.ready.head;


        while ((total < load_amount)) {//checking for if there is still remaining load
            if(!forready.isIn(n)){//if ready section is empty find next available parking lot
                n = forready.findSuccessor(n.capcty_cons);
                if (n == null){
                    st.delete(st.length() - 3, st.length());
                    return st.toString();//if there is no suitable parking lot then return all the trucks loaded so far
                }
                newcap = n.capcty_cons;
                tmp = n.ready.head;//updating the values

            }
            if (load_amount-total < newcap) {//checking for partial loading
                tmp.getElement().loaded=tmp.getElement().loaded+load_amount-total;//truck can only be loaded what is leftover
                total =load_amount;//total amount of loads will reach the given amount
                n.ready.removeFirst();//remove the loaded truck from the parking lots ready section
                if(n.ready.isEmpty())
                    forready.delete(n);// if there is no truck in ready section then delete this lot from forready tree
                if(n.waiting.size()+n.ready.size()<n.limit){
                    notfull.insert(n);//after removing insert this lot into the not full tree

                }

                ParkingLot ne=notfull.search(tmp.getElement().max_capacity-tmp.getElement().loaded);// it will move to the parking lot with remaining capacity.

                if(ne==null){//if cant found the specified amount
                    ne=notfull.findInOrderPredecessor(tmp.getElement().max_capacity-tmp.getElement().loaded);//searching for largest parking lot
                    // with a capacity smaller than its own

                    if(ne==null) {//if not exist then write -1 for that truck because it cant be replaced
                        st.append(tmp.getElement().ID);
                        st.append(" ");
                        st.append(-1).append(" - ");;
                    }

                }if(ne!=null) {//if we found the suitable parking lot

                    ne.waiting.addLast(tmp.getElement());//then add truck to this parking lot's waiting section
                    st.append(tmp.getElement().ID);
                    st.append(" ");
                    st.append(ne.capcty_cons).append(" - ");
                    redy.insert(ne);//now it has a truck in its waiting section

                    if(ne.waiting.size()+ne.ready.size()==ne.limit){//after adding truck check if the limit is reached or not
                        notfull.delete(ne); //if it is full now then remove it from notfull tree
                    }



                }st.delete(st.length() - 3, st.length());
                return st.toString();


            }else  {// if load_amount-total >=newcap
                total = total +newcap;//trucks will be loaded by capacity amount of this parking lot
                n.ready.removeFirst();//remove the loaded truck
                if(n.ready.isEmpty())
                    forready.delete(n);//after removing check if the ready section is empty or not
                if(n.waiting.size()+n.ready.size()<n.limit&&(!notfull.isIn(n))){
                    notfull.insert(n);//after removing it is not full now

                }
                if(newcap==tmp.getElement().max_capacity - tmp.getElement().loaded){//if truck is full after loading unload
                    //all of its load
                    tmp.getElement().loaded=0;

                }else{
                    tmp.getElement().loaded=tmp.getElement().loaded+newcap;//if truck has still some place then uğdate it load
                }

                ParkingLot ne=notfull.search(tmp.getElement().max_capacity-tmp.getElement().loaded);//searching for suitable lot for loaded truck
                //which is not full

                if(ne==null){
                    ne=notfull.findInOrderPredecessor(tmp.getElement().max_capacity-tmp.getElement().loaded);//searching for largest parking lot
                    // with a capacity smaller than its own

                    if(ne==null){//if not found truck can't be replaced
                        st.append(tmp.getElement().ID);
                        st.append(" ");
                        st.append(-1).append(" - ");;

                    }
                }if(ne!=null) {//if we found the suitable parking lot

                    ne.waiting.addLast(tmp.getElement());//then add truck to this parking lot's waiting section
                    st.append(tmp.getElement().ID);
                    st.append(" ");
                    st.append(ne.capcty_cons).append(" - ");
                    redy.insert(ne);// now its waiting section is not empty

                    if(ne.waiting.size()+ne.ready.size()==ne.limit){
                        notfull.delete(ne);//after adding check for now it is full or not
                    }



                }
                tmp = tmp.getNext();//look for the next truck node

            }

        }st.delete(st.length() - 3, st.length());
        return st.toString();







    }

}