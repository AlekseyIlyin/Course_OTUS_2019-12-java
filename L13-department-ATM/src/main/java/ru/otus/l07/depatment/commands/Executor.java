package ru.otus.l07.depatment.commands;

import ru.otus.l07.depatment.Department;

import java.util.ArrayList;
import java.util.List;

public class Executor {
    private final List<Command> commands = new ArrayList<>();

    public Executor addCommand(Command command) {
        commands.add(command);
        return this;
    }

    public void executeCommands() {
        commands.forEach(Command::execute);
        commands.clear();
    }

}
