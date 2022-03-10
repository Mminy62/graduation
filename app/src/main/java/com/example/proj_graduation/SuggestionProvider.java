package com.example.proj_graduation;

import android.content.SearchRecentSuggestionsProvider;
import android.util.Log;

public class SuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITRY = "com.example.proj_graduation";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SuggestionProvider() {
        setupSuggestions(AUTHORITRY, MODE);
    }
}
