package com.example.healingyuk.data.api

object ApiService {

    const val BASE_URL = "http://10.0.2.2/healingyuk/api/"

    // Endpoint
    const val REGISTER = "${BASE_URL}auth/register.php"
    const val LOGIN = "${BASE_URL}auth/login.php"
    const val VALIDATE_TOKEN = "${BASE_URL}auth/validate_token.php"

    const val PROFILE = "${BASE_URL}user/profile.php"
    const val CHANGE_PASSWORD = "${BASE_URL}user/change_password.php"

    const val GET_PLACES = "${BASE_URL}places/get_all_places.php"
    const val GET_PLACE_BY_ID = "${BASE_URL}places/get_place_by_id.php"
    const val ADD_PLACE = "${BASE_URL}places/add_place.php"

    const val ADD_FAVORITE = "${BASE_URL}favorites/add_favorite.php"
    const val GET_FAVORITES = "${BASE_URL}favorites/get_favorites.php"
    const val REMOVE_FAVORITE = "${BASE_URL}favorites/remove_favorite.php"
}
