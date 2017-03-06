import java.util.*;

/**
 * 01.02.2017
 *
 * @author James
 */
public class Command {
    private String name;
    private Executable executable;
    private String description;
    private Map<String, Argument> argumentMap;
    private List<String> argumentOrderList;
    private Map<String, FlagArgument> flagMap;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
        this.argumentMap = new HashMap<>();
        this.argumentOrderList = new ArrayList<>();
        this.flagMap = new HashMap<>();

        FlagArgument helpArg = new FlagArgument("help");
        this.addArgument(helpArg);
    }
    public Command(String name) { this(name, null); }

    public void setExecutable(Executable executable) {
        if(this.getFlagArgument("help").getValue()) {
            System.out.println(this);
            System.out.println();
        }
        this.executable = executable;
    }

    public void addArgument(Argument arg) {
        if(arg instanceof FlagArgument) {
            this.flagMap.put(arg.getName(), (FlagArgument)arg);
        } else {
            this.argumentOrderList.add(arg.getName());
            this.argumentMap.put(arg.getName(), arg);
        }
    }
    public void addAllArguments(Argument...args) {
        for(Argument arg : args)
            addArgument(arg);
    }

    public String getName() { return this.name; }

    public Argument getArgument(String name) { return this.argumentMap.get(name); }
    public StringArgument getStringArgument(String name) {
        Argument argument = this.getArgument(name);
        return (argument instanceof StringArgument) ? (StringArgument)argument : null;
    }
    public IntegerArgument getIntegerArgument(String name) {
        Argument argument = this.getArgument(name);
        return (argument instanceof IntegerArgument) ? (IntegerArgument)argument : null;
    }
    public FlagArgument getFlagArgument(String name) { return this.flagMap.get(name); }

    public boolean parseArguments(String argumentString) {
        if(argumentString.equals("") && this.countRequiredArguments() != 0)
            return false;

        char delimiter = '\0';
        String delimString = "";
        boolean inQuote = false;
        String[] argumentList;

        for(int i = 0; i < argumentString.length(); i++) {
            char c = argumentString.charAt(i);
            switch(c) {
                case '"':
                    inQuote = !inQuote;
                    break;
                case ' ':
                    delimString += (inQuote) ? c : delimiter;
                    break;
                default:
                    delimString += c;
            }
        }

        int numFlags = 0;
        for(FlagArgument flgArg : this.flagMap.values()) {
            flgArg.parseValue(delimString);
            if(flgArg.getValue()) numFlags++;
        }

        argumentList = delimString.split("" + delimiter);
        int numArgs = argumentList.length - numFlags;
        if(argumentList.length == 1 && argumentList[0] == "") numArgs--;
        if((numArgs < this.countRequiredArguments() || this.argumentOrderList.size() < numArgs) && numFlags == 0)
            return false;

        int offset = 0;
        for(int i = 0; i + offset < numArgs; i++) {
            Argument arg = this.argumentMap.get(this.argumentOrderList.get(i));
            String s = argumentList[i + offset];
            if(arg != null && !s.contains(FlagArgument.IDENTIFIER)) {
                if (i < argumentList.length)
                    arg.parseValue(s);
                else
                    break;
            } else {
                offset++;
            }
        }

        return true;
    }

    public int countRequiredArguments() {
        int count = 0;
        for(Argument arg : this.argumentMap.values()) {
            if(arg.isRequired()) count++;
        }

        return count;
    }

    public void execute() {
        if(this.getFlagArgument("help").getValue()) {
            System.out.println(this);
        }

        this.executable.execute();

        for(Argument arg : this.argumentMap.values()) {
            arg.reset();
        }

        for(FlagArgument flgArg : this.flagMap.values()) {
            flgArg.reset();
        }
    }

    @Override
    // Display help text
    public String toString() {
        String desc = this.name;

        for(String str : this.argumentOrderList) {
            Argument arg = this.argumentMap.get(str);
            desc += " " + arg;
        }
        for(Argument arg : this.flagMap.values()) {
            desc += " " + arg;
        }

        if (this.description != null)
            desc += "\n" + this.description;

        return desc;
    }
}
