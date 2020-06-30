package chouc.java.algorithm.tree;

/**
 * @author chouc
 * @version V1.0
 * @Title: AVLTree
 * @Package chouc.java.algorithm
 * @Description:
 * @date 3/10/20
 */
public class
AVLTree<E extends Comparable<E>> {

    private AVLNode root;

    public AVLTree() {
        this.root = null;
    }

    public void insert(E x) {
        root = insert(x, root);
    }

    public void remove(E x) {
        remove(x, root);
    }

    public int height() {
        return height(root);
    }

    /**
     * 插入新数据
     */
    public AVLNode<E> insert(E x, AVLNode<E> node) {
        if (node == null) {
            return new AVLNode<E>(x);
        }
        //先比较 是插左边还是插右边
        int compareResult = x.compareTo(node.element);
        if (compareResult < 0) {//插到左子树上
            node.left = insert(x, node.left);
            //插入之后要判断是否打破了平衡，因为插入的是左子树，
            // 只有左子树才会打破平衡，用左子树的高减去右子树的高
            if (height(node.left) - height(node.right) == 2) {
                //如果等于2，说明平衡被打破了，需要进行调整。就看选择什么方法调整
                if (x.compareTo(node.left.element) < 0) {
                    //如果x小于t的左子树的值，那么x会被插到t的左子树的左子树上，符合LL 用右旋转调整。
                    node = rightRotate(node);
                } else {
                    //如果x大于t的左子树的值，则会被插到t的左子树的右子树上，符合LR，用先左旋转后右旋转来矫正。
                    node = leftAndRightRotate(node);
                }
            }
        } else if (compareResult > 0) {//插到右子树上，逻辑和上面一样。
            node.right = insert(x, node.right);
            if (height(node.right) - height(node.left) == 2) {
                if (x.compareTo(node.right.element) > 0) {
                    node = leftRotate(node);
                } else {
                    node = rightAndLeftRotate(node);
                }
            }
        } else {
            //已经有这个值了
        }
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        return node;
    }

    /**
     * 删除数据
     */
    private AVLNode<E> remove(E x, AVLNode<E> node) {
        if (node == null)
            return null;
        int compareResult = x.compareTo(node.element);
        if (compareResult < 0) {
            node.left = remove(x, node.left);
            //完了之后验证该子树是否平衡
            if (node.right != null) {        //若右子树为空，则一定是平衡的，此时左子树相当对父节点深度最多为1, 所以只考虑右子树非空情况
                if (node.left == null) {     //若左子树删除后为空，则需要判断右子树
                    if (height(node.right) - node.height == 2) {
                        AVLNode<E> k = node.right;
                        if (k.right != null) {        //右子树存在，按正常情况单旋转
                            node = leftRotate(node);
                        } else {                      //否则是右左情况，双旋转
                            node = rightAndLeftRotate(node);
                        }
                    }
                }
                if (node.left != null) {
                    //否则判断左右子树的高度差
                    //左子树自身也可能不平衡，故先平衡左子树，再考虑整体
                    AVLNode<E> k = node.left;
                    //删除操作默认用右子树上最小节点补删除的节点
                    //k的左子树高度不低于k的右子树
                    if (k.right != null) {
                        if (height(k.left) - height(k.right) == 2) {
                            AVLNode<E> m = k.left;
                            if (m.left != null) {     //左子树存在，按正常情况单旋转
                                k = rightRotate(k);
                            } else {                      //否则是左右情况，双旋转
                                k = leftAndRightRotate(k);
                            }
                        }
                    } else {
                        if (height(k.left) - k.height == 2) {
                            AVLNode<E> m = k.left;
                            if (m.left != null) {     //左子树存在，按正常情况单旋转
                                k = rightRotate(k);
                            } else {                      //否则是左右情况，双旋转
                                k = leftAndRightRotate(k);
                            }
                        }
                    }
                    if (height(node.right) - height(node.left) == 2) {
                        //右子树自身一定是平衡的，左右失衡的话单旋转可以解决问题
                        node = leftRotate(node);
                    }
                }
            }
            //完了之后更新height值
            node.height = Math.max(height(node.left), height(node.right)) + 1;
        } else if (compareResult > 0) {
            node.right = remove(x, node.right);
            //下面验证子树是否平衡
            if (node.left != null) {         //若左子树为空，则一定是平衡的，此时右子树相当对父节点深度最多为1
                node = balanceChild(node);
            }
            //完了之后更新height值
            node.height = Math.max(height(node.left), height(node.right)) + 1;
        } else if (node.left != null && node.right != null) {
            //默认用其右子树的最小数据代替该节点的数据并递归的删除那个节点
            AVLNode<E> min = node.right;
            while (min.left != null) {
                min = min.left;
            }
//            node.element = findMin(node.right).element;
            node.element = min.element;
            node.right = remove(node.element, node.right);
            node = balanceChild(node);
            //完了之后更新height值
            node.height = Math.max(height(node.left), height(node.right)) + 1;
        } else {
            node = (node.left != null) ? node.left : node.right;
        }
        return node;
    }

    /**
     * 平衡
     */
    private AVLNode<E> balanceChild(AVLNode<E> node) {
        if (node.right == null) {        //若右子树删除后为空，则只需判断左子树与根的高度差
            if (height(node.left) - node.height == 2) {
                AVLNode<E> k = node.left;
                if (k.left != null) {
                    node = rightRotate(node);
                } else {
                    node = leftAndRightRotate(node);
                }
            }
        } else {              //若右子树删除后非空，则判断左右子树的高度差
            //右子树自身也可能不平衡，故先平衡右子树，再考虑整体
            AVLNode<E> k = node.right;
            //删除操作默认用右子树上最小节点（靠左）补删除的节点

            if (k.left != null) {
                if (height(k.right) - height(k.left) == 2) {
                    AVLNode<E> m = k.right;
                    if (m.right != null) {        //右子树存在，按正常情况单旋转
                        k = leftRotate(k);
                    } else {                      //否则是右左情况，双旋转
                        k = rightAndLeftRotate(k);
                    }
                }
            } else {
                if (height(k.right) - k.height == 2) {
                    AVLNode<E> m = k.right;
                    if (m.right != null) {        //右子树存在，按正常情况单旋转
                        k = leftRotate(k);
                    } else {                      //否则是右左情况，双旋转
                        k = rightAndLeftRotate(k);
                    }
                }
            }
            //左子树自身一定是平衡的，左右失衡的话单旋转可以解决问题
            if (height(node.left) - height(node.right) == 2) {
                node = rightRotate(node);
            }
        }
        return node;
    }


    /**
     * 右旋转
     *
     * @param node 需要调整的树
     * @return 调整后的树
     */
    private AVLNode<E> rightRotate(AVLNode<E> node) {
        AVLNode newTree = node.left;
        node.left = newTree.right;
        newTree.right = node;
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        newTree.height = Math.max(height(newTree.left), height(newTree.right)) + 1;
        return newTree;
    }

    /**
     * 左旋转
     */
    private AVLNode<E> leftRotate(AVLNode node) {
        AVLNode<E> newTree = node.right;
        node.right = newTree.left;
        newTree.left = node;
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        newTree.height = Math.max(height(newTree.left), height(newTree.right)) + 1;
        return newTree;
    }

    /**
     * 先左旋后右旋
     */
    private AVLNode<E> leftAndRightRotate(AVLNode<E> node) {
        node.left = leftRotate(node.left);
        return rightRotate(node);
    }

    /**
     * 先右旋后左旋
     */
    private AVLNode<E> rightAndLeftRotate(AVLNode<E> node) {
        node.right = rightRotate(node.right);
        return leftRotate(node);
    }


    /**
     * 获取指定树的高度
     */
    private int height(AVLNode<E> node) {
        if (node != null){
            System.out.println(" node:" + node.element + " h=" + node.height);
        }
        return node == null ? -1 : node.height;
    }

    public void printTree() {
        printTree(root);
    }

    private void printTree(AVLNode<E> tree) {
        if (tree == null) {
            return;
        }
        System.out.print(tree.element + " ");
        printTree(tree.left);
        printTree(tree.right);

    }

    private static class AVLNode<E> {
        E element;
        AVLNode<E> left;
        AVLNode<E> right;
        int height;

        public AVLNode(E element) {
            this(element, null, null);
        }

        public AVLNode(E element, AVLNode<E> left, AVLNode<E> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }
    }


    public static void main(String[] args) {
//        AVLTree<Integer> searchTree = new AVLTree<>();
//        for (int i = 0; i < 10; i++) {
//            searchTree.insert(i);
//        }
//        System.out.println("```");
//        searchTree.printTree();


        AVLTree<Integer> avlTree = new AVLTree<>();
        for (int i = 0; i < 10; i++) {
            avlTree.insert(i);
            System.out.println("插入" + i + "后整颗树的高 " + avlTree.height());
        }
//        System.out.println("一般二叉查找树的先序遍历:");
//        System.out.println();
//        System.out.println("AVL树的先序遍历:");
//        avlTree.printTree();
    }
}