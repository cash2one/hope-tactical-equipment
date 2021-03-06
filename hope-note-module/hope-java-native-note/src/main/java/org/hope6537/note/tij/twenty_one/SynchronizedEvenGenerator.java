package org.hope6537.note.tij.twenty_one;

/**
 * @author Hope6537(赵鹏)
 * @version 0.9
 * @describe 使用synchronized来进行上锁操作
 * @signdate 2014年7月26日下午8:00:53
 * @company Changchun University&SHXT
 */
public class SynchronizedEvenGenerator extends IntGenerator {
    private int currentValue = 0;

    public static void main(String[] args) {
        EvenChecker.test(new SynchronizedEvenGenerator());
    }

    @Override
    public synchronized int next() {
        // 上锁了之后就安全了
        ++currentValue;
        // 对yield的调用提高Value在奇数时上下文切换的可能性 但是上锁了就不可能会有切换
        Thread.yield();
        ++currentValue;
        return currentValue;
    }
}
