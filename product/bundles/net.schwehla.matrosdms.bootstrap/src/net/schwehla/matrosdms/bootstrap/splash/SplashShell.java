package net.schwehla.matrosdms.bootstrap.splash;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import net.schwehla.matrosdms.domain.admin.MatrosConnectionCredential;

public class SplashShell {

	public static final int COMPONENT_INIT = 1;
	public static final int COMPONENT_LOGIN = 2;
	public static final int COMPONENT_PROGRESS = 3;

	protected Shell shell;
	private Text txtTxtuser;
	private Text txtTxtpassword;
	private Composite compositeProgress;
	private Composite compositeContentLogin;
	
	public void updateLoginFeedback(String text) {
		

		shell.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				loginFeedback.setText(text);
				loginFeedback.redraw();
			}
		});
		
	}

	public void setLocation(Point x) {
		shell.setLocation(x);
	}
	
	public void setSize(Point d) {
		shell.setSize(d);
	}
	
	

	public void setPersistedState(String text, boolean checkbox) {
		
		shell.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				txtTxtuser.setText(text);
				
				btnRememerlogin.setSelection(checkbox);
				btnRememerlogin.redraw();
				txtTxtuser.redraw();
			}
		});
		
	}

	
	
	public void activate(int component) {

		switch (component) {

		case COMPONENT_INIT:

			sl_compositeContentArea.topControl = compositeInit;

			compositeContentArea.layout();
			shell.redraw();

			break;

		case COMPONENT_LOGIN:

			sl_compositeContentArea.topControl = compositeContentLogin;
			
			compositeContentArea.layout();
			shell.redraw();

			shell.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					
					if (txtTxtuser.getText() != null && txtTxtuser.getText().length()> 0) {
						txtTxtpassword.setFocus();
					} else {
						txtTxtuser.setFocus();
					}
					
				
				}
			});
			
			break;

		case COMPONENT_PROGRESS:

			sl_compositeContentArea.topControl = compositeProgress;

			compositeBottom.setVisible(false);
			compositeContentArea.layout();
		
			shell.redraw();

			
			break;

		default:
			break;
		}



	}

	private void setLocation(Display display, Shell shell) {
		Monitor monitor = display.getPrimaryMonitor();
		Rectangle monitorRect = monitor.getBounds();
		Rectangle shellRect = shell.getBounds();
		int x = monitorRect.x + (monitorRect.width - shellRect.width) / 2;
		int y = monitorRect.y + (monitorRect.height - shellRect.height) / 2;
		shell.setLocation(x, y);
	}

	private Point getMonitorCenter(Shell shell) {
		Monitor primary = shell.getDisplay().getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		return new Point(x, y);
	}

	Reciever reciever;

	public SplashShell(Reciever reciever) {

		this.reciever = reciever;

		createContents();

		shell.setSize(400, 250);
		shell.setLocation(getMonitorCenter(shell));

		activate(COMPONENT_INIT);

		shell.open();
		
		
		 shell.getDisplay().asyncExec(new Runnable() {
		        public void run() {
		        	
//		        	shell.setMinimized(true);
//		        	shell.setMinimized(false);
		            shell.forceActive();
		        }
		    });
	
	}

	/**
	 * Create contents of the window.
	 */

	StackLayout sl_compositeContentArea = new StackLayout();
	private Composite compositeContentArea;
	private Composite compositeInit;

	/**
	 * @wbp.parser.entryPoint
	 */
	public void createContents() {
		shell = new Shell(SWT.TOOL | SWT.NO_TRIM  );
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(new GridLayout(1, false));

		Composite compositeHeading = new Composite(shell, SWT.NONE);
		compositeHeading.setLayout(new FillLayout(SWT.HORIZONTAL));
		compositeHeading.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblWelcomeToMatros = new Label(compositeHeading, SWT.NONE);
		lblWelcomeToMatros.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		lblWelcomeToMatros.setText("Welcome to Matros");
		new Label(shell, SWT.NONE);

		compositeContentArea = new Composite(shell, SWT.NONE);
		compositeContentArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeContentArea.setLayout(sl_compositeContentArea);

		compositeInit = new Composite(compositeContentArea, SWT.NONE);
		compositeInit.setLayout(new GridLayout(1, false));
		
		
		Label lblConnecting = new Label(compositeInit, SWT.NONE);
		lblConnecting.setText("connecting...");

		compositeProgress = new Composite(compositeContentArea, SWT.NONE);
		compositeProgress.setLayout(new GridLayout(1, false));

		
		
		// progressBar
		progressBar = new ProgressBar(compositeProgress, SWT.BORDER | SWT.SMOOTH | SWT.HORIZONTAL);
		progressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		progressBar.setMaximum(100);
		
		lbCurrentLoadedStep = new Label(compositeProgress, SWT.NONE);
		lbCurrentLoadedStep.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		lbCurrentLoadedStep.setText("progress");
		

		compositeContentLogin = new Composite(compositeContentArea, SWT.NONE);
		compositeContentLogin.setLayout(new GridLayout(2, false));

		Label lblEnterCreditials = new Label(compositeContentLogin, SWT.NONE);
		lblEnterCreditials.setText("Enter Creditials");
		new Label(compositeContentLogin, SWT.NONE);

		Label lblUser = new Label(compositeContentLogin, SWT.NONE);
		lblUser.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUser.setText("User");

		txtTxtuser = new Text(compositeContentLogin, SWT.BORDER);
		txtTxtuser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblPassword = new Label(compositeContentLogin, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText("Password");

		txtTxtpassword = new Text(compositeContentLogin, SWT.BORDER |  SWT.PASSWORD );
		txtTxtpassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	

		 
		btnRememerlogin = new Button(compositeContentLogin, SWT.CHECK);

		btnRememerlogin.setText("rememerLogin");
		new Label(compositeContentLogin, SWT.NONE);
		new Label(compositeContentLogin, SWT.NONE);
		
		loginFeedback = new Label(compositeContentLogin, SWT.NONE);
		loginFeedback.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		loginFeedback.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		compositeBottom = new Composite(shell, SWT.NONE);
		compositeBottom.setLayout(new GridLayout(3, false));
		compositeBottom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblSpacer = new Label(compositeBottom, SWT.NONE);
		lblSpacer.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		Button btnOk = new Button(compositeBottom, SWT.NONE);
	
		// http://stackoverflow.com/questions/16817725/swt-text-focus-and-default-selectionreturn-key-events
		txtTxtpassword.addListener(SWT.Traverse, new Listener()
		    {
		        @Override
		        public void handleEvent(Event event)
		        {
		            if(event.detail == SWT.TRAVERSE_RETURN)
		            {
		            	btnOk.notifyListeners(SWT.Selection, new Event());
		            }
		        }
		    });
		
		
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				// XXX
				MatrosConnectionCredential cred = new MatrosConnectionCredential();
				cred.setDbPasswd(txtTxtpassword.getText());
				cred.setDbUser(txtTxtuser.getText());

				reciever.fireCredentialCheck(cred);
				
				if (btnRememerlogin.getSelection() && txtTxtuser.getText() != null ) {
					reciever.fireRememberState(txtTxtuser.getText(), true);
				} else {
					reciever.fireRememberState("", false);
				}

			}
		});
		GridData gd_btnOk = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnOk.widthHint = 70;
		gd_btnOk.minimumWidth = 70;
		btnOk.setLayoutData(gd_btnOk);
		btnOk.setText("Ok");

		Button btnCancel = new Button(compositeBottom, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
		GridData gd_btnCancel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnCancel.widthHint = 70;
		gd_btnCancel.minimumWidth = 70;
		btnCancel.setLayoutData(gd_btnCancel);
		btnCancel.setText("Abbruch");




	}

	public void close() {
		shell.close();

	}

	public void dispose() {
		shell.dispose();
	}

	boolean loggedIn = false;
	private ProgressBar progressBar;

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public void runEventLoop() {

		// Use the display provided by the shell if possible
		Display display;
		if (shell == null) {
			display = Display.getCurrent();
		} else {
			display = shell.getDisplay();
		}

		while (shell != null && !shell.isDisposed() && !loggedIn) {
			try {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if (!display.isDisposed())
			display.update();

	}
	
	int progressed = 100;
	private Label lbCurrentLoadedStep;
	private Label loginFeedback;
	private Button btnRememerlogin;
	private Composite compositeBottom;
	

	public void worked(int worked, org.osgi.service.event.Event event) {
		if (progressBar != null && !progressBar.isDisposed()) {
			progressed += worked;
			shell.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					progressBar.setSelection(worked);
					lbCurrentLoadedStep.setText( event.getTopic() );
					lbCurrentLoadedStep.redraw();
					shell.update();
				}
			});
		}
	}


	public void forceActive() {
		shell.forceActive();
	}


	


}
