import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse(
    val problems: List<Map<String, List<Problem>>>
)

@JsonClass(generateAdapter = true)
data class Problem(
    val medications: List<Medication>?,
    val labs: List<Lab>?
)

@JsonClass(generateAdapter = true)
data class Medication(
    val medicationsClasses: List<MedicationClass>?
)

@JsonClass(generateAdapter = true)
data class MedicationClass(
    val className: List<ClassNameEntry>?,
    @Json(name = "className2") val className2: List<ClassNameEntry>?
)

@JsonClass(generateAdapter = true)
data class ClassNameEntry(
    val associatedDrug: List<Drug>?,
    @Json(name = "associatedDrug#2") val associatedDrug2: List<Drug>?
)

@JsonClass(generateAdapter = true)
data class Drug(
    val name: String?,
    val dose: String?,
    val strength: String?
)

@JsonClass(generateAdapter = true)
data class Lab(
    @Json(name = "missing_field")
    val missingField: String?
)
