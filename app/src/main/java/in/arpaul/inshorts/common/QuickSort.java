package in.arpaul.inshorts.common;

import java.util.ArrayList;

import in.arpaul.inshorts.dataobjects.NewsDO;

/**
 * Created by aritrapal on 12/09/17.
 */

public class QuickSort {

    private ArrayList<NewsDO> arrNews;

    public void sort(ArrayList<NewsDO> arrNews) {
        this.arrNews = arrNews;

        quicksort(0, arrNews.size() - 1);
    }

    private void quicksort(int low, int high) {
        int i = low, j = high;
        long pivot = arrNews.get(low + (high-low)/2).timestamp;
        while(i <= j) {
            while(arrNews.get(i).timestamp < pivot)
                i++;

            while(arrNews.get(j).timestamp > pivot)
                j--;

            if(i <= j) {
                swap(i, j);
                i++;
                j--;
            }
        }

        if(low < j)
            quicksort(low, j);
        if(i < high)
            quicksort(i, high);
    }

    private void swap(int first, int second) {
        long temp = arrNews.get(first).timestamp;
        arrNews.get(first).timestamp = arrNews.get(second).timestamp;
        arrNews.get(second).timestamp = temp;
    }
}
