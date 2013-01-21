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
	
	
	int faceing;
	int StartX;
	int StartY;
	
	int State = 0;
	
	int TurnState = 0;
	String lastTurn;
	
	int DisEastWall = -1;
	int DisWestWall = -1;
	
    public String nextAction(Collection<String> percepts) {
		System.out.print("STATE = "+State+", Faceing = "+ faceing +"\n");
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
			if (faceing == 0 || faceing == 3)
				return TURN_LEFT;
			
		if (faceing == 2){
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
			lastTurn = faceing == 1 ? TURN_RIGHT : TURN_LEFT;
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
		return GO; // TODO.
	}
	
	void trackPosition(String command)
	{
		System.out.print("Command from TrackPosistion: " + command + "\n");
		if (command.equals(TURN_LEFT))
			faceing = (faceing + 3) % 4;
		if (command.equals(TURN_RIGHT))
			faceing = (faceing + 1) % 4;
		if (command.equals(GO))
		{
			switch (faceing)
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
