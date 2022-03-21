#include "tldlist.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

struct tldlist {
    // a List Containing TLDNodes, in a binary tree format?
    //maybe store in array or linkedlist
    Date *begindate;
    Date *enddate;
    TLDNode *rootnode;
    int noofTLD;
    long totaladds;
};

struct tldnode {
    // consists of Date struct, parent, left, right , and height of the node in AVL
    //    Date *nodedate;
    //    int heightofnode;
    TLDNode *left;
    TLDNode *right;
    TLDNode *parent; // these represent the left,right and parent of the node.
    char* hostname; // the hostname the node represents
    int nooftimesadded; // this is to increment when a same domain node is added
};

struct tlditerator {
    //consists of the list to iterate over
    TLDList *list;
    TLDNode *currentnode; //this variable is to track the current node we are iterating from
    int isTraverse; // this is to track if we are already iterating the tldlist
};

static void delete_child_nodes(TLDNode*);
static int insert_new_entry(TLDNode*, char*);
static TLDNode *minoftree(TLDNode*);
//static TLDNode *nextnodeinorder(TLDNode*);

TLDList *tldlist_create(Date *begin, Date *end) {

    //have to open the file to be inserted by File IO
    //check each file line for the date, and compare that it is <= end and >= date begin 
    //if it is correct make a TLD Node, which is stored in a TLDList and returned
    //is TLDList a binary search tree already?

    TLDList *list = (TLDList *) malloc(sizeof (TLDList));
    if (list != NULL) {
        list->begindate = begin;
        list->enddate = end;
        list->noofTLD = 0;
        list->rootnode = NULL;
        list->totaladds = 0;
        list->noofTLD = 0;
    } else {
        free(list);
        return NULL;
    }

    return list;

    /*
     * tldlist_create generates a list structure for storing counts against
     * top level domains (TLDs)
     *
     * creates a TLDList that is constrained to the `begin' and `end' Date's
     * returns a pointer to the list if successful, NULL if not
     */
}

void tldlist_destroy(TLDList *tld) {
    // if there is a rootnode then we proceed
    if (tld != NULL) {
        //free(tld->begindate);
        //free(tld->enddate);
        //free(tld->noofTLD);
        //free(tld->totaladds);

        if (tld->rootnode != NULL) {
            delete_child_nodes(tld->rootnode);
        }
        //after deleting all the items in the list we also deallocated the TLDList 
        free(tld);
        //have to destroy each item in the tldList, by iterating one by one using free()
        //after that destroy the list as well.

    }

    /*
     * tldlist_destroy destroys the list structure in `tld'
     *
     * all heap allocated storage associated with the list is returned to the heap
     */
}

int tldlist_add(TLDList *tld, char *hostname, Date *d) {
    int result;
    unsigned int i = 0;

    //spilt the hostname to the last domain name
    hostname = strrchr(hostname, '.') + 1;

    //convert to lower case
    for (i = 0; i < strlen(hostname); i++) {
        hostname[i] = tolower((char) hostname[i]);
    }

    char *domainname = (char *) malloc(sizeof (hostname));

    if (domainname == NULL) {
        free(domainname);
        return 0;
    }

    strcpy(domainname, hostname);

    //get comparison results
    int comparebegin = date_compare(d, tld->begindate);
    int compareend = date_compare(d, tld->enddate);

    if ((comparebegin == 1 && compareend == -1) || comparebegin == 0 || compareend == 0) {
        //if date is in between the dates  OR date is same as begin date OR 
        //date is same as end Date

        //if the list is empty
        if (tld->rootnode == NULL) {
            //adding new root node
            TLDNode *newnode = (TLDNode *) malloc(sizeof (TLDNode));

            if (newnode != NULL) {
                newnode->hostname = domainname;
                newnode->nooftimesadded = 1;
                newnode->left = NULL;
                newnode->right = NULL;
                newnode->parent = NULL;
                tld->rootnode = newnode;
                result = 1;
            } else {
                free(domainname);
                return 0;
            }

        } else {

            //if its not empty we add to the rootnode
            result = insert_new_entry(tld->rootnode, domainname);
            free(domainname);
        }
    }

    if (result == 1) {
        //if succesful add
        tld->totaladds++;
        tld->noofTLD++;
        //increase number of adds
    } else if (result == 0) {
        //if we increment count in one node only

        tld->totaladds++;
    }
    //free(domainname);
    return 0;
}
//}


//if so, add a entry of tldnode with char*hostname into the list

/*
 * tldlist_add adds the TLD contained in `hostname' to the tldlist if
 * `d' falls in the begin and end dates associated with the list;
 * returns 1 if the entry was counted, 0 if not
 */

long tldlist_count(TLDList *tld) {

    return (long) tld->totaladds;
    /*
     * tldlist_count returns the number of successful tldlist_add() calls since
     * the creation of the TLDList
     */
}

TLDIterator *tldlist_iter_create(TLDList *tld) {

    TLDIterator *iter_tld = malloc(sizeof (TLDIterator));
    TLDNode *currnode = tld->rootnode;
    if (iter_tld != NULL) {
        //if malloc correct, set the variables
        iter_tld->list = tld;
        iter_tld->currentnode = currnode;
        iter_tld->isTraverse = 0;
        return iter_tld;
    } else {

        return NULL;
    }
    /*
     * tldlist_iter_create creates an iterator over the TLDList; returns a pointer
     * to the iterator if successful, NULL if not
     */

}

