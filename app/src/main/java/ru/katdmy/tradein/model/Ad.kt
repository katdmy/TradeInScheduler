import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ad(
    @SerialName("code")
    val code: Int? = null,
    @SerialName("mark")
    val mark: String? = null,
    @SerialName("model")
    val model: String? = null,
    @SerialName("vyear")
    val vyear: Int? = null,
    @SerialName("ref")
    val ref: String? = null,
    @SerialName("note")
    val note: String? = null,
    @SerialName("rejReason")
    val rejReason: String? = null
)