package chapter6;

public interface Command {
	void executeAdd(Day pDay, Show pShow);
	void executeRemove(Day pDay);
	void executeClear();
	void undo();
	void redo();
}
