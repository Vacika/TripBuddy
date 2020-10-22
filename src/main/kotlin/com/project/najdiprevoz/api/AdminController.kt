package com.project.najdiprevoz.api

import com.project.najdiprevoz.mapper.AdminService
import com.project.najdiprevoz.web.request.ChangeRoleRequest
import com.project.najdiprevoz.web.request.UserGridFilterRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/api/admin")
class AdminController(private val adminService: AdminService) {

    @PutMapping("/ban")
    fun banUser(@RequestBody username: String) = adminService.banUser(username)

    @PutMapping("/unban")
    fun unBanUser(@RequestBody username: String) = adminService.unbanUser(username)

    @PutMapping("/activate")
    fun activateUser(@RequestBody username: String) = adminService.activateUser(username)

    @PostMapping("/change-role")
    fun changeUserRole(@RequestBody req: ChangeRoleRequest) = adminService.changeUserRole(req.username, req.role)

    @GetMapping("/users/filter")
    fun fetchAllUsers(req: UserGridFilterRequest) = adminService.findAllUsersFiltered(req)

//    @GetMapping("/users/filter")
//    fun fetchAllUsers(req: UserGridFilterRequest) = adminService.findAllPaginated(req)
}