package fr.gming.cmd.carnival;

import fr.gming.cmd.RandomDrawWithoutReplacement;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

@Service
@Command(scope = "gming", name = "suit")
public class RandomSuit extends RandomDrawWithoutReplacement {

	public RandomSuit() {
		super("suit.txt");
	}

}
