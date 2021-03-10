package company.tap.samsungpayexample.APIHelper;

/**
 *
 * Since Pair calss, require to hold serializable object, i using semi structure with this tuple class
 */

public class Tuple<first, second> {
    public final first first;
    public final second second;
    public Tuple(first first, second second) {
        this.first = first;
        this.second = second;
    }
}