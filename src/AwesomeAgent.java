import java.util.Collection;
import java.util.Random;

public class AwesomeAgent implements Agent
{

    public String nextAction(Collection<String> percepts) {
		System.out.print("perceiving:");
		for(String percept:percepts) {
			System.out.print("'" + percept + "', ");
		}
		return "";
	}
}
