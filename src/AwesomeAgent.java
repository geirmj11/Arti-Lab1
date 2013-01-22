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
		String command = GO;
		String percept = "";
		for (String p : percepts) {
			percept = p;
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
		if (percept.equals(BUMP)){
			if (orientation == 0){
			    StartY--;
				return TURN_LEFT;
			}

		    if (orientation == 3){
			    State = 1;
			    StartX--;
			    StartY--;
			    DisWestWall = 0;
			    TurnState = 2;
			    lastTurn = TURN_LEFT;
			    return lastTurn;
		    }
		}
		return GO;
	}

	String Sweep(String percept)
	{
        if(lastTurn == TURN_LEFT && DisEastWall == 0){
            TurnState = 1;
            lastTurn = TURN_RIGHT;
            return lastTurn;
        }
        if(lastTurn == TURN_RIGHT && DisWestWall == 0){
            TurnState = 1;
            lastTurn = TURN_LEFT;
            return lastTurn;
        }
        if (percept.equals("") && TurnState == 0)
			return GO;
		if (percept.equals(BUMP) && TurnState == 0) {
			TurnState = 1;
			if(orientation == 1) {
			    DisEastWall = 0;
			    DisWestWall--;  
			    StartX++; 
			}
			if(orientation == 3) {
	            DisWestWall = 0;
	            DisEastWall--;    
	            StartX--;
	        }
			lastTurn = orientation == 1 ? TURN_RIGHT : TURN_LEFT;
			return lastTurn;
		}
		if (TurnState == 1) {
			TurnState = 2;
			return GO;
		}
		if (percept.equals(BUMP) && TurnState == 2) {
			State = 2; // Just hit the last wall.
			StartY++;
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
	    if(StartY < 0 && orientation == 0)
	        return GO;
	    if(StartY < 0 && orientation != 0)
	        return lastTurn;

	    if(StartX < 0 && orientation == 3)
	        return GO;
	    if(StartX > 0 && orientation == 1)
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
					StartY++;
					break;

				case 1: // East
					if (State == 1)
						DisEastWall--;
					if (State == 1)
						DisWestWall++;

					StartX--;
					break;

				case 2: // South
					StartY--;
					break;

				case 3: // West
					if (State == 1){
						DisWestWall--;
				    }
					if (State == 1){
						DisEastWall++;
					}
					StartX++;
					break;
			}
		}
		//System.out.print("::::::::::::::::\nSTATE = " + State + " \t\t\tCommand: " + command + "--------------------\n");
		System.out.print("-------------\nSTATE = " + State + ", orientation = "+ orientation + " Command: " + command);
		System.out.print("\n" + "StartX: " + StartX + "\nStartY: " + StartY);
		System.out.print("\nWest Wall: " + DisWestWall + "\nEast Wall: " + DisEastWall + "\n:::::::::::::\n");            
	}
}
