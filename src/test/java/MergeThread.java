//classic merge algorithm
public class MergeThread extends Thread{
    private int[] firstHalf;
    private int[] secondHalf;
    private int res[];

    public int[] getRes() {
        return res;
    }

    public MergeThread(int[] firstHalf, int[] secondHalf) {
        this.firstHalf = firstHalf;
        this.secondHalf = secondHalf;
        res=new int[firstHalf.length+secondHalf.length];
    }

    @Override
    public void run() {
        merge(res,firstHalf,secondHalf);
    }

    //classic merge algorithm
    public void merge(int[] res, int[] firstHalf, int[] secondHalf)
    {
        int m = 0;
        int n = 0;
        //merges arrays until one of them reaches the end
        while (m < firstHalf.length && n < secondHalf.length) {
            if (secondHalf[n] < firstHalf[m]) {
                res[m+n]= secondHalf[n];
                n++;
            } else {
                res[m+n]= firstHalf[m];
                m++;
            }
        }
        //only one of the next two loops will run

        //finishes first array
        while (m < firstHalf.length) {
            res[m+n]= firstHalf[m];
            m++;
        }
        //finishes second array
        while (n < secondHalf.length) {
            res[m+n]= secondHalf[n];
            n++;
        }
    }
}