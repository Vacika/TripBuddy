package com.project.najdiprevoz.api

import com.project.najdiprevoz.mapper.AdminMapper
import com.project.najdiprevoz.web.request.ChangeRoleRequest
import com.project.najdiprevoz.web.request.UserGridFilterRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/api/admin")
class AdminController(private val adminMapper: AdminMapper) {

    @PutMapping("/ban")
    fun banUser(@RequestBody username: String) = adminMapper.banUser(username)

    @PutMapping("/unban")
    fun unBanUser(@RequestBody username: String) = adminMapper.unbanUser(username)

    @PutMapping("/activate")
    fun activateUser(@RequestBody username: String) = adminMapper.activateUser(username)

    @PostMapping("/change-role")
    fun changeUserRole(@RequestBody req: ChangeRoleRequest) = adminMapper.changeUserRole(req.username, req.role)

    @GetMapping("/users/filter")
    fun fetchAllUsers(req: UserGridFilterRequest) = adminMapper.findAllUsersFiltered(req)

//    @GetMapping("/users/filter")
//    fun fetchAllUsers(req: UserGridFilterRequest) = adminService.findAllPaginated(req)
}