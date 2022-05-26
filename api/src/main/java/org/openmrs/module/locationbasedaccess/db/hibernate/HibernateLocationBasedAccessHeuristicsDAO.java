package org.openmrs.module.locationbasedaccess.db.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.locationbasedaccess.LocationBasedAccessConstants;
import org.openmrs.module.locationbasedaccess.UserLocationAccess;
import org.openmrs.module.locationbasedaccess.db.LocationBasedAccessHeuristicsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("locationbasedaccess.hibernateLocationBasedAccessHeuristicsDAO")
public class HibernateLocationBasedAccessHeuristicsDAO implements LocationBasedAccessHeuristicsDAO {

	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void linkPersonAttributeToLocation(User user, Patient patient, PersonAttribute personAttribute) {
		this.evictCache();

		@SuppressWarnings("rawtypes")
		List list = this.sessionFactory.getCurrentSession().createSQLQuery(String.format(
				"select * from person_attribute where person_id = %s and person_attribute_type_id = %s and value = '%s'",
				patient.getId(), personAttribute.getAttributeType().getPersonAttributeTypeId(),
				personAttribute.getValue())).list();

		if (list.isEmpty()) {
			String sqlInsert = "insert into person_attribute(person_id, value, person_attribute_type_id, creator, date_created, voided, uuid) values(%s, '%s', %s, %s, now(), %s, '%s' ) on duplicate key update person_attribute_type_id = 41 ";

			this.sessionFactory.getCurrentSession()
					.createSQLQuery(String.format(sqlInsert, patient.getId(), personAttribute.getValue(),
							personAttribute.getAttributeType().getPersonAttributeTypeId(), user.getUserId(), false,
							UUID.randomUUID().toString()))
					.executeUpdate();
			this.sessionFactory.getCurrentSession().flush();
			this.evictCache();
		}
	}

	private void evictCache() {
		Context.clearSession();
		Context.flushSession();
	}

	@Override
	public void linkUserToLocations(User user, List<Location> locations) {
		this.evictCache();

		Set<String> uuids = new TreeSet<String>();

		for (Location location : locations) {
			if (!uuids.contains(location.getUuid())) {
				uuids.add(location.getUuid());
			}
			if (location.getParentLocation() != null && !uuids.contains(location.getParentLocation().getUuid())) {
				uuids.add(location.getParentLocation().getUuid());
			}
		}

		String joinedUUIDs = StringUtils.join(uuids, ",");

		User foundUser = Context.getUserService().getUser(user.getId());

		foundUser.setUserProperty(LocationBasedAccessConstants.LOCATION_USER_PROPERTY_NAME, joinedUUIDs);

		this.sessionFactory.getCurrentSession().saveOrUpdate(foundUser);
		this.sessionFactory.getCurrentSession().flush();

	}

	@Override
	public List<Location> findLocationAccessedByIser(User user) {

		List<Location> locations = new ArrayList<Location>();

		User foundUser = Context.getUserService().getUser(user.getId());
		String locationUUIDS = foundUser.getUserProperty(LocationBasedAccessConstants.LOCATION_USER_PROPERTY_NAME);
		if (StringUtils.isNotEmpty(locationUUIDS)) {
			String[] split = StringUtils.split(locationUUIDS, ",");
			for (String locationUUID : split) {
				Location location = Context.getLocationService().getLocationByUuid(locationUUID);
				if (location != null && location.getParentLocation() != null) {
					locations.add(location);
				}
			}
		}
		return locations;
	}

	@Override
	public List<UserLocationAccess> findAllUserLocationAccess() {

		List<UserLocationAccess> result = new ArrayList<UserLocationAccess>();
		@SuppressWarnings("unchecked")
		List<Integer> list = this.sessionFactory.getCurrentSession()
				.createSQLQuery(String.format("select user_id from user_property where property = '%s'",
						LocationBasedAccessConstants.LOCATION_USER_PROPERTY_NAME))
				.list();

		for (Integer userId : list) {
			User foundUser = Context.getUserService().getUser(userId);
			List<Location> locations = this.findLocationAccessedByIser(foundUser);
			result.add(new UserLocationAccess(foundUser, locations));
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersonAttribute> findAllAccessiblePersonosByLocations(List<String> locationUUIDs) {

		String uuids = StringUtils.EMPTY;
		for (String uuid : locationUUIDs) {
			uuids += "'" + uuid + "',";
		}
		String parameter = StringUtils.removeEnd(uuids, ",");

		return this.sessionFactory.getCurrentSession().createSQLQuery(String.format(
				"select * from person_attribute where voided is false and person_attribute_id = 41 and value in (%s)",
				parameter)).addEntity(PersonAttribute.class).list();
	}

	@Override
	public void removeUserLocationAssociation(User user) {
		this.sessionFactory.getCurrentSession()
				.createSQLQuery(String.format("delete from user_property where user_id = %s and property = '%s'",
						user.getUserId(), LocationBasedAccessConstants.LOCATION_USER_PROPERTY_NAME))
				.executeUpdate();
		this.sessionFactory.getCurrentSession().flush();
		this.evictCache();
	}

	@Override
	public List<String> findAllChildLocationUUIDs() {
		return this.sessionFactory.getCurrentSession()
				.createSQLQuery("select uuid from location where retired  is false and parent_location is not null")
				.list();
	}
}
