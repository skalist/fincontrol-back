package com.fincontrol.exception

import java.util.*

/**
 * Throw when entity doesn't exist
 */
class EntityNotFoundException(className: String, id: UUID): Exception("Entity $className with id: $id not found")
