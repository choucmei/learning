package chouc.algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: C22GenerateParenthesis
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2020/12/24
 */
public class C22GenerateParenthesis {
    public List<String> generateParenthesis(int n) {
        List<String> ans = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        strace(ans,sb, 0, 0, n);
        return ans;
    }

    private void strace(List<String> ans,StringBuilder sb, int index, int status, int n) {
        if (index == n){
            ans.add(sb.toString());
            return;
        }
        sb.append("(");
        strace(ans,sb,index+1,0,n);
        sb.append(")");
        sb.delete(index+1,sb.length()-1);
        strace(ans,sb,index+1,0,n);
    }


    public static void main(String[] args) {
        C22GenerateParenthesis c = new C22GenerateParenthesis();
        System.out.println(c.generateParenthesis(2));
    }

    // [(((()))), ((()())), ((())()), ((()))(), (()(())), (()()()), (()())(), (())(()), (())()(), ()((())), ()(()()), ()(())(), ()()(()), ()()()()]
}
