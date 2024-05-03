package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import java.util.Set;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl) {
        this.userService = userServiceImpl;
        this.roleService = roleServiceImpl;
    }

    @GetMapping()
    public String getAdminPage(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        return "adminPage";
    }

    @GetMapping("/add_user")
    public String getNewUserForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "new";
    }

    @PostMapping("/add_user")
    public String addUser(@ModelAttribute("user") User user, @RequestParam("roles") Set<Long> roleIds) {
        Set<Role> roles = roleService.findDyIds(roleIds);

        user.setRoles(roles);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/show_user")
    public String showSingleUser(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "user";
    }

    @GetMapping("/edit_user")
    public String getUserEditForm(@RequestParam("id") Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "edit";
    }

    @PostMapping("/{id}/edit_user")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam("roles") Set<Long> roleIds,
                             @PathVariable("id") Long id) {
        Set<Role> roles = roleService.findDyIds(roleIds);
        user.setRoles(roles);
        user.setId(id);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete_user")
    public String deleteUserById(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
