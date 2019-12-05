package chapter6;

import java.util.Stack;

public class CommandProcessor {
	private final Stack<Command> aCommands = new Stack<>();
	private final Stack<Command> undones = new Stack<>();
	
	public void execute(Command pCommand) {
		pCommand.execute();
		aCommands.push(pCommand);
	}
	
	public void undo() {
		assert !aCommands.isEmpty();
		Command lastExecuted = aCommands.pop();
		lastExecuted.undo();
		undones.push(lastExecuted);
	}
	
	
	public void redo() {
		assert !undones.isEmpty();
		Command lastUndo = undones.pop();
		lastUndo.execute();
		aCommands.push(lastUndo);
	}
	
}
