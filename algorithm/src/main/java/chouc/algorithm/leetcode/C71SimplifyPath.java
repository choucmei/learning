package chouc.algorithm.leetcode;

import java.util.Iterator;
import java.util.Stack;

/**
 * @author chouc
 * @version V1.0
 * @Title: C71SimplifyPath
 * @Package chouc.java.algorithm.leetcode
 * @Description:
 * @date 2021/4/7
 */
public class C71SimplifyPath {
    public String simplifyPath(String path) {
        Stack<String> stack = new Stack<>();
        for (String s : path.split("/")) {
            switch (s) {
                case ".":
                case "":
                    continue;
                case "..":
                    if (!stack.isEmpty()){
                        stack.pop();
                    }
                    break;
                default:
                    stack.push(s);
            }
        }
        Iterator<String> it = stack.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()){
            sb.append("/")
                    .append(it.next());
        }
        return sb.length()==0?"/":sb.toString();
    }

    public static void main(String[] args) {

    }
}
