#include "date.h"
#include "tldlist.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define USAGE "usage: %s begin_datestamp end_datestamp [file] ...\n"

static void process(FILE *fd, TLDList *tld) {
    char bf[1024], sbf[1024];
    Date *d;
    while (fgets(bf, sizeof (bf), fd) != NULL) {
        char *q, *p = strchr(bf, ' ');
        if (p == NULL) {
            fprintf(stderr, "Illegal input line: %s", bf);
            return;
        }
        strcpy(sbf, bf);
        *p++ = '\0';
        while (*p == ' ')
            p++;
        q = strchr(p, '\n');
        if (q == NULL) {
            fprintf(stderr, "Illegal input line: %s", sbf);
            return;
        }
        *q = '\0';
        d = date_create(bf);
        (void) tldlist_add(tld, p, d);
        date_destroy(d);
    }
}

int main(int argc, char *argv[]) {
//        char *middate = "01/11/1992";
//        Date *middledate = date_create(middate);
//        //
//        char* a = "11/11/1990";
//        Date *d = date_create(a);
//        char glasgow[] = "www.glasgow.ac.gla.uk";
//        char glasgow2[] = "www.glasgow.ac.gla.uk";
//        char* b = "12/12/1993";
//        Date *c = date_create(b);
//        //   //test for tldlist.insert
//        TLDList *testlist = tldlist_create(d, c);
//    
//        //test for adding same data on root
//        tldlist_add(testlist, glasgow, middledate);
//        tldlist_add(testlist, glasgow2, middledate);
//    
//        //test for adding some left nodes
//        char *leftdate = "11/11/1990";
//        Date *leftd = date_create(leftdate);
//        char lefts[] = "www.glasgow.ac.gla.co";
//        char lefte[] = "www.google.ac";
//        char leftx[] = "www.google.ae";
//        tldlist_add(testlist, lefts, leftd);
//        tldlist_add(testlist, lefte, leftd);
//        tldlist_add(testlist, leftx, leftd);
//        
//        date_destroy(middledate);
//        date_destroy(d);
//        date_destroy(c);
//        date_destroy(leftd);
       // TLDIterator *iter = tldlist_iter_create(testlist);
//    s   tldlist_destroy(testlist);
        //    while ((TLDNode *)iter->currentnode != NULL) {
//        TLDNode *testnode =tldlist_iter_next(iter);
//        char* host;
//        host = tldnode_tldname(testnode);
//        printf("first node is %s\n", host);
//        testnode =tldlist_iter_next(iter);
//        host =tldnode_tldname(testnode);
//        printf("second node is %s\n", host);
//        testnode = tldlist_iter_next(iter);
//        host =tldnode_tldname(testnode);
//        printf("third node is %s\n",host);
//        testnode = tldlist_iter_next(iter);
//        host = tldnode_tldname(testnode);
//        printf("last node is %s\n", host);
//        testnode = tldlist_iter_next(iter);
//        host = tldnode_tldname(testnode);
//        printf("if we iterate again it should show same result as just now: %s\n", host);
//        //    }
    
       // return 0;




    Date *begin = NULL, *end = NULL;
    int i;
    FILE *fd;
    TLDList *tld = NULL;
    TLDIterator *it = NULL;
    TLDNode *n;
    double total;

    if (argc < 3) {
        fprintf(stderr, USAGE, argv[0]);
        return -1;
    }
    begin = date_create(argv[1]);
    if (begin == NULL) {
        fprintf(stderr, "Error processing begin date: %s\n", argv[1]);
        goto error;
    }
    end = date_create(argv[2]);
    if (end == NULL) {
        fprintf(stderr, "Error processing end date: %s\n", argv[2]);
        goto error;
    }
    if (date_compare(begin, end) > 0) {
        fprintf(stderr, "%s > %s\n", argv[1], argv[2]);
        goto error;
    }
    tld = tldlist_create(begin, end);
    if (tld == NULL) {
        fprintf(stderr, "Unable to create TLD list\n");
        goto error;
    }
    if (argc == 3)
        process(stdin, tld);
    else {
        for (i = 3; i < argc; i++) {
            if (strcmp(argv[i], "-") == 0)
                fd = stdin;
            else
                fd = fopen(argv[i], "r");
            if (fd == NULL) {
                fprintf(stderr, "Unable to open %s\n", argv[i]);
                continue;
            }
            process(fd, tld);
            if (fd != stdin)
                fclose(fd);
        }
    }
    total = (double) tldlist_count(tld);
    it = tldlist_iter_create(tld);
    if (it == NULL) {
        fprintf(stderr, "Unable to create iterator\n");
        goto error;
    }
    while ((n = tldlist_iter_next(it))) {
        printf("%6.2f %s\n", 100.0 * (double) tldnode_count(n) / total, tldnode_tldname(n));
    }
    tldlist_iter_destroy(it);
    tldlist_destroy(tld);
    date_destroy(begin);
    date_destroy(end);
    return 0;
error:
    if (it != NULL) tldlist_iter_destroy(it);
    if (tld != NULL) tldlist_destroy(tld);
    if (end != NULL) date_destroy(end);
    if (begin != NULL) date_destroy(begin);
    return -1;
}
