package chouc.hadoop.mr.secondarysort;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class OrderBean implements WritableComparable<OrderBean> {

    private Text orderId;
    private DoubleWritable price;

    public OrderBean() {
    }

    public void set(Text orderId, DoubleWritable price) {
        this.orderId = orderId;
        this.price = price;
    }

    public Text getOrderId() {
        return orderId;
    }

    public void setOrderId(Text orderId) {
        this.orderId = orderId;
    }

    public DoubleWritable getPrice() {
        return price;
    }

    public void setPrice(DoubleWritable price) {
        this.price = price;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(orderId.toString());
        dataOutput.writeDouble(price.get());
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        orderId = new Text(dataInput.readUTF());
        price = new DoubleWritable(dataInput.readDouble());
    }

    @Override
    public int compareTo(@NotNull OrderBean o) {
        int compara = orderId.compareTo(o.getOrderId());
        if (compara == 0){
            this.price.compareTo(o.getPrice());
            System.out.println(" ****************compareTo********************* "  );
        }
        return compara;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "orderId='" + orderId + '\'' +
                ", price=" + price +
                '}';
    }
}
