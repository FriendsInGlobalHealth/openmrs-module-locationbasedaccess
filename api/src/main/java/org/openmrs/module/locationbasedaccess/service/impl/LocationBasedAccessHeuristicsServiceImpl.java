package org.openmrs.module.locationbasedaccess.service.impl;

import java.util.List;

import org.openmrs.Location;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.locationbasedaccess.UserLocationAccess;
import org.openmrs.module.locationbasedaccess.db.LocationBasedAccessHeuristicsDAO;
import org.openmrs.module.locationbasedaccess.service.LocationBasedAccessHeuristicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service(LocationBasedAccessHeuristicsServiceImpl.BEAN_NAME)
public class LocationBasedAccessHeuristicsServiceImpl extends BaseOpenmrsService
		implements LocationBasedAccessHeuristicsService {
	public static final String BEAN_NAME = "locationbasedaccess.locationBasedAccessHeuristicsService";

	private LocationBasedAccessHeuristicsDAO heuristicsDAO;;

	@Autowired
	public void setLocationBasedAccessHeuristicsDAO(LocationBasedAccessHeuristicsDAO heuristicsDAO) {
		this.heuristicsDAO = heuristicsDAO;
	}

	@Override
	public void associateUserToLocations(User user, List<Location> locations) throws APIException {
		this.heuristicsDAO.linkUserToLocations(user, locations);
	}

	@Override
	public List<Location> findLocationAcessedByUser(User user) {
		return heuristicsDAO.findLocationAccessedByIser(user);
	}

	@Override
	public List<UserLocationAccess> findAllUserLocationAccess() {
		return this.heuristicsDAO.findAllUserLocationAccess();
	}

	@Override
	public void removeUserLocationAssociation(User user) {
		this.heuristicsDAO.removeUserLocationAssociation(user);
	}
}
