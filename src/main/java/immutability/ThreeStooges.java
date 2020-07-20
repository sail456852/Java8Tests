package immutability;

import javax.annotation.concurrent.Immutable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: Eugene_Wang<br/>
 * Date: 7/16/2020<br/>
 * Time: 11:20 AM<br/>
 * To change this template use File | Settings | File Templates.
 *
 * @author Eugene_Wang
 */
@Immutable
public final class ThreeStooges {
    private final Set<String> stooges = new HashSet<String>();

    public ThreeStooges() {
        stooges.add("Moe");
        stooges.add("Larry");
        stooges.add("Curly");
    }

    public boolean isStooge(String name) {
        return stooges.contains(name);
    }

    public boolean modifyStooges(String newName) {
        boolean add = stooges.add(newName);
        return add;
    }

    @Override
    public String toString() {
        String msg = "";
        for (String stooge : stooges) {
           msg += stooge;
        }
        return msg;
    }
}
