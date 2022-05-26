package org.openmrs.module.locationbasedaccess.web.controller;

import org.openmrs.module.locationbasedaccess.service.LocationBasedAccessHeuristicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LocationBasedAccessListController {

	private LocationBasedAccessHeuristicsService locationBasedAccessHeuristicsService;

	@Autowired
	public void setLocationBasedAccessHeuristicsService(
			LocationBasedAccessHeuristicsService locationBasedAccessHeuristicsService) {
		this.locationBasedAccessHeuristicsService = locationBasedAccessHeuristicsService;
	}

	@RequestMapping(value = { "/module/locationbasedaccess/locationbasedaccessList" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ModelAndView getGaacList() {
		ModelAndView modelAndView = new ModelAndView();

		modelAndView.addObject("userLocationAccessList",
				this.locationBasedAccessHeuristicsService.findAllUserLocationAccess());
		return modelAndView;
	}
}
