package fr.gming.cmd;

import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.console.CommandLine;
import org.apache.karaf.shell.api.console.Completer;
import org.apache.karaf.shell.api.console.Session;
import org.apache.karaf.shell.support.completers.StringsCompleter;

import java.util.List;

import static fr.gming.cmd.RandomLists.getLists;

@Service
public class ListsCompleter implements Completer {

	@Override
	public int complete(Session session, CommandLine commandLine, List<String> candidates) {
		StringsCompleter completer = new StringsCompleter();
		completer.getStrings().addAll(getLists().keySet());
		return completer.complete(session, commandLine, candidates);
	}

}
