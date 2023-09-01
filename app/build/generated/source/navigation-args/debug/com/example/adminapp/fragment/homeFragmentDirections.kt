package com.example.adminapp.fragment

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.adminapp.R

public class homeFragmentDirections private constructor() {
  public companion object {
    public fun actionHomeFragmentToCategoryFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_homeFragment_to_categoryFragment)

    public fun actionHomeFragmentToProductFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_homeFragment_to_productFragment)

    public fun actionHomeFragmentToLiderFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_homeFragment_to_liderFragment)
  }
}
