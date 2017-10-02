package net.schwehla.matrosdms.bootstrap.splash;

import net.schwehla.matrosdms.domain.admin.MatrosConnectionCredential;

public interface Reciever {

	void fireCredentialCheck(MatrosConnectionCredential cred);

	void fireRememberState(String login, boolean checkbox);
}
