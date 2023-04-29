public class SpecialBinarySearch {
    public static int definePosition(long[] arr,long target,int leftIndex,int rightIndex){
        int midIndex = (rightIndex+leftIndex)/2;
        if(rightIndex<leftIndex){
            return midIndex;
        }
        if(target==arr[midIndex]){
            return midIndex;
        } else if (target<arr[midIndex]) {
            return definePosition(arr, target, leftIndex, midIndex-1);
        } else if (target>arr[midIndex]){
            return definePosition(arr, target, midIndex+1, rightIndex);
        }
        return -1;
    }
}
