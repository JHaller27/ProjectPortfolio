/**
 * 03.02.2017
 *
 * @author James
 */
public class FlagArgument extends Argument<Boolean> {
    public final static String IDENTIFIER = "--";

    public FlagArgument(String name) {
        super(name);
        this.setDefaultValue(false);
        this.setValue(false);
        this.setRequired(false);
    }

    @Override
    public void parseValue(String str) {
        if(this.containedIn(str))
            this.setValue(true);
    }

    private Boolean containedIn(String argString) {
        return argString.contains(IDENTIFIER + this.getName());
    }

    public String toString() {
        return IDENTIFIER + this.getName();
    }
}
