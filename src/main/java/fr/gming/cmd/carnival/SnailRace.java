package fr.gming.cmd.carnival;

import fr.gming.cmd.GmingCommand;
import fr.gming.cmd.utils.KarafFile;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Command(scope = "gming", name = "snail")
public class SnailRace implements GmingCommand {

	private static int round = 0;
	private static final Random random = new Random();
	private static final List<String> hazards;
	private static final List<Integer> hazardsMapping = List.of(5, 6, 8, 9);

	static {
		try {
			hazards = KarafFile.readAllLinesFileInEtc("etc/snailRaceHazards.txt");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static List<Snail> npcSnails = new ArrayList<>();

	@Option(aliases = "-s", name = "--snail", description = "Clear the game and create snails from their names", multiValued = true)
	private List<String> snailColor = new ArrayList<>();

	@Option(aliases = "-p", name = "--setPos", description = "Position of the snail to change")
	private int pos = -1;
	@Option(aliases = "-u", name = "--update", description = "Select snail from its name to update its position")
	private String snailToUpdate;

	@Override
	public Object execute() {

		if (!snailColor.isEmpty()) {
			round = 0;
			npcSnails = snailColor.stream().map(Snail::new).collect(Collectors.toList());
			System.out.println("Added " + npcSnails.size() + " snails.");
		} else if (snailToUpdate != null) {
			if (pos > -1) {
				npcSnails.stream().filter(npcs -> snailToUpdate.equals(npcs.getColor())).forEach(snail -> snail.setPosition(pos));
				System.out.println("Changed " + snailToUpdate + " position to " + pos + ".");
			} else {
				System.out.println("ERROR : pos was not set, no snail updated.");
			}
			return null;
		} else {
			playAndPrintSnailsTurn();
		}

		return null;

	}

	private void hazard() {
		int roll = random.nextInt(8) + 1;
		if (roll <= 3) {
			System.out.println("rolled " + roll + " : nothing crazy happens this round");
			return;
		}
		int hazadNb = mapHazard(roll);
		System.out.println(hazards.get(hazadNb));
		int target = random.nextInt(8) + 1;
		if (target > npcSnails.size()) {
			System.out.println("Hazard target is #" + target + " (which is a pc)");
			return;
		}
		switch (hazadNb) {
			case 0 -> {
				npcSnails.get(target - 1).addPos(-4);
				System.out.println("Snail " + npcSnails.get(target - 1).getColor() + " lost 4 meters on this turn");
			}
			case 1 -> {
				npcSnails.get(target - 1).setStuck(true);
				System.out.println("Snail " + npcSnails.get(target - 1).getColor() + " is stuck eating salad this turn");
			}
			case 2 -> {
				npcSnails.get(target - 1).addPos(2);
				System.out.println("Snail " + npcSnails.get(target - 1).getColor() + " rushed 2 extra meters this round");
			}
			case 3 -> {
				roll = random.nextInt(20) + 1;
				System.out.print("Rolled " + roll + ", ");
				if (roll < 15) {
					npcSnails.get(target - 1).setHasJockey(false);
					System.out.println("snail " + npcSnails.get(target - 1).getColor() + " lost his jockey !");
				} else {
					System.out.println("jockey of snail " + npcSnails.get(target - 1).getColor() + " almost fell !");
				}
			}
			default -> throw new RuntimeException("Unexpected hazadNb : " + hazadNb + " (out of expected bounds");
		}
	}

	private int mapHazard(int roll) {
		for (int i = 0; i < hazardsMapping.size(); i++) {
			if (roll < hazardsMapping.get(i)) {
				return i;
			}
		}
		throw new RuntimeException("Unexpected roll : " + roll + " (out of 'hazardsMapping' bounds");
	}

	private void playAndPrintSnailsTurn() {
		System.out.println();
		System.out.println("Round : " + ++round);
		System.out.println();
		if (round > 1) {
			hazard();
		}
		System.out.println("--------------------------------------------");
		npcSnails.forEach(snail ->
				System.out.println(snail.getColor()
						+ "\t: moved " + snail.move()
						+ ";\t is now at position " + snail.getPosition()
						+ "\n")
		);
	}

}
