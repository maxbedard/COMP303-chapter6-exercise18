package chapter6;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Stack;

import javafx.util.Pair;

/**
 * Class responsible for managing a program that consists
 * of various shows presented on different days of one week.
 * Each day of the week must be associated with exactly one
 * Show object. If there is not show on a given day, a special 
 * object of type show is used to represent a "non-show".
 */
public class Program
{
	private final EnumMap<Day, Show> aShows = new EnumMap<>(Day.class);
	
	private static Show nullShow = new Show() {

		@Override
		public String description() {return "";}

		@Override
		public int runningTime() {return 0;}

		@Override
		public Iterator<Show> iterator() {return Collections.emptyIterator();}

		@Override
		public Show copy() {return nullShow;}
		
		@Override
		public boolean equals(Object pObj) {return pObj.equals(nullShow);}
		
		@Override
		public int hashCode() {return 0;}
	};
	
	public boolean isNull(Show pShow) {
		return pShow.equals(nullShow);
	}
	
	public Program() { 
		clear();
	}
	
	/**
	 * Clear the program by removing all existing shows.
	 */
	private void clear()
	{
		for (Day day: Day.values()) {
			aShows.put(day, nullShow);
		}
	}
	
	/**
	 * Adds a new show to the program. Overrides any existing show
	 * on that day.
	 * @param pShow The show to add.
	 * @param pDay The day when the show takes place.
	 */
	public void add(Show pShow, Day pDay)
	{
		assert pShow != null && pDay != null;
		aShows.put(pDay, pShow);
	}
	
	
	/**
	 * Removes a show from the program.
	 * @param pDay The day when we want to zap the show.
	 */
	private void remove(Day pDay)
	{
		assert pDay != null;
		aShows.put(pDay, nullShow);
	}
	
	/**
	 * @param pDay The day of the requested show.
	 * @return A copy of the show on a given day.
	 */
	public Show get(Day pDay)
	{
		assert pDay != null;
		return aShows.get(pDay);
	}
	
	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		for( Day day : aShows.keySet() )
		{
			if( aShows.containsKey(day))
			{
				result.append(String.format("%9s", day.name()))
					.append(": ").append(aShows.get(day).description()).append("\n");
			}
		}
		return result.toString();
	}
	
	public Command createAddCommand() {
		return new Command() {
			private Stack<Day> aDaysStack = new Stack<>();
			private Stack<Pair<Day, Show>> aUndosStack = new Stack<>();
			@Override
			public void executeAdd(Day pDay, Show pShow) {
				add(pShow, pDay);
				aDaysStack.push(pDay);
			}

			@Override
			public void executeRemove(Day pDay) {}

			@Override
			public void executeClear() {}

			@Override
			public void undo() {
				assert !aDaysStack.isEmpty();
				aUndosStack.add(new Pair<Day, Show>(aDaysStack.peek(), aShows.get(aDaysStack.peek())));
				remove(aDaysStack.pop());
			}

			@Override
			public void redo() {
				assert !aUndosStack.isEmpty();
				Pair<Day, Show> temp = aUndosStack.pop();
				executeAdd(temp.getKey(), temp.getValue());
			}
		};
	}
	
	public Command createRemoveCommand() {
		return new Command() {
			private Stack<Day> aDaysStack = new Stack<>();
			private Stack<Show> aShowsStack =new Stack<>();
			private Stack<Day> aUndosStack = new Stack<>();
			@Override
			public void executeRemove(Day pDay) {
				assert pDay!=null;
				aDaysStack.push(pDay);
				aShowsStack.push(aShows.get(pDay));
				remove(pDay);
			}

			@Override
			public void executeAdd(Day pDay, Show pShow) {}

			@Override
			public void executeClear() {}

			@Override
			public void undo() {
				assert !aShowsStack.isEmpty() && !aDaysStack.isEmpty();
				aUndosStack.add(aDaysStack.peek());
				add(aShowsStack.pop(), aDaysStack.pop());
			}

			@Override
			public void redo() {
				assert !aUndosStack.isEmpty();
				executeRemove(aUndosStack.pop());
			}
		};
	}
	
	public Command createClearCommand() {
		return new Command() {
			private Stack<EnumMap<Day, Show>> aMapsStack = new Stack<>();
			
			@Override
			public void executeClear() {
				aMapsStack.add(aShows.clone());
				clear();
			}

			@Override
			public void executeAdd(Day pDay, Show pShow) {}

			@Override
			public void executeRemove(Day pDay) {}

			@Override
			public void undo() {
				assert !aMapsStack.isEmpty();
				EnumMap<Day, Show> temp = aMapsStack.pop();
				for (Day day: temp.keySet()) {
					aShows.put(day, temp.get(day));
				}
			}

			@Override
			public void redo() {
				executeClear();
			}
		};
	}
	
}
