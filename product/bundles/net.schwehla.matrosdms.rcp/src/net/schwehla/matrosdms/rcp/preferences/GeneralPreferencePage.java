package net.schwehla.matrosdms.rcp.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;

import net.schwehla.matrosdms.rcp.MyGlobalConstants;

// @Creatable
// widget is disposed
public class GeneralPreferencePage extends FieldEditorPreferencePage
{

    public GeneralPreferencePage()
    {
        super(GRID);
        setTitle("GENERAL");
    }

    public void createFieldEditors()
    {
        addField(new StringFieldEditor(MyGlobalConstants.Preferences.EXPORT_PATH, //
                        "ExportPath", getFieldEditorParent()));
    }
    
    
    
}
