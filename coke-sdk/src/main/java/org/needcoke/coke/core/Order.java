package org.needcoke.coke.core;

/**
 * 排序接口
 *
 * @author warren
 */
public interface Order extends Comparable<Order>{

    /**
     * 返回排序关键字
     */
    int getOrder();

    @Override
    default int compareTo(Order o){
        return Integer.compare(this.getOrder(),o.getOrder());
    }
}
