/**
 * Created by Jay on 02.02.2017.
 */
public class IntegerArgument extends Argument<Integer> {
    public IntegerArgument(String name) {
        super(name);
    }

    @Override
    public void parseValue(String str) {
        try {
            this.setValue(Integer.parseInt(str));
        } catch (NumberFormatException nfe) {
            this.setValue(null);
        }
    }
}
