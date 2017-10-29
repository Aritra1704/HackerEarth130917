package in.arpaul.inshorts.dataobjects;

import java.io.Serializable;

/**
 * Created by aritrapal on 12/09/17.
 */

public class BaseDO implements Cloneable {
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
