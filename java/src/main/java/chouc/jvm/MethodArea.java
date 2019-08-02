package chouc.jvm;

/**
 * @author chouc
 * @version V1.0
 * @Title: MethodArea
 * @Package chouc.jvm
 * @Description:
 * @date 7/18/19
 */
public class MethodArea {
    public static void main(String[] args) {
        StringBuilder sb =  new StringBuilder();
        sb.append("123456");
        sb.delete(sb.length()-2,sb.length());
        System.out.println(sb.toString());
        int a = 1;
        Integer b = 1;
        Integer c = new Integer(1);
        Integer d = Integer.valueOf(1);


        System.out.println(a == b);

        System.out.println(a == c);

        System.out.println(b == c);

        System.out.println(b.hashCode());
        System.out.println(System.identityHashCode(b));
        System.out.println(System.identityHashCode(c));
        System.out.println(System.identityHashCode(d));


        String f = "abc";
        String g = new String("abc");
    }
}
