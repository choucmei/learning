package chouc.java.thread.juc10fieldupdate;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author chouc
 * @version V1.0
 * @Title: FieldUpdaterTest
 * @Package chouc.java.thread.juc10fieldupdate
 * @Description:
 * @date 1/22/20
 */
public class FieldUpdaterTest {
    public static void main(String[] args) {
        Account account = new Account(0);
    }
}

class FieldThread extends Thread {

    Account account;

    public FieldThread(Account account) {
        this.account = account;
    }

    @Override
    public void run() {

    }
}

class Account {
    private volatile int money;
    private static final AtomicIntegerFieldUpdater<Account> updater = AtomicIntegerFieldUpdater.newUpdater(Account.class, "money");

    Account(int initial) {
        this.money = initial;
    }

    public void increMoney() {
        updater.incrementAndGet(this);
    }

    public int getMoney() {
        return money;
    }

    @Override
    public String toString() {
        return "Account{" +
                "money=" + money +
                '}';
    }
}