/**
 * Created by Jay on 02.02.2017.
 */
public class StringArgument extends Argument<String> {
    public StringArgument(String name) {
        super(name);
    }

    @Override
    public void parseValue(String str) {
        this.setValue(str);
    }
}
