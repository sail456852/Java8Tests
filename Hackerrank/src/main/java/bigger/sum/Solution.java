package bigger.sum;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: Eugene_Wang<br/>
 * Date: 7/29/2020<br/>
 * Time: 10:45 AM<br/>
 * To change this template use File | Settings | File Templates.
 */
public class Solution {
    public static void main(String[] args) {
        long ar[] = {1000000001,1000000002,1000000003,1000000004,1000000005};
        System.err.println(aVeryBigSum2(ar));
    }

    /**
     * wrong sum!
     * @param ar
     * @return
     */
    static long aVeryBigSum(long[] ar) {
        int longSum = 0;
        for(long a : ar){
            System.err.println("a = " + a);
            longSum += a;
        }
        return longSum;
    }

    static long aVeryBigSum2(long[] ar) {
        BigDecimal sum = BigDecimal.valueOf(0);
        for(long a : ar){
            System.err.println("a = " + a);
            BigDecimal ab = BigDecimal.valueOf(a);
            sum = sum.add(ab);
        }
        return sum.longValue();
    }
}
