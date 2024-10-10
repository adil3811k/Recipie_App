import kotlinx.serialization.Serializable


sealed class Rout{

    @Serializable
    object Home

    @Serializable
    object Favorites

    @Serializable
    data class RecipeView(val id:String)

    @Serializable
    object Searcher

    @Serializable
    object Demo
}