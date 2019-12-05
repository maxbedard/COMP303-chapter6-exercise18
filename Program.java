package chapter6;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Stack;

import javafx.util.Pair;

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
	private void add(Day pDay, Show pShow)
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
	
	public Command addCommand(Day pDay, Show pShow) {
		return new Command() {
			private Day aDay = pDay;
			private Show aShow = pShow;
			
			@Override
			public void execute() {
				add(aDay, aShow);
			}

			@Override
			public void undo() {
				remove(aDay);
			}
			
		};
	}
	
	public Command removeCommand(Day pDay) {
		return new Command() {
			private Day aDay = pDay;
			private Show aShow;
			
			@Override
			public void execute() {
				aShow = aShows.get(aDay);
				remove(aDay);
			}

			@Override
			public void undo() {
				assert aShow!=null;
				add(aDay, aShow);
			}		
			
		};
	}
	
	public Command clearCommand() {
		return new Command() {
			private EnumMap<Day, Show> aShowsCopy = new EnumMap<>(Day.class);
			
			@Override
			public void execute() {
				aShowsCopy.putAll(aShows);
				clear();	
			}

			@Override
			public void undo() {
				for (Day day: Day.values()) {
					aShows.put(day, aShowsCopy.get(day));
				}
			}
		};
	}
	
}
