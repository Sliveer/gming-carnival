package fr.gming.cmd;

import fr.gming.cmd.utils.KarafFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class RandomDrawWithoutReplacement extends ResettableCommand {

	private static final Logger LOGGER = Logger.getLogger("RandomDrawWithoutReplacement");
	private static final String directory = "randomdraw-nr";
	private static final Random random = new Random();

	private static Map<String, List<String>> fullLists = new HashMap<>();
	private static Map<String, List<String>> dynamicLists = new HashMap<>();
	private String file;

	public RandomDrawWithoutReplacement(String file) {
		this.file = file;
		if (!fullLists.containsKey(fullListFile())) {
			LOGGER.log(Level.INFO, "No full version found for file " + file + ", creating full list...");
			try {
				List<String> values = KarafFile.readAllLinesFileInEtc(directory, fullListFile());
				fullLists.put(fullListFile(), values.stream().map(v -> v.replace("\\n", "\n\t")).toList());
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Error reading file " + fullListFile(), e);
				System.out.println("!ERROR! reading file " + fullListFile() + ", could not instantiate random draw...");
			}
		}
		if (!dynamicLists.containsKey(dynamicListFile())) {
			try {
				if (KarafFile.existsInData(directory, dynamicListFile())) {
					List<String> values = KarafFile.readAllLinesFileInData(directory, dynamicListFile());
					values = values.stream()
							.map(v -> v.replace("\t", "\\n"))
							.reduce("", (v1, v2) -> {
								if (v2.startsWith("\\n")) {
									return v1 + v2;
								}
								return v1 + "\n" + v2;
							})
							.lines()
							.map(v -> v.replace("\\n", "\n\t"))
							.filter(v -> ! v.isEmpty())
							.collect(Collectors.toList());
					dynamicLists.put(dynamicListFile(), values);
				} else {
					LOGGER.log(Level.INFO, "No dynamic version found for file " + file + ", creating dynamic list...");
					dynamicLists.put(dynamicListFile(), new ArrayList<>(fullLists.get(fullListFile())));
				}
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Error reading file " + dynamicListFile(), e);
				System.out.println("!ERROR! reading file " + dynamicListFile() + ", could not instantiate random draw...");
			}
		}
	}

	@Override
	public void internalExecute() {
		System.out.println("(picking from " + readDynamicList().size() + " values)\n");

		if (readDynamicList().size() < 1) {
			System.out.println("No more element to pick from!");
			return;
		}

		String picked = readDynamicList().remove(random.nextInt(readDynamicList().size()));

		int index = readFullList().indexOf(picked);

		KarafFile.overrideFileInData(readDynamicList(), directory, dynamicListFile());

		System.out.println((index + 1) + ")\t" + picked);
	}

	@Override
	public void reset() {
		KarafFile.deleteInData(directory, dynamicListFile());
		dynamicLists.remove(dynamicListFile());
	}

	@Override
	public String fullListFile() {
		return file;
	}

	@Override
	public String dynamicListFile() {
		return "dynamic-" + file;
	}

	public List<String> readFullList() {
		return fullLists.get(fullListFile());
	}

	public List<String> readDynamicList() {
		return dynamicLists.get(dynamicListFile());
	}

}
