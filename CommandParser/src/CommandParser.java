import java.util.*;

/**
 * 01.02.2017
 *
 * @author James
 */
public class CommandParser {
    private Map<String, Command> commandMap;
    private Scanner scanner;

    public CommandParser() {
        commandMap = new HashMap<>();

        Command help = new Command("help", "Display list of all commands in parser");
        help.setExecutable(() -> {
            for(Command cmd : commandMap.values()) {
                System.out.println(cmd);
                System.out.println();
            }
        });
        this.addCommand(help);
    }

    public void addCommand(Command command) { this.commandMap.put(command.getName(), command); }
    public void parse(String command) {
        int firstSpace = command.indexOf(' ');
        String name = "";
        String arguments = "";

        if(firstSpace != -1) {
            name = command.substring(0, firstSpace);
            arguments = command.substring(firstSpace + 1);
        } else
            name = command;


        try {
            if(this.commandMap.get(name).parseArguments(arguments)) {
                this.commandMap.get(name).execute();
            } else {
                System.out.println("Format: " + this.commandMap.get(name));
            }
        } catch (NullPointerException e) {
            System.out.println("Command not found. Type \"help\" for a list of available commands");
        }
    }

    public boolean parseInput(String exitString) {
        String rawInput;

        if(this.scanner == null) this.scanner = new Scanner(System.in);

        System.out.print(">>> ");
        rawInput = scanner.nextLine();
        String[] inputStringList = rawInput.split("(; |;)");
        for(String inputString : inputStringList) {
            if (inputString.equals(exitString)) return false;
            this.parse(inputString);
        }
        return true;
    }
    public boolean parseInt() { return this.parseInput(null); }

    public void startParseLoop() {
        while(this.parseInput("exit"));
    }
}
