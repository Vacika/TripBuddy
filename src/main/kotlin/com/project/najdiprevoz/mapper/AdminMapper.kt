package com.project.najdiprevoz.mapper

import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.services.UserService
import com.project.najdiprevoz.web.request.UserGridFilterRequest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class AdminMapper(private val userService: UserService) {

    fun banUser(username: String) =
            userService.banUser(username)


    fun unbanUser(username: String) =
            userService.unbanUser(username)


    fun changeUserRole(username: String, role: String) =
            userService.changeUserRole(username, role)

    fun findAllUsersFiltered(req: UserGridFilterRequest) {
        val pageable: Pageable = PageRequest.of(req.page, req.pageSize)
//        return userService.findAllFiltered(username=req.username, phone=req.phone, pageable=pageable)
    }

}
