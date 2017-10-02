package net.schwehla.matrosdms.logview.util;

import org.eclipse.e4.core.di.annotations.Execute;

public class ClearLogHandler
{
    @Execute
    public void execute(LogEntryCache cache)
    {
        cache.clear();
    }
}
