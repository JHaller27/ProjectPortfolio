import java.util.ArrayList;

/**
 * 01.02.2017
 *
 * @author James
 */
public class Test {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        CommandParser parser = new CommandParser();

        // Add item command
        Command addCmd = new Command("add", "Add an item to the list");

        Argument itemArg = new StringArgument("item");
        Argument numArg = new IntegerArgument("number");
        numArg.setRequired(false);

        addCmd.addAllArguments(itemArg, numArg);
        addCmd.setExecutable(() -> {
            list.add(addCmd.getStringArgument("item").getValue());
        });

        parser.addCommand(addCmd);

        // Display list command
        Command listCmd = new Command("lst", "Display list");

        Argument prettyArg = new FlagArgument("pretty");
        prettyArg.setRequired(false);

        listCmd.addArgument(prettyArg);
        listCmd.setExecutable(() -> {
            if(listCmd.getFlagArgument("pretty").getValue()) {
                System.out.println(list);
            } else {
                for(String s : list) {
                    System.out.println(" - " + s);
                }
            }
            System.out.println();
        });

        parser.addCommand(listCmd);

        parser.startParseLoop();
    }
}
