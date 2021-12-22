package com.project.najdiprevoz.mapper

import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.services.UserService
import com.project.najdiprevoz.web.request.UserGridFilterRequest
import org.springframework.stereotype.Service

@Service
class AdminMapper(private val userService: UserService) {

    fun banUser(username: String) =
            userService.banUser(username)

    fun unbanUser(username: String) =
            userService.unbanUser(username)

    fun changeUserRole(username: String, role: String) =
            userService.changeUserRole(username, role)

    fun findAllUsersFiltered(req: UserGridFilterRequest): List<User> {
        return userService.findAllFiltered(req.username, req.phone)
    }

    fun activateUser(username: String) =
            userService.activateWithoutToken(username)

//    fun findAllPaginated(req: UserGridFilterRequest): Page<User> = with(req) {
//        val pageable = PageableUtils.getPageableWithDefaultSortById(page, pageSize, sortProperty, sortDirection)
//        return userService.findAllPaginated(req.username, req.phone, pageable)
//    }
}
