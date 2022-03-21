#include <stdio.h>
#include <stdlib.h>
#include "date.h"
#include <string.h>

struct date {
    int day;
    int month;
    int year;
};

Date *date_create(char *datestr) {
    Date *newdate; //the new struct to hold the date values
    int day, month, year;

    //allocate memory to newdate, frst make size then cast it the pointer to the space as a Date pointer
    newdate = malloc(sizeof (Date));

    if (newdate != NULL) {
        //if malloc fails on newdate it fails so we return NULL
        sscanf(datestr, "%d/%d/%d", &day, &month, &year);
        newdate->day = day;
        newdate->month = month;
        newdate->year = year;

    }
    //seperate the string into its compoenents based on pattern
    return newdate;
}

/*Create a for loop which iterates throught the datestr char char by char
and record chars until meeting '/' or EOF. if '/' stop, make recorded input into a
day type, and continue. if space or EOF stop and get out of the iteration.*/


Date *date_duplicate(Date *d) {
    Date *new;
    //need to use malloc
    //make one new data structure of date using malloc, instatntiate a pointer of Date
    //in C dont explicit cast, in C++ must explicit
    new = malloc(sizeof (Date));
    if (new == NULL) {
        //if the allocation of memory is failed
        free(new);
        return NULL;
    }
    //copy over all data inside d into new data structure created in this function
    //memcpy(new,d, sizeof(d)); this is a shallow copy (doesnt hold its own memory, only points to one memory)
    // assigning value by value for a deep copys(holds its own memory space)
    new->day = d->day;
    new->month = d->month;
    new->year = d->year;

    return new;
    //return address/pointer to the new date object
}

int date_compare(Date *date1, Date *date2) {

    if (date1->year < date2->year) {
        //if by year date1 is earlier than date 2 which implies that date 1 is earlier
        //return value of date1<date2
        return -1;
    } else if (date1->year > date2->year) {
        //if date1 is more recent then date2
        //return date1>date2
        return 1;
    }

    if (date1->month < date2->month) {
        //traverse the next identifier, month and compare which is smaller
        //if month of date 1 is lesser than date 2 and date1.year == date2.year
        //this implies that date1 is less recent then date 2
        //return date1<date2
        return -1;
    } else if (date1->month > date2->month) {
        //at this point, we assume that year is equal, since if its different we
        //wont run through this code here. so we compare by month to check which is
        //more recent. here date1 is more recent as e.g month1 = 12 vs month2 = 1
        //return date1>date2
        return 1;
    }

    if (date1->day < date2->day) {
        //we will only run this code if year & month is the same, since if it is 
        //the same they wont fit the other conditions.
        //in this case, date2 is more recent, so return date1<date2
        return -1;
    } else if (date1->day > date2->day) {
        // in this case as above year and month is the same, and we compare day
        //to determine which is more recent. in this case, date1 is more recent
        //so return date1>date2
        return 1;
    }
    //we checked every field, and now year, month and day is the same values.
    //thus, both are same so we return date1==date2
    return 0;
}

void date_destroy(Date *d) {

    if (d != NULL) {
        free(d);
    }
}
