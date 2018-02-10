package test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class AutoCompleteTest {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AutoCompleteTest window = new AutoCompleteTest();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(new FillLayout());
		
		
		Ch5CompletionEditor e = new Ch5CompletionEditor(shell);
		
		if (true)
			return; 
		
		SourceViewer viewer = new SourceViewer(shell, null, SWT.NONE);
		viewer.getControl().setLayoutData(
		new GridData(GridData.FILL_BOTH)); 


		
		viewer.getTextWidget().setDoubleClickEnabled(false);
		
		MySourceViewerConfiguration viewerConfiguration =
				new MySourceViewerConfiguration(); 
		
		viewer.configure(viewerConfiguration);
		viewer.setDocument(new Document("Hello I am the initial Content!")); 
	
		
		
		String[] str =  {"Ananas", "Apfel", "Banane", "Birne", "Kiwi", "Pflaume" };
		String[] str2 = {"besonders lecker", "schön grün", "wunderbar gebogen", 
		                 "irgendwie nicht rund", "exotisch", "hmm" };
		

	}
	
	class MySourceViewerConfiguration extends SourceViewerConfiguration {
		
	      public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
	          ContentAssistant ca = new ContentAssistant();
	          
	          IContentAssistProcessor cap = new SQLCompletionProcessor(); // <-- See reference to IContentAssistProcess implementation here
	          ca.setContentAssistProcessor(cap, IDocument.DEFAULT_CONTENT_TYPE);
	          ca.setInformationControlCreator(getInformationControlCreator(sourceViewer));
	          
	          // enable auto activation
	          ca.enableAutoActivation(true);
	          
	          
	          return ca;
	    }
		
	}


	public class SQLCompletionProcessor implements IContentAssistProcessor {

		 private String[] proposals = new String[] { "ID:", "Summary:", "Description:", "Done:", "Duedate:", "Dependent:" };

		    @Override
		    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {

		        IDocument document = viewer.getDocument();

		        try {
		            int lineOfOffset = document.getLineOfOffset(offset);
		            int lineOffset = document.getLineOffset(lineOfOffset);

		            // do not show any content assist in case the offset is not at the
		            // beginning of a line
		            if (offset != lineOffset) {
		                return new ICompletionProposal[0];
		            }
		        } catch (BadLocationException e) {
		            // ignore here and just continue
		        }

		        List<ICompletionProposal> completionProposals = new ArrayList<ICompletionProposal>();

		        for (String c : proposals) {
		            // Only add proposal if it is not already present
		            if (!(viewer.getDocument().get().contains(c))) {
		                completionProposals.add(new CompletionProposal(c, offset, 0, c.length()));
		            }
		        }

		        return completionProposals.toArray(new ICompletionProposal[completionProposals.size()]);
		    }

		    @Override
		    public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		        return null;
		    }

		    @Override
		    public char[] getCompletionProposalAutoActivationCharacters() {
		        return null;
		    }

		    @Override
		    public char[] getContextInformationAutoActivationCharacters() {
		        return null;
		    }

		    @Override
		    public String getErrorMessage() {
		        return null;
		    }

		    @Override
		    public IContextInformationValidator getContextInformationValidator() {
		        return null;
		    }

	
	}
	
}
