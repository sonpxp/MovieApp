package com.sonmob.movieapp.responses;

import com.google.gson.annotations.SerializedName;
import com.sonmob.movieapp.models.TVShowDetails;

public class TVShowDetailsResponse {

    @SerializedName("tvShow")
    private TVShowDetails tvShowDetails;

    public TVShowDetails getTVShowDetails() {
        return tvShowDetails;
    }

}
