package net.schwehla.matrosdms.persistenceservice.entity.internal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="FileMetadata")

/**
 * Space for the physical files
 * 
 * @author Martin
 *
 */

@NamedQueries ( {
	@NamedQuery(name="DBItemMetadata.findByChecksum", query="SELECT c FROM DBItemMetadata c where c.sha256Original = :checksum") 
})




public class DBItemMetadata   {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO )
	@Column(unique=true,nullable=false, name="FILE_ID")
	private Long id;
	
	
	public Long getFileId() {
		return id;
	}

	public void setFileId(Long fileId) {
		this.id = fileId;
	}

	public String getSha256Original() {
		return sha256Original;
	}

	public void setSha256Original(String sha256Original) {
		this.sha256Original = sha256Original;
	}

	public String getSha256Crypted() {
		return sha256Crypted;
	}

	public void setSha256Crypted(String sha256Crypted) {
		this.sha256Crypted = sha256Crypted;
	}

	public String getCryptSettings() {
		return cryptSettings;
	}

	public void setCryptSettings(String cryptSettings) {
		this.cryptSettings = cryptSettings;
	}

	
	@Column(nullable=false,unique=false)
	Long filesize;
	
	@Column(nullable = false) 
	String filename;

	@Column(nullable=false,unique=false)
	String mimetype;

	// SHA shall not be equal for same file
	@Column(nullable=false,unique=true,updatable=false)
	String sha256Original;

	// SHA shall not be equal for same file
	@Column(nullable=false,unique=true,updatable=false)
	String sha256Crypted;
	
	@Column(nullable=false,unique=false)
	String cryptSettings;
	
	
	public Long getFilesize() {
		return filesize;
	}

	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}



}
