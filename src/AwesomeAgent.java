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
    
	int orientation; // 0 = North, 1 = East, 2 = South, 3 = West.
	int startX;
	int startY;

	int state = 0;

	int turnState = 0;
	String lastTurn;

	int DisEastWall = -1;
	int DisWestWall = -1;

    public String nextAction(Collection<String> percepts) {
		String command = GO;
		String percept = "";
		for (String p : percepts) {
			percept = p;
			if (percept.equals(DIRT))
				return SUCK;
		}

		switch (state)
		{
			case 0: // Find north left corner ! :)
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
		if (percept.equals(BUMP)){
			if (orientation == 0){
			    startY--;
				return TURN_LEFT;
			}

		    if (orientation == 3){
			    state = 1;
			    startX--;
			    startY--;
			    DisWestWall = 0;
			    turnState = 2;
			    lastTurn = TURN_LEFT;
			    return lastTurn;
		    }
		}
		return GO;
	}

	String Sweep(String percept)
	{
        if(lastTurn == TURN_LEFT && DisEastWall == 0){
            turnState = 1;
            lastTurn = TURN_RIGHT;
            return lastTurn;
        }
        if(lastTurn == TURN_RIGHT && DisWestWall == 0){
            turnState = 1;
            lastTurn = TURN_LEFT;
            return lastTurn;
        }
        if (percept.equals("") && turnState == 0)
			return GO;
		if (percept.equals(BUMP) && turnState == 0) {
			turnState = 1;
			if(orientation == 1) {
			    DisEastWall = 0;
			    DisWestWall--;  
			    startX++; 
			}
			if(orientation == 3) {
	            DisWestWall = 0;
	            DisEastWall--;   
	            startX--;
	        }
			lastTurn = orientation == 1 ? TURN_RIGHT : TURN_LEFT;
			return lastTurn;
		}
		if (turnState == 1) {
			turnState = 2;
			return GO;
		}
		if (percept.equals(BUMP) && turnState == 2) {
			state = 2; // Just hit the last wall.
			startY++;
			return lastTurn;
		}
		if (turnState == 2) {
			turnState = 0;
			return lastTurn;
		}
		return GO;
	}

	String GoHome(String percept)
	{
	    if(startX == 0 && startY == 0)
	        return TURN_OFF;
	    if(startY < 0 && orientation == 0)
	        return GO;
	    if(startY < 0 && orientation != 0)
	        return lastTurn;

	    if(startX < 0 && orientation == 3)
	        return GO;
	    if(startX > 0 && orientation == 1)
	        return GO;
	    return lastTurn; 
	}

	void trackPosition(String command)
	{
		if (command.equals(TURN_LEFT))
			orientation = (orientation + 3) % 4;
		if (command.equals(TURN_RIGHT))
			orientation = (orientation + 1) % 4;
		if (command.equals(GO))
		{
			switch (orientation)
			{
				case 0: // North
					startY++;
					break;

				case 1: // East
					if (state == 1)
						DisEastWall--;
					if (state == 1)
						DisWestWall++;

					startX--;
					break;

				case 2: // South
					startY--;
					break;

				case 3: // West
					if (state == 1){
						DisWestWall--;
				    }
					if (state == 1){
						DisEastWall++;
					}
					startX++;
					break;
			}
		}
		//System.out.print("::::::::::::::::\nSTATE = " + state + " \t\t\tCommand: " + command + "--------------------\n");
		//System.out.print("-------------\nSTATE = " + state + ", orientation = "+ orientation + " Command: " + command);
		//System.out.print("\n" + "startX: " + startX + "\nStartY: " + startY);
		//System.out.print("\nWest Wall: " + DisWestWall + "\nEast Wall: " + DisEastWall + "\n:::::::::::::\n");            
	}
}
