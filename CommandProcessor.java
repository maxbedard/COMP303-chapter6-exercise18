package chapter6;

import java.util.Stack;

public class CommandProcessor implements Command {
	private Stack<Command> aCommands = new Stack<>();
	private Stack<Command> aUndos = new Stack<>();
	private Program aProgram;
	private Command addCommand;
	private Command removeCommand;
	private Command clearCommand;
	
	public CommandProcessor(Program pProgram){
		assert aProgram !=null;
		aProgram = pProgram;
		addCommand = aProgram.createAddCommand();
		removeCommand = aProgram.createRemoveCommand();
		clearCommand = aProgram.createClearCommand();
	}
	
	public void executeAdd(Day pDay, Show pShow) {
		assert pDay!=null && pShow!=null;
		addCommand.executeAdd(pDay, pShow);
		aCommands.add(addCommand);
	}
	
	public void executeRemove(Day pDay) {
		assert pDay!=null;
		removeCommand.executeRemove(pDay);
		aCommands.add(removeCommand);
	}
	
	public void executeClear() {
		clearCommand.executeClear();
		aCommands.add(clearCommand);
	}
	
	public void undo() {
		assert !aCommands.isEmpty();
		aUndos.push(aCommands.peek());
		aCommands.pop().undo();
	}

	@Override
	public void redo() {
		assert !aUndos.isEmpty();
		aUndos.pop().redo();	
	}
	
}
