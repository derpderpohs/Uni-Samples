
/*
 * Name:Seah Hao Jian
 * GUID:2228156S
 * Title: APH Exercise 2
 * This is my own work as defined in the Academic Ethics agreement I have signed..
 */

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class dependancyDiscoverer {

    static ArrayDeque<String> dirList = new ArrayDeque<>(); //store the dir list
    static Queue<workItem> files = new workQueue<>(); // store the files to find/ work queue
    static harvestHashMap dependancies = new harvestHashMap();// store the files and its dependancies
    static Thread[] threadPool;

    public static void main(String[] args) {

        LinkedList<String> order = new LinkedList<>();
        //always search current directory first
        dirList.add("./");
        // proceess the -I paramters
        for (String dirStr : args) {
            String isDir = dirStr.substring(0, 2);
            int compare = isDir.compareTo("-I");
            if (compare == 0) {
                isDir = dirStr.substring(2, dirStr.length());
                int isValid = (isDir.substring(isDir.length() - 1, isDir.length())).compareTo("/");
                if (isValid != 0) {
                    isDir += "/";
                }

                dirList.add(isDir);
                //if it is a directory parameter
                //put it in a data structure to process
            } else {
                //read the file
                int in = dirStr.indexOf(".");
                String fext = dirStr.substring(in + 1, dirStr.length());

                //check file extension
                if (!fext.equals("c") && !fext.equals("y")
                        && !fext.equals("l")) {
                    System.out.println("Illegal extension: " + dirStr
                            + " must be .c, .y or .l");
                    System.exit(1);
                }

                //add it into a array of files.
                //add the filename defined as parameter to the array
                workItem workItem = new workItem(dirStr, dirStr);
                files.add(workItem);

                //add the order to traverse
                String key = workItem.getwParent();
                order.add(key.substring(0, key.length() - 1) + "o");
            }
        }

        //set the order to print
        dependancies.setOrder(order);

        //process the CPATH and add to tail of queue
        String cpath = System.getenv("CPATH");
        if (cpath != null) {
            String[] parts = cpath.split(":");

            if (parts != null) {
                Pattern p = Pattern.compile("([A-Z|a-z]:)?(\\|\\/)?[a-zA-z]+");
                for (String s : parts) {
                    Matcher m = p.matcher(s);
                    if (m.find()) {
                        //check if the file path has a / at the end, if not append it
                        int isValid = s.substring(s.length() - 1, s.length()).compareTo("/");
                        if (isValid != 0) {
                            s += "/";
                        }
                        dirList.add(s);
                    }
                }

            }
        }

        //get num of worker threads needed
        int noOfThreads = 2;
        try {
            String n = System.getenv("CRAWLER_THREADS");
            if (n != null) {
                noOfThreads = Integer.parseInt(n);
            }

        } catch (NumberFormatException ex) {
            System.out.println("invalid input for number of threads; defaulting to 2");
        }

        threadPool = new Thread[noOfThreads];

        //setup dem worker threads
        for (int i = 0; i < noOfThreads; i++) {
            workerThread wk;
            wk = new workerThread(files, dependancies, dirList, noOfThreads);
            threadPool[i] = new Thread(wk);
        }

        //start the worker threads
        for (int i = 0; i < noOfThreads; i++) {
            threadPool[i].start();
        }

        //get all dem worker threads to join
        for (int i = 0; i < noOfThreads; i++) {
            try {
                threadPool[i].join();
            } catch (InterruptedException ex) {
            }
        }

        //print the stuff out and exit the program
        printDependancies();
        System.exit(0);

    }

    public static void printDependancies() {
        LinkedList<String> order = dependancies.getOrder();
        Iterator<String> it = order.iterator();
        while (it.hasNext()) {
            ConcurrentLinkedDeque<String> a;
            String file = it.next();
            a = dependancies.get(file);
            System.out.print(file + ":");
            if (a != null) {
                for (Object s : a) {
                    System.out.print(" " + (String) s);
                }
            }

            System.out.println("");
        }
    }
}
