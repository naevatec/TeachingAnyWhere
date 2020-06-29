package com.naevatec.taw.backend.forum;

import com.naevatec.taw.backend.entry.Entry;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Forum implements Serializable{
	
	private static final long serialVersionUID = 6760455059440066123L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private boolean activated;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Entry> entries;
	
	public Forum(){}
	
	public Forum(boolean activated) {
		this.activated = activated;
		this.entries = new ArrayList<>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

}
