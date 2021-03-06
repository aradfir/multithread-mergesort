package com.company;


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    static void shuffleArray(int[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    private static int[] createRandomArray(int itemCount) {
        int[] array = new int[itemCount];
        for (int i = 0; i < itemCount; i++) {
            array[i] = i;
        }

        shuffleArray(array);
        return array;
    }

    public static void sort(int[] array, int threadCount, int itemsPerThread, boolean printResults) throws InterruptedException {
        //make an array of threads to sort each segment of the array
        SingleThreadedMergeSort[] sortThreads = new SingleThreadedMergeSort[threadCount];
        for (int i = 0; i < threadCount; i++) {
            sortThreads[i] = new SingleThreadedMergeSort(Arrays.copyOfRange(array, i * itemsPerThread, (i + 1) * itemsPerThread));
            sortThreads[i].start();
        }
        //wait for all single pieces to be sorted
        for (SingleThreadedMergeSort sortThread : sortThreads)
            sortThread.join();

        ArrayList<int[]> sortedSegments = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            sortedSegments.add(sortThreads[i].getList());
        }
        ArrayList<MergeThread> mergeThreads = new ArrayList<>();
        int activeMergeThreads = 0;
        //ends when no more merges are running, and the whole array has been merged into one large array
        while (activeMergeThreads != 0 || sortedSegments.size() != 1) {

            //start as many merges as possible
            while (sortedSegments.size() >= 2) {
                MergeThread tmp = new MergeThread(sortedSegments.get(0), sortedSegments.get(1));
                sortedSegments.remove(0);
                sortedSegments.remove(0);
                tmp.start();
                mergeThreads.add(tmp);
                activeMergeThreads++;
            }

            //check for finished merges
            Iterator<MergeThread> it = mergeThreads.iterator();
            while (it.hasNext()) {
                MergeThread merger = it.next();
                if (merger.getState() == Thread.State.TERMINATED) {

                    sortedSegments.add(merger.getRes());
                    it.remove();

                    activeMergeThreads--;
                }
            }
        }
        if (printResults)
            System.out.println(Arrays.toString(sortedSegments.get(0)));
    }
    //sort and print the sorted array
    private static void sortAndPrint(int[] array, int threadCount, int itemsPerThread) throws InterruptedException {
        sort(array, threadCount, itemsPerThread, true);
    }
    //sort and print how long it took
    private static void timeTest(int[] array, int threadCount, int itemsPerThread) throws InterruptedException {
        //clone because running test multiple times
        int[] arrayClone=array.clone();
        Date startTime=new Date();
        sort(arrayClone,threadCount,itemsPerThread,false);
        Date endTime=new Date();
        System.out.println(endTime.getTime()-startTime.getTime());
    }

    public static void main(String[] args) throws InterruptedException {

        //defining constants
        int itemCount = 100000000;
        int threadCount = 1000;
        int itemsPerThread = itemCount / threadCount;

        //make an array from 0 to itemCount-1, randomly shuffled
        int[] array = createRandomArray(itemCount);
        timeTest(array, threadCount, itemsPerThread);

    }
}
