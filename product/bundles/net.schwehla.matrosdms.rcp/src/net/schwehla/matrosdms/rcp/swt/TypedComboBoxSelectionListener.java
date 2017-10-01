package net.schwehla.matrosdms.rcp.swt;

public interface TypedComboBoxSelectionListener<T> {

    public void selectionChanged(TypedComboBox<T> typedComboBox, T newSelection);
}