TLDNode *tldlist_iter_next(TLDIterator *iter) {

    if (iter->isTraverse == 0) {
        //if we just started traversing
        iter->isTraverse = 1;

        return ( iter->currentnode = minoftree(iter->currentnode));
        //get the lowest node and start traverse from there

    }

    // step 1 of the traversal  algorithm 
    if (iter->currentnode->right != NULL) {
        return (iter->currentnode = minoftree((TLDNode *) iter->currentnode->right));
    }

    // step 2 of the traversal algorithm
    TLDNode *currentnode = iter->currentnode;
    TLDNode *parentofroot = currentnode ->parent;
    //set a variable to hold parent of root
    while (parentofroot != NULL &&
            iter->currentnode == parentofroot->right) {

        //        if () {
        //while parentofroot is not null and if the current node is the right child
        //set currentnode of the iteration to parent of root
        //this setting sets to the child right node, it doesnt work!
        iter->currentnode = parentofroot;
        parentofroot = parentofroot->parent;
        //set the currentnode of the iteration to what we just return to search from there 
        //        } 
        //            else {
        //            //if its the final node and it is the root node(contains something AND parent is null and there is no right nodes)
        //            return NULL;
        //        }
    }
    iter->currentnode = parentofroot;
    return parentofroot;

    /*
     * tldlist_iter_next returns the next element in the list; returns a pointer
     * to the TLDNode if successful, NULL if no more elements to return
     */

}

void tldlist_iter_destroy(TLDIterator *iter) {
    if (iter != NULL) {
        free(iter);
    }

}

char *tldnode_tldname(TLDNode *node) {
    if (node != NULL) {

        return node->hostname;
    }
    return "";
    /*
     * tldnode_tldname returns the tld associated with the TLDNode
     */

}

long tldnode_count(TLDNode *node) {

    //    if (node != NULL) {
    return node->nooftimesadded;
    //    }
    //return 0;
    /*
     * tldnode_count returns the number of times that a log entry for the
     * corresponding tld was added to the list
     */

}

void delete_child_nodes(TLDNode *node) {
    //delete the left and right node recursively, think about it backways from 
    //the bottom.
    //the if case is in case current node is a leaf node, we dont want to 
    // deallocate something that doesnt exist do we? Might cause an error.
    if (node != NULL) {
        delete_child_nodes(node->left);
        delete_child_nodes(node->right);
        free(node->hostname);
        free(node);
    }

    //after freeing all nodes, free the items in node:
    //leaf nodes will free first, then branch nodes
}

static int insert_new_entry(TLDNode *root, char *domainname) {
    //this function adds a node if the hostname doesnt exist, otherwise
    //it goes a level down more to search for a place to add itself
    //this function deals with one node per instance
    int comparison;
    //TLDNode *left = root->left;
    //TLDNode *right = root->right;
    //compare the hostname with the current tldnode hostname
    comparison = strcmp(domainname, root->hostname);
    if (comparison == 0) {
        //if currentnode is same value aka same hostname
        //we add 1 to the count
        root->nooftimesadded++;
        return 0;
    }
    if (comparison < 0) {
        //if hostname to be added is lesser than currentnode's hostname we
        //go left

        if (root->left != NULL) {
            //if the left node we want to add is not null
            //we go next node and check again.
            return insert_new_entry(root->left, domainname);
        } else {
            //if the left node is empty
            //make a new node and add the association with the parent
            TLDNode *newnode = malloc(sizeof (TLDNode));
            char* domain = malloc(1 + sizeof (domainname));
            //int* timesadded = malloc(sizeof (int));
            //memcpy(domain, domainname, sizeof (domainname));
            strcpy(domain, domainname);
            //domain = domainname;
            newnode->hostname = domain;
            newnode->parent = root;
            newnode->left = NULL;
            newnode->right= NULL;
            newnode->nooftimesadded = 1;
            root->left = newnode;
            return 1;

        }
    }
    if (comparison > 0) {
        //if host name to be added is more than current hostname we go right

        if (root->right != NULL) {
            //if the right node we want to add is not null
            //we go next node and check again.
            return insert_new_entry(root->right, domainname);
        } else {
            //if the right node is empty
            //make a new node and add the association with the parent
            TLDNode *newnode = malloc(sizeof (TLDNode));
            char* domain = malloc(1 + sizeof (domainname));
            //int* timesadded = malloc(sizeof (int));
            //memcpy(domain, domainname, sizeof (domainname));;
            strcpy(domain, domainname);
            newnode->hostname = domain;
            newnode->parent = root;
            newnode->left = NULL;
            newnode->right = NULL;
            root->right = newnode;
            newnode->nooftimesadded = 1;

            return 1;
        }
    }
    return -1;
}

//static TLDNode *nextnodeinorder(TLDNode *root) {
//    // step 1 of the  algorithm 
//    if (root->right != NULL) {
//        //if right child has a value, 
//        return minoftree(root->right);
//    }
//    // step 2 of the  algorithm
//    TLDNode *parentofroot = root->parent;
//    while (parentofroot != NULL && root == parentofroot) {
//
//        root = parentofroot;
//        parentofroot = parentofroot->parent;
//    }
//    return parentofroot;
//}

static TLDNode *minoftree(TLDNode *root) {
    TLDNode* current = root;

    /* loop down to find the leftmost leaf */
    while (current->left != NULL) {
        current = current->left;
    }
    return current;
}
//#endif /* _TLDLIST_H_INCLUDED_ */
