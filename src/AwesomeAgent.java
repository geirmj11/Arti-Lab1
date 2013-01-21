import java.util.Collection;
import java.util.Random;

public class AwesomeAgent implements Agent
{
	const String BUMP = "BUMP";
	const String DIRT = "DIRT";
	
	const String TURN_LEFT = "TURN_LEFT";
	const String TURN_RIGHT = "TURN_LEFT";
	const String TURN_OFF = "TURN_OFF";
	const String SUCK = "SUCK";
	const String GO = "GO";
	
	
	int faceing
	int StartX;
	int StartY;
	
	int State = 0;
	
	int SweepingTurn = 0;
	String lastTurn;
	
	int DisEastWall = -1;
	int DisWestWall = -1;
	
    public String nextA ction(Collection<String> percepts) {
		String command = GO;
		String percept = "";
		for (percept : percepts) {
			System.out.print("This happend: '" + percept.trim() + "'\n");
			if (percept == DIRT)
				return SUCK;
		}
		
		switch (State)
		{
			case 0: // Find north wall ! :)
			command = FindNorth(percept);
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
	
	String FindNorth(String percept)
	{
		if (percept.equals(BUMP)) {
			State = 1;
			return TURN_LEFT;
		}
		return GO;
	}
	
	String Sweep(String percept)
	{
		if (percept.equals("") && SweepingTurn == 0)
			return GO;
		if (percept.equals(BUMP) && SweepingTurn == 0) {
			SweepingTurn = 1;
			lastTurn = faceing == 1 ? TURN_RIGHT : TURN_LEFT;
			return lastTurn;
		}
		if (SweepingTurn == 1) {
			SweepingTurn = 2;
			return GO;
		}
		if (percept.equals(BUMP) && SweepingTurn == 2) {
			State = 3; // Just hit the last wall.
			return lastTurn;
		}
		if (percept.equals(BUMP) && SweepingTurn == 2) {
			SweepingTurn = 0;
			return lastTurn;
		}		
	}
	
	String GoHome(String percept)
	{
		return GO; // TODO.
	}
	
	void trackPosition(String command)
	{
		if (command.equals(TURN_LEFT))
			Oriantation = (faceing + 1) % 4;
		if (command.equals(TURN_RIGHT))
			Oriantation = (faceing - 1) % 4;
		if (command.equals(GO))
		{
			switch (facing)
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
