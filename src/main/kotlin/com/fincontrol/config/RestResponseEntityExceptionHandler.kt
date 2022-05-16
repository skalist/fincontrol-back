package com.fincontrol.config

import com.fincontrol.exception.EntityNotFoundException
import mu.KotlinLogging
import org.hibernate.exception.ConstraintViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * Controller exception handler
 * Handle all standard exceptions in project
 */
@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    private val log = KotlinLogging.logger {  }

    /**
     * Handle EntityNotFoundException
     */
    @ExceptionHandler(value = [EntityNotFoundException::class])
    fun handleNotFound(ex: Exception, request: WebRequest): ResponseEntity<Any?> {
        val message = "Entity not found"
        log.error(ex){ message }
        return handleExceptionInternal(ex, message, HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    /**
     * Handle ConstraintViolationException
     */
    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun handleConflict(ex: Exception, request: WebRequest): ResponseEntity<Any?> {
        val message = "Request can't be performed"
        log.error(ex){ message }
        return handleExceptionInternal(ex, message, HttpHeaders(), HttpStatus.CONFLICT, request)
    }
}
