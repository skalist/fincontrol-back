package com.fincontrol.service

import com.fincontrol.dto.IndustryListDto
import com.fincontrol.dto.IndustryUpsertDto
import com.fincontrol.exception.EntityNotFoundException
import com.fincontrol.model.Industry
import com.fincontrol.repository.IndustryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class IndustryService(
    private val authenticationFacade: AuthenticationFacade,
    private val industryRepository: IndustryRepository,
) {
    fun findAll(): List<IndustryListDto> {
        val userId = authenticationFacade.getUserId()
        return industryRepository.findAllByUserId(userId).map { IndustryListDto(it.id, it.name) }
    }

    fun findById(id: UUID) = industryRepository.findById(id)
        .orElseThrow { throw EntityNotFoundException(Industry::class.java.simpleName, id) }
        .let { IndustryUpsertDto(it.id, it.name) }

    @Transactional
    fun create(dto: IndustryUpsertDto): UUID {
        val userId = authenticationFacade.getUserId()
        val industry = Industry(userId = userId, name = dto.name)
        return industryRepository.save(industry).id
    }

    @Transactional
    fun update(dto: IndustryUpsertDto): UUID {
        val industry = industryRepository.findById(dto.id!!)
            .orElseThrow { throw EntityNotFoundException(Industry::class.java.simpleName, dto.id) }
        val copiedIndustry = industry.copy(name = dto.name)
        return industryRepository.save(copiedIndustry).id
    }

    @Transactional
    fun delete(id: UUID) {
        industryRepository.deleteById(id)
    }
}
