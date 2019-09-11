package chouc.jdk8;

/**
 * @author chouc
 * @version V1.0
 * @Title: AdditionInterface
 * @Package chouc.jdk8
 * @Description: Java 8使我们能够通过使用 default 关键字向接口添加非抽象方法实现。 此功能也称为虚拟扩展方法。
 * @date 9/10/19
 */
public interface AdditionInterface {

    double calculate(int a);

    default double sqrt(int a) {
        return Math.sqrt(a);
    }
}
