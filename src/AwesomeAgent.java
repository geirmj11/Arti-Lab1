import java.util.Collection;
import java.util.Random;

public class AwesomeAgent implements Agent
{
	final String BUMP = "BUMP";
	final String DIRT = "DIRT";

	final String TURN_LEFT = "TURN_LEFT";
	final String TURN_RIGHT = "TURN_RIGHT";
	final String TURN_OFF = "TURN_OFF";
	final String SUCK = "SUCK";
	final String GO = "GO";

	int orientation;
	int StartX;
	int StartY;

	int State = 0;

	int TurnState = 0;
	String lastTurn;

	int DisEastWall = -1;
	int DisWestWall = -1;

    public String nextAction(Collection<String> percepts) {
		System.out.print("STATE = "+State+", orientation = "+ orientation +"\n");
		String command = GO;
		String percept = "";
		for (String p : percepts) {
			percept = p;
			System.out.print("This happend: '" + percept.trim() + "'\n");
			if (percept.equals(DIRT))
				return SUCK;
		}

		switch (State)
		{
			case 0: // Find north wall ! :)
			command = FindNorthLeftCorner(percept);
			break;

			case 1: // Sweep the area :D
			command = Sweep(percept);
			break;

			case 2: // Go home ! 
			command = GoHome(percept);
			break;
		}

		trackPosition(command);
		return command;
	}

	String FindNorthLeftCorner(String percept)
	{
		if (percept.equals(BUMP))
			if (orientation == 0 || orientation == 3)
				return TURN_LEFT;

		if (orientation == 2){
			State = 1;
			return TURN_LEFT;
		}
		return GO;
	}

	String Sweep(String percept)
	{
		if (percept.equals("") && TurnState == 0)
			return GO;
		if (percept.equals(BUMP) && TurnState == 0) {
			TurnState = 1;
			lastTurn = orientation == 1 ? TURN_RIGHT : TURN_LEFT;
			return lastTurn;
		}
		if (TurnState == 1) {
			TurnState = 2;
			return GO;
		}
		if (percept.equals(BUMP) && TurnState == 2) {
			State = 2; // Just hit the last wall.
			return lastTurn;
		}
		if (TurnState == 2) {
			TurnState = 0;
			return lastTurn;
		}

		return GO;
	}

	String GoHome(String percept)
	{
	    if(StartX == 0 && StartY == 0)
	        return TURN_OFF;
	    if(StartY > 0 && orientation == 2)
	        return GO;
	    if(StartY > 0 && orientation != 2)
	        return lastTurn;
	    if(StartX < 0 && orientation != 3)
	        return lastTurn;
	    if(StartX > 0 && orientation != 1)
	        return lastTurn;
	    return GO; 
	}

	void trackPosition(String command)
	{
		System.out.print("Command from TrackPosistion: " + command + "\n" + "StartX: " + StartX + "\nStartY: " + StartY + "\n");
		if (command.equals(TURN_LEFT))
			orientation = (orientation + 3) % 4;
		if (command.equals(TURN_RIGHT))
			orientation = (orientation + 1) % 4;
		if (command.equals(GO))
		{
			switch (orientation)
			{
				case 0: // North
					StartY++;
					break;

				case 1: // East
					if (DisEastWall >= 0)
						DisEastWall--;
					if (DisWestWall >= 0)
						DisWestWall++;

					StartX--;
					break;

				case 2: // South
					StartY--;
					break;

				case 3: // West
					if (DisWestWall >= 0)
						DisWestWall--;
					if (DisEastWall >= 0)
						DisEastWall++;

					StartX++;
					break;
			}
		}
	}
}
