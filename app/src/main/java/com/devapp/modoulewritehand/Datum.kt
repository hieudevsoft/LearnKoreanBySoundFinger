package mobi.eup.easykorean.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Dinh Sam Vu on 1/18/2021.
 */
data class Datum(

    @SerializedName("id")
    var id : Int,

    @SerializedName("news_id")
    var newsID : String,

    @SerializedName("content")
    var content : String,

    @SerializedName("news_like")
    var newsLike : Int,

    @SerializedName("news_dislike")
    var newsDislike : Int,

    @SerializedName("contrycode")
    var contrycode : String,

    @SerializedName("uuid")
    var uuid : String,

    @SerializedName("username")
    var username : String,

    @SerializedName("timestamp")
    var timestamp : String,

    @SerializedName("status")
    var status : Int

)
