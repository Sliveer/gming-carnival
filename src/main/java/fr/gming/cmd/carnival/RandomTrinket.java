package fr.gming.cmd.carnival;

import fr.gming.cmd.GmingCommand;
import fr.gming.cmd.utils.KarafFile;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@Service
@Command(scope = "gming", name = "trinket")
public class RandomTrinket implements GmingCommand {

	private static List<String> allTrinkets;

	@Option(aliases = "-i", name = "--id", description = "Id of the trinket to retrieve")
	private int id = -1;

	@Option(aliases = "-n", name = "--nb", description = "Nb of trinkets to pick (default is 1)")
	private int nbToPick = 1;

	private Random random = new Random();

	static {
		try {
			allTrinkets = KarafFile.readAllLinesFileInEtc("lists/trinkets.txt");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object execute() {

		if (id >= 0) {
			pick(Integer.valueOf(id));
		} else {
			randomPick(nbToPick);
		}

		return null;

	}

	private void pick(int i) {
		System.out.println(allTrinkets.get(i - 1));
	}

	private void randomPick(int nb) {
		for (int i = 0; i < nb; i ++) {
			int nexInt = random.nextInt(allTrinkets.size());
			String picked = allTrinkets.get(nexInt);

			int index = allTrinkets.indexOf(picked);

			System.out.println((1 + i) + ".\t(" + (index + 1) + ")\t" + picked);
		}
	}

}
