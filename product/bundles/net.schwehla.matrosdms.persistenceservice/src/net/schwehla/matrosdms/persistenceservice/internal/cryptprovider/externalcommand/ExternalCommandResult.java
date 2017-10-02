package net.schwehla.matrosdms.persistenceservice.internal.cryptprovider.externalcommand;

public class ExternalCommandResult {
	
    public final int code;
    public final String output;

    public ExternalCommandResult(int code, String output) {
        this.code = code;
        this.output = output;
    }
}
