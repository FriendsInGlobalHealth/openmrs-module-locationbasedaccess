package org.openmrs.module.locationbasedaccess;

import java.io.Serializable;
import java.util.List;

import org.openmrs.Location;
import org.openmrs.User;

public class UserLocationAccess implements Serializable {

	private static final long serialVersionUID = -3206341562433757213L;

	private User user;
	private List<Location> locations;

	public UserLocationAccess() {
	}

	public UserLocationAccess(User user, List<Location> locations) {
		this.user = user;
		this.locations = locations;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
}