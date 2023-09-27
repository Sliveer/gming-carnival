package fr.gming.cmd.carnival;

import fr.gming.cmd.RandomDrawWithoutReplacement;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

@Service
@Command(scope = "gming", name = "atmosphere")
public class RandomAtmosphere implements Action {

	@Option(name = "--reset", description = "Resets current command data. If set to true then all other args are ignored")
	private boolean reset = false;

	@Argument
	private String arg = "p";

	@Override
	public Object execute() {
		RandomDrawWithoutReplacement atmosphere;
		switch (arg) {
			case "p", "plus", "inc", "increase", "increases" -> atmosphere = new AtmosphereIncrease();
			case "m", "minus", "dec", "decrease", "decreases" -> atmosphere = new AtmosphereDecrease();
			default -> {
				System.out.println("Invalid arg : " + arg + "\nExpected 'p' or 'm'");
				return null;
			}
		}
		atmosphere.setReset(reset);
		return atmosphere.execute();
	}

}
