package net.schwehla.matrosdms.rcp.swt;

public interface TypedComboBoxLabelProvider<T> {

    public String getSelectedLabel(T element);

    public String getListLabel(T element);

}