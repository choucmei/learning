package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C59generateMatrix
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/3/4
 */
public class C59GenerateMatrix {

    public int[][] generateMatrix(int n) {
        int[][] rs =new int[n][n];
        int l = 0, r = n - 1, t = 0, b = n - 1;
        int total = n * n;
        int start = 1;
        while (start <= total) {
            for (int i = l; i <= r; i++) {
                System.out.println("c:"+t+" r:"+i+" s:"+start);
                rs[t][i] = start++;
            }
            t++;
            for (int i = t; i <= b; i++) {
                System.out.println("c:"+i+" r:"+r+" s:"+start);
                rs[i][r] = start++;
            }
            r--;
            for (int i = r; i >= l; i--) {
                rs[b][i] = start++;
            }
            b--;
            for (int i = b; i >= t; i--) {
                rs[i][l] = start++;
            }
            l++;
        }
        return rs;
    }

    public static void main(String[] args) {
        C59GenerateMatrix c = new C59GenerateMatrix();
        Utils.printArrayInArray(c.generateMatrix(4));
    }
}
