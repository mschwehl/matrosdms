package net.schwehla.matrosdms.domain.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import net.schwehla.matrosdms.rcp.MatrosServiceException;

public class ObjectCloner<T extends Serializable> {

	@SuppressWarnings("unchecked")
	public T cloneThroughSerialize(T t) throws MatrosServiceException {
		
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream() ) {
			serializeToOutputStream(t, bos);
			byte[] bytes = bos.toByteArray();
			
			try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
				return (T) ois.readObject();
			}
		} catch(Exception e) {
			throw new MatrosServiceException("Cannot serialize",e);
		}
		
			
	
	}

	private static void serializeToOutputStream(Serializable ser, OutputStream os) throws IOException {
		
		try (ObjectOutputStream oos = new ObjectOutputStream(os) ) {
			oos.writeObject(ser);
			oos.flush();
		}
		
		
	}

}
