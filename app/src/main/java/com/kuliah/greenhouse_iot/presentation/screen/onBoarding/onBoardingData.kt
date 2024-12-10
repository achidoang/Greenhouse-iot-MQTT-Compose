package com.kuliah.greenhouse_iot.presentation.screen.onBoarding

import androidx.compose.ui.graphics.Color
import com.kuliah.greenhouse_iot.R

data class OnBoardingData(
	val image: Int, val title: String,
	val desc: String,
	val backgroundColor: Color,
	val mainColor:Color
)

// Data Onboarding
val pages = listOf(
	OnBoardingData(
		title = "Lorem ipsum is simply dummy",
		desc = "Welcome to GymToolKit, the app that will guide you on a deep and inspiring fitness journey; get ready to achieve the healthy and strong body you've always dreamed of",
		image = R.drawable.onboarding1,
		backgroundColor = TODO(),
		mainColor = TODO() //image onboarding
	),
	OnBoardingData(
		title = "Lorem ipsum is simply dummy",
		desc = "In GymToolKit, we provide unlimited access to the best fitness resources, expert training, and a supportive community, making every step of your journey towards health and fitness easier and more meaningful",
		image = R.drawable.onboarding2,
		backgroundColor = TODO(),
		mainColor = TODO() //image onboarding
	),
	OnBoardingData(
		title = "Lorem ipsum is simply dummy",
		desc = "With our innovative features, you can track gym equipment usage, practice with proper techniques, and maintain consistency in your fitness journey. Embrace change, welcome health.",
		image = R.drawable.onboarding3,
		backgroundColor = TODO(),
		mainColor = TODO() //image onboarding
	)
)