package com.fincontrol.exception

import java.util.UUID

class EntityNotFoundException(className: String, id: UUID): Exception("Entity $className with id: $id not found")
