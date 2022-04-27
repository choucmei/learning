package chouc.algorithm.t;

public class NodeT {
    static class Node {
        String val;
        Node next;

        public Node(String val) {
            this.val = val;
        }

        public Node(String val, Node next) {
            this.val = val;
            this.next = next;
        }
    }

    public Node delete(Node head) {
        Node aNull = new Node("null"), pre = aNull, current = head;
        aNull.next = head;
        while (current != null) {
            if (current.val.equals("VALUE")) {
                pre.next = current.next;
                break;
            } else {
                pre = current;
                current = current.next;
            }
        }
        return aNull.next;
    }




}
