package net.schwehla.matrosdms.rcp.dnd;


import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

/**
 * Used for transferring an arbitary class within the application.
 * 
 * @author Mike, Martin
 */
public class GenericTransfer extends ByteArrayTransfer  {

	private final Class<?> _clazz;
	private final String[] _typeNames;
	private final int[] _typeIds;
	
	/**
	 * TODO: fix this (yuk! - only works within app)
	 */
	private Object _obj = null;
	
	public GenericTransfer( Class clazz ) {
		super();
		assert clazz != null;
		_clazz = clazz;
		_typeNames = new String[]{ clazz.getName() }; 
		_typeIds = new int[]{ registerType( clazz.getName() ) }; 
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#javaToNative(java.lang.Object, org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	protected void javaToNative(Object object, TransferData transferData) {
		if ( object == null || !isObjectValid( object, _clazz ) ) {
			DND.error(DND.ERROR_INVALID_DATA);
		}
		else {
			_obj = object;
			// use super functionality to set all low-level constructs used 
			// elsewhere in DnD framework
			super.javaToNative( new byte[1], transferData );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	public Object nativeToJava(TransferData transferData) {
		// use super functionality to set all low-level constructs used 
		// elsewhere in DnD framework
		super.nativeToJava( transferData );
		Object obj = null;
		if ( _obj != null ) {
			// clear object reference
			obj = _obj;
			_obj = null;
		}
		return obj;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
	 */
	protected String [] getTypeNames () {
		return _typeNames;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
	 */
	protected int [] getTypeIds () {
		return _typeIds;
	}
	
	/**
	 * Whether object to be dragged is valid for the transfer.
	 * <br>This default implementation checks that the object's class is
	 * the same or a subclass of the transferClass.
	 * @param object - will not be <code>null</code>;
	 * @param class defined in constructor - will not be <code>null</code>
	 * @return
	 */
	protected boolean isObjectValid( Object object, Class<?> transferClass ) {
		return  transferClass.isAssignableFrom( object.getClass() );
	}
}
