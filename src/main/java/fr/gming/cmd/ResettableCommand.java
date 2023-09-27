package fr.gming.cmd;

import org.apache.karaf.shell.api.action.Option;

public abstract class ResettableCommand implements GmingCommand {

	@Option(name = "--reset", description = "Resets current command data. If set to true then all other args are ignored")
	protected boolean reset = false;

	@Override
	public Object execute() {
		if (reset) {
			reset();
			return null;
		}
		internalExecute();
		return null;
	}

	public abstract void internalExecute();

	public abstract void reset();

	public abstract String fullListFile();
	public abstract String dynamicListFile();

	public void setReset(boolean reset) {
		this.reset = reset;
	}

}
