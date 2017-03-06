/**
 * Created by Jay on 02.02.2017.
 */
public abstract class Argument<E> {
    private String name;
    private E value;
    private E defaultValue;
    private boolean required;

    public Argument(String name) {
        this.name = name;
        this.value = null;
        this.defaultValue = null;
        this.required = true;
    }

    public void setName(String name) { this.name = name; }

    public String getName() { return this.name; }

    public E getValue() { return value; }

    public void reset() { this.value = this.defaultValue; }

    public void setRequired(boolean val) { required = val; }

    public boolean isRequired() { return required; }

    public abstract void parseValue(String str);

    protected void setValue(E value) { this.value = value; }
    protected void setDefaultValue(E value) { this.defaultValue = value; }

    public String toString() {
        String str = "";

        if(!required) str += "(";
        str += "[" + name + "]";
        if(!required) str += ")";

        return str;
    }
}
