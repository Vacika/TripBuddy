package com.project.najdiprevoz.api

import com.project.najdiprevoz.mapper.AdminService
import com.project.najdiprevoz.web.request.ChangeRoleRequest
import com.project.najdiprevoz.web.request.UserGridFilterRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
class AdminController(private val adminService: AdminService) {

    @PutMapping("/ban")
    fun banUser(username: String) = adminService.banUser(username)

    @PutMapping("/unban")
    fun unBanUser(username: String) = adminService.unbanUser(username)

    @PostMapping("/change-role")
    fun unBanUser(@RequestBody req: ChangeRoleRequest) = adminService.changeUserRole(req.username, req.role)

    @GetMapping("/users/filter")
    fun fetchAllUsers(req: UserGridFilterRequest) = adminService.findAllUsersFiltered(req)
}