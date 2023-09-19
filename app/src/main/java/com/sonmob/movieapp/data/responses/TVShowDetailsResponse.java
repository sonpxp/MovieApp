package com.sonmob.movieapp.data.responses;

import com.google.gson.annotations.SerializedName;
import com.sonmob.movieapp.data.models.TVShowDetails;

public class TVShowDetailsResponse {

    @SerializedName("tvShow")
    private TVShowDetails tvShowDetails;

    public TVShowDetails getTVShowDetails() {
        return tvShowDetails;
    }

}
