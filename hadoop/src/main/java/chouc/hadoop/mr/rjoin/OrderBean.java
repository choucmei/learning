package chouc.hadoop.mr.rjoin;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public class OrderBean implements Writable {

    private boolean flag;

    private String orderId;
    private String dateString;
    private String goodsId;
    private long goodsNum;
    private String goodsName;
    private float price;

    public void set(boolean flag, String orderId, String dateString, String goodsId, long goodsNum, String goodsName, float price) {
        this.flag = flag;
        this.orderId = orderId;
        this.dateString = dateString;
        this.goodsId = goodsId;
        this.goodsNum = goodsNum;
        this.goodsName = goodsName;
        this.price = price;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public long getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(long goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                ", orderId='" + orderId + '\'' +
                ", dateString='" + dateString + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", goodsNum=" + goodsNum +
                ", goodsName='" + goodsName + '\'' +
                ", price=" + price +
                "flag=" + flag +
                '}';
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(orderId);
        dataOutput.writeUTF(dateString);
        dataOutput.writeUTF(goodsId);
        dataOutput.writeLong(goodsNum);
        dataOutput.writeUTF(goodsName);
        dataOutput.writeFloat(price);
        dataOutput.writeBoolean(flag);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        orderId = dataInput.readUTF();
        dateString = dataInput.readUTF();
        goodsId = dataInput.readUTF();
        goodsNum = dataInput.readLong();
        goodsName = dataInput.readUTF();
        price = dataInput.readFloat();
        flag = dataInput.readBoolean();
    }
}
