package com.project.najdiprevoz.api

import com.project.najdiprevoz.mapper.AdminMapper
import com.project.najdiprevoz.web.request.ChangeRoleRequest
import com.project.najdiprevoz.web.request.UserGridFilterRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
class AdminController(private val adminMapper: AdminMapper) {

    @PutMapping("/ban")
    fun banUser(username: String) = adminMapper.banUser(username)

    @PutMapping("/unban")
    fun unBanUser(username: String) = adminMapper.unbanUser(username)

    @PostMapping("/change-role")
    fun unBanUser(@RequestBody req: ChangeRoleRequest) = adminMapper.changeUserRole(req.username, req.role)

    fun fetchAllUsers(req: UserGridFilterRequest) = adminMapper.findAllUsersFiltered(req)

}