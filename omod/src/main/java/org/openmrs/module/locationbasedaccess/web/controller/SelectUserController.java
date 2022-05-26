package org.openmrs.module.locationbasedaccess.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SelectUserController {

	@RequestMapping(value = "/module/locationbasedaccess/selectUser")
	public String displayUsers(ModelMap model, @RequestParam(value = "action", required = false) String action,
			@RequestParam(value = "name", required = false) String name) throws Exception {

		if (Context.isAuthenticated()) {
			List<User> users = this.getUsers(action, name);
			Map<User, Set<Role>> userRolesMap = new HashMap<User, Set<Role>>(users.size());

			Map<User, Set<Role>> userInheritanceLineMap = new HashMap<User, Set<Role>>(users.size());
			Set<Role> inheritanceLineRoles = new LinkedHashSet<Role>();
			List<Role> helpList = new ArrayList<Role>();
			Role role = null;

			for (User user : users) {
				Set<Role> roles = new LinkedHashSet<Role>();
				if (role != null && !user.getRoles().contains(role)) {
					// condition -> user has role only via inheritance
					inheritanceLineRoles.add(role);
					for (Role r : user.getRoles()) {
						if (r.getAllParentRoles().contains(role)) {
							// condition -> r = role that inherits from filtered role
							roles.add(r);
							helpList.addAll(role.getChildRoles());
							Role r2;
							for (int i = 0; i < helpList.size(); i++) {
								r2 = helpList.get(i);
								if (r2.getAllChildRoles().contains(r)) {
									// condition -> finding first child role that contains role, which inherits from
									// filtered role
									inheritanceLineRoles.add(r2);
									helpList.clear();
									helpList.addAll(r2.getAllChildRoles());
									i = -1;
								} else if (r2.equals(r)) {
									inheritanceLineRoles.add(r2);
									break;
								}
							}
						}
					}
					userInheritanceLineMap.put(user, inheritanceLineRoles);
				} else if (role != null && user.getRoles().contains(role)) {
					// adding searched role on the first place for simplicity of dealing with it in
					// JSTL
					roles.add(role);
				}

				roles.addAll(user.getRoles());
				userRolesMap.put(user, roles);
			}

			model.put("users", users);
			model.put("role", role);
			model.put("userInheritanceLineMap", userInheritanceLineMap);
			model.put("userRolesMap", userRolesMap);

		}
		return "module/locationbasedaccess/selectUser";
	}

	protected List<User> getUsers(String action, String name) {
		if (action != null || StringUtils.hasText(name)) {

			if (!StringUtils.hasText(name)) {
				name = null;
			}
			List<User> users = Context.getUserService().getUsers(name, null, false);

			for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
				User user = (User) iterator.next();
				if (user.isSuperUser()) {
					iterator.remove();
				}
			}
			return users;
		}
		return new ArrayList<User>();

	}
}
