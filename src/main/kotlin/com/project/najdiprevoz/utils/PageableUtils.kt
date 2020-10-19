package com.project.najdiprevoz.utils

import org.springframework.data.domain.*

/**
 * Utility class for Pageable Grids.
 */
object PageableUtils {
    @Deprecated("")
    fun getPageable(page: Int, pageSize: Int, sortProperty: String?, sortDirection: Sort.Direction?): Pageable {
        return if (sortProperty != null) {
            val sort = Sort.by(sortDirection, sortProperty)
            PageRequest.of(page - 1, pageSize, sort)
        } else {
            PageRequest.of(page - 1, pageSize)
        }
    }

    /*
     *  Maintains the order of the rows after updates if there is no sort property set,
     *  so that the row doesn't appear at the bottom of the grid after changes.
     *  Sorts by id
     * */
    fun getPageableWithDefaultSortById(page: Int, pageSize: Int, sortProperty: String?,
                                       sortDirection: Sort.Direction?): Pageable {
        return if (sortProperty != null) {
            val sort = Sort.by(sortDirection, sortProperty)
            PageRequest.of(page - 1, pageSize, sort)
        } else {
            val sort = Sort.by(Sort.Direction.ASC, "id")
            PageRequest.of(page - 1, pageSize, sort)
        }
    }

    /*
     *  Maintains the order of the rows after updates if there is no sort property set,
     *  so that the row doesn't appear at the bottom of the grid after changes.
     *  Sorts by dateLastModified
     * */
    fun getPageableWithDefaultSortByDateLastModifiedDesc(page: Int, pageSize: Int, sortProperty: String?,
                                                         sortDirection: Sort.Direction?): Pageable {
        return if (sortProperty != null) {
            val sort = Sort.by(sortDirection, sortProperty)
            PageRequest.of(page - 1, pageSize, sort)
        } else {
            val sort = Sort.by(Sort.Direction.DESC, "dateLastModified")
            PageRequest.of(page - 1, pageSize, sort)
        }
    }

    fun getPageableWithDefaultSortByProperty(page: Int, pageSize: Int, sortProperty: String?,
                                             sortDirection: Sort.Direction?, property: String?): Pageable {
        return if (sortProperty != null) {
            val sort = Sort.by(sortDirection, sortProperty)
            PageRequest.of(page - 1, pageSize, sort)
        } else {
            val sort = Sort.by(Sort.Direction.ASC, property)
            PageRequest.of(page - 1, pageSize, sort)
        }
    }

    /*
     *  Maintains the order of the rows after updates if there is no sort property set,
     *  so that the row doesn't appear at the bottom of the grid after changes.
     *  Sorts by dateCreated
     * */
    fun getPageableWithDefaultSortByDateCreatedDesc(page: Int, pageSize: Int, sortProperty: String?,
                                                    sortDirection: Sort.Direction?): Pageable {
        return if (sortProperty != null) {
            val sort = Sort.by(sortDirection, sortProperty)
            PageRequest.of(page - 1, pageSize, sort)
        } else {
            val sort = Sort.by(Sort.Direction.DESC, "dateCreated")
            PageRequest.of(page - 1, pageSize, sort)
        }
    }

    fun <T, U> createPageResponse(originalPage: Page<U>, transformedList: List<T>?, pageable: Pageable?): Page<T> {
        return PageImpl(transformedList, pageable, originalPage.totalElements)
    }

    fun <T> createPageResponse(data: List<T>?, totalElements: Long, pageable: Pageable?): Page<T> {
        return PageImpl(data, pageable, totalElements)
    }
}