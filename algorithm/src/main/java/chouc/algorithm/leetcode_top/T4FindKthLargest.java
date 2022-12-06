package chouc.algorithm.leetcode_top;

public class T4FindKthLargest {


    public int findKthLargest(int[] nums, int k) {
        int length = nums.length;
        heapMaxSort(nums, length);
        for (int i = 0; i < k - 1; i++) {
            int lastIndex = length - i - 1;
            swap(nums, 0, lastIndex);
            sort(nums, 0, lastIndex);
        }
        return nums[0];
    }

    public void heapMaxSort(int[] num, int length) {
        for (int currentNode = length / 2 - 1; currentNode >= 0; currentNode--) {
            sort(num, currentNode, length);
        }
    }

    public void sort(int[] num, int currentNode, int length) {
        int leftNode = currentNode * 2 + 1, rigthNode = leftNode + 1;
        int maxNode = currentNode;
        if (leftNode < length && num[leftNode] > num[maxNode]) {
            maxNode = leftNode;
        }
        if (rigthNode < length && num[rigthNode] > num[maxNode]) {
            maxNode = rigthNode;
        }
        if (currentNode != maxNode) {
            swap(num, currentNode, maxNode);
            sort(num, maxNode, length);
        }
    }

    public static void swap(int[] array, int start, int end) {
        int tmp = array[start];
        array[start] = array[end];
        array[end] = tmp;
    }

    public static void main(String[] args) {
        int[] nums = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        T4FindKthLargest t4FindKthLargest = new T4FindKthLargest();
        int kthLargest = t4FindKthLargest.findKthLargest(nums, 4);
        System.out.println(kthLargest);
    }
}
