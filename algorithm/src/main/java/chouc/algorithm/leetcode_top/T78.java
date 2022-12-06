package chouc.algorithm.leetcode_top;

public class T78 {
    public static void main(String[] args) {

    }
}

class Trie {
    boolean isEnd;
    Trie[] tree;

    public Trie() {
        isEnd = false;
        tree = new Trie[26];
    }

    public void insert(String word) {
        Trie node = this;
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a';
            if (node.tree[index] == null) {
                node.tree[index] = new Trie();
            }
            node = node.tree[index];
        }
        node.isEnd = true;
    }

    public boolean search(String word) {
        Trie trie = searchPrefix(word);
        return trie != null && trie.isEnd;
    }

    public boolean startsWith(String prefix) {
        return searchPrefix(prefix) != null;
    }

    public Trie searchPrefix(String prefix) {
        Trie node = this;
        for (int i = 0; i < prefix.length(); i++) {
            int index = prefix.charAt(i) - 'a';
            if (node.tree[index] == null) {
                return node.tree[index];
            } else {
                node = node.tree[index];
            }
        }
        return node;
    }


    public static void main(String[] args) {
//        ["Trie","insert","search","search","startsWith","insert","search"]
//        [[],["apple"],["apple"],["app"],["app"],["app"],["app"]]
        Trie trie = new Trie();
        trie.insert("apple");
        System.out.println(trie.search("apple"));
        System.out.println(trie.search("app"));
        System.out.println(trie.startsWith("app"));
        trie.insert("app");
        System.out.println(trie.search("app"));
    }


}