/*
 * Name:Seah Hao Jian
 * GUID:2228156S
 * Title: APH Exercise 2
 * This is my own work as defined in the Academic Ethics agreement I have signed..
 */


import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;


public class harvestHashMap extends ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> {

    private LinkedList<String> inOrder = new LinkedList<>();
    private static final long serialVersionUID = 0;

    public synchronized void putD(String key, ConcurrentLinkedDeque<String> value) {
        //get the arrraydeque of dependancies, if null return a put with value value of the parameter
        //else get, then add the value passed into this method to the arraydeque existing THEN do a put.
        if (value == null) {
            //if the value is null we dont need to update anything
            return;
        }

        ConcurrentLinkedDeque<String> current = this.get(key);
        ConcurrentLinkedDeque<String> newA = value;
        if (current != null) {
            //add the value which is inputted to this method to the current deque

            for (Object s : newA) {
                String newData = (String) s;
                if (!current.contains(newData)) {
                    current.add(newData);
                }

            }

        }
        this.put(key, newA);

    }

    public LinkedList<String> getOrder() {
        return inOrder;
    }

    public void setOrder(LinkedList<String> nl) {
        inOrder = nl;
    }
}
