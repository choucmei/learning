package chouc.hadoop.mr.secondarysort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class ItemidGroupingComparator extends WritableComparator {

    protected ItemidGroupingComparator() {
        super(OrderBean.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {

        System.out.println(" 00000 ");

        OrderBean abean = (OrderBean) a;
        OrderBean bbean = (OrderBean) b;
        return abean.getOrderId().compareTo(bbean.getOrderId());
    }
}
