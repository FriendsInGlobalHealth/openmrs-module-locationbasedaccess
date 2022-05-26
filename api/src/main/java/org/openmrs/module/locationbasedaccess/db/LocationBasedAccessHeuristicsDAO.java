package org.openmrs.module.locationbasedaccess.db;

import java.util.List;

import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.User;
import org.openmrs.module.locationbasedaccess.UserLocationAccess;

public interface LocationBasedAccessHeuristicsDAO {

	void linkPersonAttributeToLocation(User user, Patient patient, PersonAttribute personAttribute);

	void linkUserToLocations(User user, List<Location> locations);

	List<Location> findLocationAccessedByIser(User user);

	List<UserLocationAccess> findAllUserLocationAccess();

	List<PersonAttribute> findAllAccessiblePersonosByLocations(List<String> locationUUIDs);

	void removeUserLocationAssociation(User user);

	List<String> findAllChildLocationUUIDs();

}
