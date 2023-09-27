package fr.gming.cmd;

import org.apache.karaf.shell.api.action.Argument;

import java.util.List;
import java.util.Map;

import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import static fr.gming.cmd.utils.KarafFile.listFilesUnderDirInEtc;

@Service
@Command(scope = "gming", name = "lists")
public class RandomLists implements GmingCommand {

	private static Map<String, List<String>> lists;

	@Completion(ListsCompleter.class)
	@Argument(description = "Name of the list to display, or empty to list all known names")
	private String listName;

	public RandomLists() {
		lists = listFilesUnderDirInEtc("lists");
	}

	@Override
	public Object execute() {
		if (lists == null || lists.isEmpty()) {
			System.out.println("ERROR : Lists are not intantiated");
		} else if (null == listName) {
			lists.keySet().forEach(System.out::println);
		} else if (lists.containsKey(listName)) {
			lists.get(listName).forEach(System.out::println);
		} else {
			System.out.println(listName + " not found in lists");
		}
		return null;
	}

	public static Map<String, List<String>> getLists() {
		return lists;
	}

}
