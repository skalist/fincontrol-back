import java.util.*

data class OperationCategoryListDto(
    val id: UUID,
    val name: String,
)


data class OperationCategoryUpsertDto(
    val id: UUID?,
    val name: String,
)
