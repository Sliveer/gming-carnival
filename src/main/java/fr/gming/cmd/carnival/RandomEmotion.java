package fr.gming.cmd.carnival;

import fr.gming.cmd.RandomDrawWithoutReplacement;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

@Service
@Command(scope = "gming", name = "emotion")
public class RandomEmotion implements Action {

	@Option(name = "--reset", description = "Resets current command data. If set to true then all other args are ignored")
	private boolean reset = false;

	@Argument
	private String arg = "p";

	@Override
	public Object execute() {
		RandomDrawWithoutReplacement emotion;
		switch (arg) {
			case "p", "plus", "positive", "inc", "increase", "increases" -> emotion = new PositiveEmotion();
			case "m", "minus", "n", "negative", "dec", "decrease", "decreases" -> emotion = new NegativeEmotion();
			default -> {
				System.out.println("Invalid arg : " + arg + "\nExpected 'p' or 'n'");
				return null;
			}
		}
		emotion.setReset(reset);
		return emotion.execute();
	}

}
