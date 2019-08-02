package chouc.spark.demo.interview;

public class SonClas extends FatherClas {
//
//    public void set(){
//        mo = 1;
//        System.out.println(mo);
//    }
//
//
//    class ssonClas extends FatherClas{
//
//    }

    static class Person{
        String name;
    }

    public static void main(String[] args) {
//        int t = 1;
//        change(t);
        Person p =  new Person();
        p.name = "1";
        change(p);
        System.out.println(p.name);
    }

    public static void change(int t){
        t = 2;
    }
    public static void change(Person p){
        p.name = "2";
    }

}
