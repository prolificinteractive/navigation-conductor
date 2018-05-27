package com.prolificinteractive.conductornav.util

import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bluelinelabs.conductor.Controller

fun Controller.findNavController(): NavController = view!!.findNavController()