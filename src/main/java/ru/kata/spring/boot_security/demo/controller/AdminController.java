package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import java.util.Set;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserServiceImpl userServiceImpl;
    private final RoleServiceImpl roleServiceImpl;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
    }

    @GetMapping()
    public String getAdminPage(Model model) {
        model.addAttribute("allUsers", userServiceImpl.getAllUsers());
        return "adminPage";
    }

    @GetMapping("/add_user")
    public String getNewUserForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleServiceImpl.getAllRoles());
        return "new";
    }

    @PostMapping("/add_user")
    public String addUser(@ModelAttribute("user") User user, @RequestParam("roles") Set<Long> roleIds) {
        Set<Role> roles = roleServiceImpl.findDyIds(roleIds);

        user.setRoles(roles);
        userServiceImpl.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/show_user")
    public String showSingleUser(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("user", userServiceImpl.findUserById(id));
        return "user";
    }

    @GetMapping("/edit_user")
    public String getUserEditForm(@RequestParam("id") Long id, Model model) {
        User user = userServiceImpl.findUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleServiceImpl.getAllRoles());
        return "edit";
    }

    @PostMapping("/{id}/edit_user")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam("roles") Set<Long> roleIds,
                             @PathVariable("id") Long id) {
        Set<Role> roles = roleServiceImpl.findDyIds(roleIds);
        user.setRoles(roles);
        user.setId(id);
        userServiceImpl.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/delete_user")
    public String deleteUserById(@RequestParam("id") Long id) {
        userServiceImpl.delete(id);
        return "redirect:/admin";
    }
}
