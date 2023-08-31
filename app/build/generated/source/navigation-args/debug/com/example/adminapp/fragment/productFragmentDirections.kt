package com.example.adminapp.fragment

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.adminapp.R

public class productFragmentDirections private constructor() {
  public companion object {
    public fun actionProductFragmentToAddProductFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_productFragment_to_addProductFragment)
  }
}
