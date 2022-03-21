
/*
 * Name:Seah Hao Jian
 * GUID:2228156S
 * Title: APH Exercise 2
 * This is my own work as defined in the Academic Ethics agreement I have signed..
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class workerThread implements Runnable {

    private final workQueue<workItem> q;
    private final harvestHashMap h;
    private final ArrayDeque<String> a;

    //constructo
    public workerThread(Queue<workItem> queue, harvestHashMap hash, ArrayDeque<String> dirList, int nt) {
        q = (workQueue<workItem>) queue;
        h = hash;
        a = dirList;
        q.setTotalThreads(nt);
    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            try {
                workItem p;
                p = q.getWork();
                if (p != null) {
                    openFile(p);
                }
            } catch (InterruptedException ex) {
                return;
            }
        }
    }

    //this method tries to open the file in the folder, and reads it for include files.
    private void openFile(workItem filename) {
        File fd;
        FileReader reader;
        //add the file into the depandacies list
        for (Object s : a) {
            fd = new File((String) s, filename.getwValue());
            if (fd.exists()) {
                try {
                    reader = new FileReader(fd);
                    Scanner in = new Scanner(reader);
                    String name = filename.getwParent();
                    name = name.substring(0, name.length() - 1) + "o";
                    workItem w = new workItem(name, filename.getwValue());
                    process(in, w);
                } catch (FileNotFoundException ex) {
                    //must return and check the next one
                    return;
                }
            }
        }

    }

    //this method reads input from the file, gets the include statements
    //it finds the include files in the file, adds those files to a dependancies
    //data structure, and add those files into the workqueue of files
    private void process(Scanner in, workItem currentFile) {
        ConcurrentLinkedDeque<String> listOfDependancies = h.get(currentFile.getwParent());
        //try to get the value inside the hashmap, if its not available append the key(representing the file to which it belongs to) to the empty list.
        //need to mod so that if 1) there is no dependancies for the file add itself(if key exists, means got, else not) 2) add to the current arraydeque in the hashmap, through synchronized
        //method addWork() which will get and append then call put.
        if (listOfDependancies == null) {
            listOfDependancies = new ConcurrentLinkedDeque<>();
            //add itself to the dependancy list
            listOfDependancies.add(currentFile.getwValue());
        }
        while (in.hasNextLine()) {
            String dependancy = in.nextLine();
            dependancy = dependancy.replace(" ", "");
            int i =dependancy.indexOf("//");
            if (i != -1) {
              dependancy = dependancy.substring(0, i);
                   
            }
            Pattern p = Pattern.compile("#include\"((.)*)\"");
            Matcher m = p.matcher(dependancy);
            if (m.find()) {
                if (!listOfDependancies.contains(m.group(1))) {
                    listOfDependancies.add(m.group(1));
                }

                //add to dependancies
                workItem w = new workItem(currentFile.getwParent(), m.group(1));
                if (!q.contains(w)) {
                    q.addWork(w);
                }
                //add to workqueue files
            }
        }
        h.putD(currentFile.getwParent(), listOfDependancies);
    }

}
