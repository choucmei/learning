package chouc.java.thread.model02;

/**
 * @author chouc
 * @version V1.0
 * @Title: Person
 * @Package chouc.java.thread.model02
 * @Description:
 * @date 1/10/20
 */
public final class Person {
    private final String name;
    private final String address;
    public Person(String name, String address) {
        this.name = name;
        this.address = address;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String toString() {
        return "[ Person: name = " + name + ", address = " + address + " ]";
    }
}