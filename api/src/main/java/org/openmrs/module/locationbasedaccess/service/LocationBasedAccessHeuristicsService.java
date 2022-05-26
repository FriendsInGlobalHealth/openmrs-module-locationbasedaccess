package org.openmrs.module.locationbasedaccess.service;

import java.util.List;

import org.openmrs.Location;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.locationbasedaccess.UserLocationAccess;

public interface LocationBasedAccessHeuristicsService extends OpenmrsService {

	void associateUserToLocations(User user, List<Location> locations) throws APIException;

	List<Location> findLocationAcessedByUser(User user);

	List<UserLocationAccess> findAllUserLocationAccess();

	void removeUserLocationAssociation(User user);
}
