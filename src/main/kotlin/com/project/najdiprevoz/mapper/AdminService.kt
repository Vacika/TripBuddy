package com.project.najdiprevoz.mapper

import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.services.UserService
import com.project.najdiprevoz.utils.PageableUtils
import com.project.najdiprevoz.web.request.UserGridFilterRequest
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AdminService(private val userService: UserService) {

    fun banUser(username: String) =
            userService.banUser(username)

    fun unbanUser(username: String) =
            userService.unbanUser(username)

    fun changeUserRole(username: String, role: String) =
            userService.changeUserRole(username, role)

    fun findAllUsersFiltered(req: UserGridFilterRequest): List<User> {
        return userService.findAllUsersFiltered(req.username, req.phone)
    }

    fun activateUser(username: String) =
            userService.activateUserWithoutToken(username)

//    fun findAllPaginated(req: UserGridFilterRequest): Page<User> = with(req) {
//        val pageable = PageableUtils.getPageableWithDefaultSortById(page, pageSize, sortProperty, sortDirection)
//        return userService.findAllPaginated(req.username, req.phone, pageable)
//    }
}
