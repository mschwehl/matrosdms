package net.schwehla.matrosdms.persistenceservice.internal.cryptprovider.externalcommand;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public abstract class ExternalCommand {

    protected final String[] args;
    public ExternalCommand(String... args) {
        this.args = args;
    }

    protected abstract String command();

    protected abstract File directory();

    protected int timeout() {
        return 1000 * 60;// default timeout
    }

    public ExternalCommandResult execute() throws Exception {

        if (! directory().exists() || ! directory().isDirectory() ) {
        	throw new IllegalStateException("Not a directory " + directory());
        }
        
        List<String> args = new ArrayList<>();
        args.add(command());
        
        String[] params = this.args;
        if (this.args != null) {
            for (String param : params) {
                args.add(param);
            }
        }

        ProcessBuilder builder = new ProcessBuilder(args);
        
        // http://stackoverflow.com/questions/21764578/change-the-working-drive-java-processbuilder
        // https://community.oracle.com/thread/2066415 -- directory is outDir :-§
        
        builder = builder.directory(directory());

        final Process process = builder.start();
        final Timer timeout = new Timer();
        timeout.schedule(new TimerTask() {
            @Override
            public void run() {
                process.destroy();
            }
        }, timeout());// timeout
        int exitCode = process.waitFor();
        timeout.cancel();

        return new ExternalCommandResult(exitCode, output(process));
    }

    private String output(Process process) throws IOException {
        InputStream inputStream = process.getInputStream();

        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        int readBytes;
        byte[] buffer = new byte[2048];
        while ((readBytes = inputStream.read(buffer)) >= 0) {
            bytes.write(buffer, 0, readBytes);
        }

        return new String(bytes.toByteArray());
    }

    


    @Override
    public String toString() {
        return String.format("%s %s", command(), Arrays.stream(args).collect(Collectors.joining(" ") ));
    }
}

