package UI.Menu;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MenuCommand implements Command {

    private String menuName;
    private Map<String, Command> map= new TreeMap<>();

    public MenuCommand(String menuName) {
        this.menuName = menuName;
    }
    @Override
    public void execute() {
        map.keySet().forEach(x-> System.out.println(x));
    }

    public void addCommand(String desc, Command c){
        map.put(desc, c);
    }

    public List<Command> getCommands(){
        return map.values().stream().collect(Collectors.toList());
    }

    public String getMenuName() {
        return menuName;
    }
}