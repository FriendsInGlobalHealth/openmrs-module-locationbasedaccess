package org.openmrs.module.locationbasedaccess.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openmrs.Location;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.locationbasedaccess.UserLocationAccess;
import org.openmrs.module.locationbasedaccess.service.LocationBasedAccessHeuristicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller(AssociateUserToLocationAccessController.CONTROLLER_NAME)
public class AssociateUserToLocationAccessController {

	public static final String CONTROLLER_NAME = "locationbasedaccess.associateUserToLocationAccess";

	private LocationBasedAccessHeuristicsService locationBasedAccessHeuristicsService;

	@Autowired
	public void setLocationBasedAccessHeuristicsService(
			LocationBasedAccessHeuristicsService locationBasedAccessHeuristicsService) {
		this.locationBasedAccessHeuristicsService = locationBasedAccessHeuristicsService;
	}

	@RequestMapping(value = "/module/locationbasedaccess/associateUserToLocationAccess", method = RequestMethod.GET)
	public ModelAndView showUserLocationAccessForm(HttpSession session, HttpServletRequest request, Model model,
			@ModelAttribute("userLocationAccess") UserLocationAccess userLocationAccess,
			@ModelAttribute("selectedLocationIDs") List<Integer> selectedLocationIDs,
			@ModelAttribute("locations") List<Location> locations) {

		ModelAndView modelAndView = new ModelAndView();

		userLocationAccess.setLocations(this.getChildLocations());

		session.setAttribute("userLocationAccess", userLocationAccess);
		session.setAttribute("selectedLocationIDs", selectedLocationIDs);
		session.removeAttribute("successfullyExecuted");

		session.setAttribute("UserWithAssociations", !this.locationBasedAccessHeuristicsService
				.findLocationAcessedByUser(userLocationAccess.getUser()).isEmpty());

		return modelAndView;
	}

	@RequestMapping(value = "/module/locationbasedaccess/removeAssociation")
	public ModelAndView removeAssociation(HttpSession session, ModelMap model,
			@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "action", required = false) String action) throws Exception {

		User user = Context.getUserService().getUser(userId);
		this.locationBasedAccessHeuristicsService.removeUserLocationAssociation(user);

		session.setAttribute("successfullyExecuted", true);

		return new ModelAndView("redirect:/module/locationbasedaccess/locationbasedaccessList.form");
	}

	@RequestMapping(value = "/module/locationbasedaccess/linkUserToLocation", method = RequestMethod.POST)
	protected ModelAndView onSubmit(HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		UserLocationAccess userLocationAccess = (UserLocationAccess) session.getAttribute("userLocationAccess");
		List<Location> selectedLocations = new ArrayList<Location>();

		String[] locationIds = request.getParameterValues("selectedLocationIDs");

		if (locationIds != null && locationIds.length > 0) {
			for (String locationId : locationIds) {
				selectedLocations.add(Context.getLocationService().getLocation(Integer.valueOf(locationId)));
			}
			this.locationBasedAccessHeuristicsService.associateUserToLocations(userLocationAccess.getUser(),
					selectedLocations);
			session.setAttribute("successfullyExecuted", true);

		} else {

		}
		return new ModelAndView("redirect:/module/locationbasedaccess/locationbasedaccessList.form");
	}

	@ModelAttribute("userLocationAccess")
	UserLocationAccess formBackingObject(@RequestParam(value = "userId", required = false) Integer userId) {
		if (userId != null) {

			User user = Context.getUserService().getUser(userId);

			UserLocationAccess userLocationAccess = new UserLocationAccess();

			userLocationAccess.setUser(user);

			return userLocationAccess;
		}
		UserLocationAccess userLocationAccess = new UserLocationAccess();

		return userLocationAccess;
	}

	@ModelAttribute("selectedLocationIDs")
	List<Integer> formBackingObject2(@RequestParam(value = "userId", required = false) Integer userId) {

		List<Integer> locationIDS = new ArrayList<Integer>();
		if (userId != null) {

			User user = Context.getUserService().getUser(userId);

			List<Location> selectedLocations = this.locationBasedAccessHeuristicsService
					.findLocationAcessedByUser(user);

			for (Location location : selectedLocations) {
				locationIDS.add(location.getId());
			}
		}
		return locationIDS;
	}

	@ModelAttribute("locations")
	public List<Location> getChildLocations() {
		return this.getLocations();
	}

	private List<Location> getLocations() {

		List<Location> locations = new ArrayList<Location>();
		List<Location> allLocations = Context.getLocationService().getAllLocations(false);
		for (Location location : allLocations) {
			if (location.getParentLocation() != null) {
				locations.add(location);
			}
		}
		return locations;
	}

}
