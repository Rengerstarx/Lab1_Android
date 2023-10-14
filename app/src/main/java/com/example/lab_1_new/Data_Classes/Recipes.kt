package com.example.lab_1_new.Data_Classes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class Recipes: ViewModel() {
    val recipes : MutableLiveData<MutableList<Recipe_pars>> by lazy {
        MutableLiveData<MutableList<Recipe_pars>>()
    }
}