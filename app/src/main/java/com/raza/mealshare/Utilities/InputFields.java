package com.raza.mealshare.Utilities;

import android.view.View;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

public class InputFields {
    public InputFields() {
    }

    public String getTextFromIdFIET(@NotNull View view, int id){
        TextInputEditText inputEditText=view.findViewById(id);
        return inputEditText.getText().toString();
    }
    public String getTextFromIdFAuto(@NotNull View view, int id){
        AutoCompleteTextView inputEditText=view.findViewById(id);
        return inputEditText.getText().toString();
    }
    public String getTextFromIdAIET(@NotNull TextInputEditText inputEditText){
        return inputEditText.getText().toString();
    }
    public String getHintFromIdAIET(@NotNull TextInputEditText inputEditText){
        return inputEditText.getHint().toString();
    }
    public String getTextFromIdAAuto(@NotNull AutoCompleteTextView autoCompleteTextView){
        return autoCompleteTextView.getText().toString();
    }
    public String getHintFromIdFAuto(@NotNull View view, int id){
        TextInputEditText inputEditText=view.findViewById(id);
        return inputEditText.getHint().toString();
    }
}
