package com.company;

import java.util.*;

public class SingleThreadedMergeSort extends Thread {
    private int[] list;

    public SingleThreadedMergeSort(int[] inputList) {
        list = inputList;
    }

    public  int[] getList() {
        return list;
    }

    @Override
    public void run() {
        mergeSort(list);
    }

    public void mergeSort(int[] list)
    {
        int[] firstHalf=Arrays.copyOfRange(list,0,list.length/2).clone();
        int[] secondHalf=Arrays.copyOfRange(list,list.length/2,list.length).clone();
        if (firstHalf.length > 1)
            mergeSort(firstHalf);
        if (secondHalf.length > 1)
            mergeSort(secondHalf);
        merge(list,firstHalf,secondHalf);

    }
    public void merge(int[] res,int[] firstHalf,int[] secondHalf)
    {
        //for comments read MergeThread

        int m = 0;
        int n = 0;
        while (m < firstHalf.length && n < secondHalf.length) {
            if (secondHalf[n] < firstHalf[m]) {
                res[m+n]= secondHalf[n];
                n++;
            } else {
                res[m+n]= firstHalf[m];
                m++;
            }
        }
        while (m < firstHalf.length) {
            res[m+n]= firstHalf[m];
            m++;
        }
        while (n < secondHalf.length) {
            res[m+n]= secondHalf[n];
            n++;
        }
    }


}
