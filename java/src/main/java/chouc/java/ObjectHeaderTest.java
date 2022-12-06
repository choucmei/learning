package chouc.java;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

public class ObjectHeaderTest {
    public static void main(String[] args) {
        ClassLayout layout = ClassLayout.parseInstance(new Object());
        System.out.println("********** new Object() : ");
        System.out.println(layout.toPrintable());

        System.out.println();
        ClassLayout layout1 = ClassLayout.parseInstance(new int[]{});
        System.out.println("********** new int[]{} : ");
        System.out.println(layout1.toPrintable());

        System.out.println();
        ClassLayout layout2 = ClassLayout.parseInstance(new ArtisanTest());
        System.out.println("********** new ArtisanTest() : ");
        System.out.println(layout2.toPrintable());


        ArtisanTest artisanTest = new ArtisanTest();
        System.out.println(artisanTest.hashCode());
        GraphLayout layout3 = GraphLayout.parseInstance(artisanTest);
        System.out.println("********** new ArtisanTest() artisanTest : ");
        System.out.println(layout3.toPrintable());


        System.out.println();
        ClassLayout str = ClassLayout.parseInstance("abcdefghabcdefghabcdefghabcdefghabcdefghabcdefgh");
        System.out.println("********** abcdefghabcdefghabcdefghabcdefghabcdefghabcdefgh : ");
        System.out.println(str.toPrintable());
        System.out.println(str.hashCode());
        System.out.println(str);

        System.out.println();
        ClassLayout str2 = ClassLayout.parseInstance("abcd");
        System.out.println("********** abcd : ");
        System.out.println(str2.toPrintable());

        GraphLayout graphLayout = GraphLayout.parseInstance("abcd");
        System.out.println("********** abcd : ");
        System.out.println(graphLayout.toPrintable());
        System.out.println(graphLayout.totalSize());


        System.out.println("********** new char[]{'a', 'b', 'c', 'd'} :");
        ClassLayout str3 = ClassLayout.parseInstance(new char[]{'a', 'b', 'c', 'd'});
        System.out.println(str3.toPrintable());

        ClassLayout str31 = ClassLayout.parseInstance(new char[]{'a', 'd'});
        System.out.println("********** new char[]{'a', 'd'} :");
        System.out.println(str31.toPrintable());

        ClassLayout str32 = ClassLayout.parseInstance(new char[]{'a'});
        System.out.println("********** new char[]{'a'} :");
        System.out.println(str32.toPrintable());

        ClassLayout str4 = ClassLayout.parseInstance(new String());
        System.out.println("********** new String() :");
        System.out.println(str4.toPrintable());

        ClassLayout str5 = ClassLayout.parseInstance(new String("abcd"));
        System.out.println("********** new String(\"abcd\"):");
        System.out.println(str5.toPrintable());

        int a = 1, b = 2;
        ClassLayout int1 = ClassLayout.parseInstance(a);
        System.out.println("********** 1: ");
        System.out.println(int1.toPrintable());

        ClassLayout intO = ClassLayout.parseInstance(new Integer(1283));
        System.out.println("********** new Integer(1): ");
        System.out.println(intO.toPrintable());

        int[] ar = new int[]{a, b, b};
        ClassLayout inta = ClassLayout.parseInstance(ar);
        System.out.println("********** new int[]{a, b, b}");
        System.out.println(inta.toPrintable());


        ClassLayout intb = ClassLayout.parseInstance(new int[]{1, 1, 2, 3, 4, 5});
        System.out.println("********** new int[]{1, 1, 2, 3, 4, 5}");
        System.out.println(intb.toPrintable());


    }

    // -XX:+UseCompressedOops           默认开启的压缩所有指针
    // -XX:+UseCompressedClassPointers  默认开启的压缩对象头里的类型指针Klass Pointer
    // Oops : Ordinary Object Pointers
    public static class ArtisanTest {
        //8B mark word
        //4B Klass Pointer   如果关闭压缩-XX:-UseCompressedClassPointers或-XX:-UseCompressedOops，则占用8B
        int id = 111;        //4B
        long l;
        String name = "aaaaaaaaaaaaaaaaaaa";   //4B  如果关闭压缩-XX:-UseCompressedOops，则占用8B
        byte b;        //1B
        Object o = new String("aaaaaaaaaaaaaa");      //4B  如果关闭压缩-XX:-UseCompressedOops，则占用8B
        int[] a = new int[]{1, 1, 2, 3, 4, 5};
    }

}
