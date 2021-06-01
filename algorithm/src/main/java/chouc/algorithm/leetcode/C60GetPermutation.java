package chouc.algorithm.leetcode;

/**
 * @author chouc
 * @version V1.0
 * @Title: C60GetPermutation
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/3/4
 */
public class C60GetPermutation {

    public String getPermutation(int n, int k) {
        int[] l = new int[n];
        for (int i=0;i<n;i++){
            l[i]=i+1;
        }
        Utils.printArray(l);

        return "";
    }

    public void dfs(String[] rs,int index,int[] list,StringBuilder sb){

    }


    public static void main(String[] args) {
        C60GetPermutation c = new C60GetPermutation();
        c.getPermutation(10,1);
    }
}
