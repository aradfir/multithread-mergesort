import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SortData {
    static private void shuffleArray(int[] ar) {
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

    private static void sort(int[] array, int threadCount, int itemsPerThread, boolean printResults) throws InterruptedException {
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
    private static long timeTest(int[] array, int threadCount, int itemsPerThread) throws InterruptedException {
        //clone because running test multiple times
        int[] arrayClone=array.clone();
        Date startTime=new Date();
        sort(arrayClone,threadCount,itemsPerThread,false);
        Date endTime=new Date();

        return endTime.getTime()-startTime.getTime();
    }
    static private float[][] getTimes(int minThread,int maxThread,int threadIncrements,int minItems,int maxItems,int itemsIncrements) throws InterruptedException {
        float[][] times=new float[1+(maxItems-minItems)/itemsIncrements][1+(maxThread-minThread)/threadIncrements];
        for(int i=0;i<times.length;i++){
            int itemCounts=minItems+i*itemsIncrements;
            int[] array=createRandomArray(itemCounts);
            for(int j=0;j<times[i].length;j++)
            {
                int threadCount=j*threadIncrements+minThread;
                int itemsPerThread=itemCounts/threadCount;
                times[i][j]=(float)timeTest(array,threadCount,itemsPerThread)/1000f;
                System.out.println("Threads : "+ threadCount+"\tElements count : "+itemCounts+"\t Time : "+times[i][j]);
            }
        }
        return times;
    }
    public static float[][] getData(int sampleCount, int minThread, int maxThread, int minItem, int maxItem) throws InterruptedException {

        //defining constants
        //sampleCount++;

        int itemsIncrement=(maxItem-minItem+1)/(sampleCount);
        int threadIncrements = (maxThread-minThread+1)/(sampleCount);
        return getTimes(minThread,maxThread,threadIncrements,minItem,maxItem,itemsIncrement);


    }
}
