package chouc.algorithm.common;

public class ListNode {
    public int val;
    public ListNode next;

    public ListNode() {
    }

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    public static ListNode parse(String listStr) {
        if (listStr.startsWith("[") && listStr.endsWith("]")) {
            listStr = listStr.substring(1, listStr.length() - 1);
        }
        ListNode v = new ListNode(-1), node = v;
        for (String s : listStr.split(",")) {
            node.next = new ListNode(Integer.parseInt(s));
            node = node.next;
        }
        return v.next;
    }

    @Override
    public String toString() {
        return "ListNode{" +
                "val=" + val +
                ", next=" + next +
                '}';
    }
}