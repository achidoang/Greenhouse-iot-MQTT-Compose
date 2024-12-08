package com.kuliah.greenhouse_iot.presentation.screen.guide

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuliah.greenhouse_iot.data.model.guide.Guide
import com.kuliah.greenhouse_iot.data.model.guide.GuideStep
import com.kuliah.greenhouse_iot.presentation.viewmodel.guide.GuideViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideScreen(viewModel: GuideViewModel = hiltViewModel()) {
	val guides = viewModel.guides.collectAsState()
	val headColor = MaterialTheme.colorScheme.onSurface
	val textColor = MaterialTheme.colorScheme.onBackground
	val bgColor = MaterialTheme.colorScheme.background
	val secBgColor = MaterialTheme.colorScheme.tertiaryContainer

	LaunchedEffect(Unit) {
		viewModel.loadGuides()
	}

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Hydroponics Guide") },
				modifier = Modifier.height(50.dp), // Reduced height
				backgroundColor = MaterialTheme.colorScheme.background,
				contentColor = headColor
			)
		},
		containerColor = bgColor
	) { paddingValues ->
		LazyColumn(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
				.padding(horizontal = 16.dp)
				.padding(bottom = 90.dp, top = 10.dp)
		) {
			items(guides.value) { guide ->
				GuideItem(guide, headColor, textColor, secBgColor, bgColor)
				Spacer(modifier = Modifier.height(16.dp))
			}
		}
	}
}

@Composable
fun GuideItem(guide: Guide, headColor: Color, textColor: Color, secBgColor: Color, bgColor: Color) {
	Column(modifier = Modifier.fillMaxWidth()) {
		// Title & Description Card remains the same
		Card(
			modifier = Modifier.fillMaxWidth(),
			colors = CardDefaults.cardColors(containerColor = bgColor),
			elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
		) {
			Column(modifier = Modifier.padding(16.dp)) {
				// Title
				Text(
					text = guide.title,
					style = MaterialTheme.typography.titleLarge,
					color = headColor,
					fontWeight = FontWeight.Bold,
					textAlign = TextAlign.Start
				)

				Spacer(modifier = Modifier.height(8.dp))

				// Description
				Text(
					text = guide.description,
					style = MaterialTheme.typography.bodyMedium,
					color = textColor,
					textAlign = TextAlign.Justify

				)
			}
		}

		Spacer(modifier = Modifier.height(16.dp))

		// Improved Tools Section
		ToolsSection(
			tools = guide.tools,
			headColor = headColor,
			textColor = textColor,
			bgColor = bgColor
		)

		Spacer(modifier = Modifier.height(16.dp))

		// Improved Steps Section
		Card(
			modifier = Modifier.fillMaxWidth(),
			colors = CardDefaults.cardColors(containerColor = bgColor),
			elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
		) {
			Column(modifier = Modifier.padding(16.dp)) {
				Text(
					text = "Steps",
					style = MaterialTheme.typography.titleMedium,
					color = headColor,
					fontWeight = FontWeight.Bold,
					modifier = Modifier.padding(bottom = 8.dp)
				)

				guide.steps.forEachIndexed { index, step ->
					StepCard(
						step = step,
						index = index + 1,
						headColor = headColor,
						textColor = textColor,
						bgColor = secBgColor
					)
					if (index < guide.steps.size - 1) {
						Spacer(modifier = Modifier.height(8.dp))
					}
				}
			}
		}
	}
}

@Composable
fun ToolsSection(
	tools: List<String>,
	headColor: Color,
	textColor: Color,
	bgColor: Color
) {
	var expanded by remember { mutableStateOf(false) }
	val rotationState by animateFloatAsState(
		targetValue = if (expanded) 180f else 0f
	)

	Card(
		modifier = Modifier.fillMaxWidth(),
		colors = CardDefaults.cardColors(containerColor = bgColor),
		elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.clickable { expanded = !expanded },
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy(8.dp)
				) {
					Icon(
						imageVector = Icons.Default.Build,
						contentDescription = null,
						tint = headColor
					)
					Text(
						text = "Tools Required",
						style = MaterialTheme.typography.titleMedium,
						color = headColor,
						fontWeight = FontWeight.Bold
					)
				}
				Icon(
					imageVector = Icons.Default.ExpandMore,
					contentDescription = if (expanded) "Collapse" else "Expand",
					modifier = Modifier.rotate(rotationState),
					tint = headColor
				)
			}

			AnimatedVisibility(visible = expanded) {
				Column(
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 12.dp)
				) {
					tools.forEachIndexed { index, tool ->
						Row(
							modifier = Modifier
								.fillMaxWidth()
								.padding(vertical = 4.dp)
						) {
							Text(
								text = "${index + 1}.",
								style = MaterialTheme.typography.bodyMedium,
								color = textColor,
								modifier = Modifier.width(24.dp)
							)
							Text(
								text = tool,
								style = MaterialTheme.typography.bodyMedium,
								color = textColor,
								modifier = Modifier.padding(start = 8.dp),
								textAlign = TextAlign.Justify
							)
						}
					}
				}
			}
		}
	}
}

@Composable
fun StepCard(
	step: GuideStep,
	index: Int,
	headColor: Color,
	textColor: Color,
	bgColor: Color
) {
	var expanded by remember { mutableStateOf(false) }
	val rotationState by animateFloatAsState(
		targetValue = if (expanded) 180f else 0f
	)

	Card(
		modifier = Modifier.fillMaxWidth(),
		colors = CardDefaults.cardColors(containerColor = bgColor),
		elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.clickable { expanded = !expanded },
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy(8.dp)
				) {
					Surface(
						shape = CircleShape,
						color = headColor.copy(alpha = 0.1f),
						modifier = Modifier.size(32.dp)
					) {
						Box(
							contentAlignment = Alignment.Center,
							modifier = Modifier.fillMaxSize()
						) {
							Text(
								text = "$index",
								style = MaterialTheme.typography.titleMedium,
								color = headColor,
								fontWeight = FontWeight.Bold
							)
						}
					}
					Text(
						text = step.title,
						style = MaterialTheme.typography.titleMedium,
						color = headColor,
						fontWeight = FontWeight.Bold
					)
				}
				Icon(
					imageVector = Icons.Default.ExpandMore,
					contentDescription = if (expanded) "Collapse" else "Expand",
					modifier = Modifier.rotate(rotationState),
					tint = headColor
				)
			}

			AnimatedVisibility(visible = expanded) {
				Column(
					modifier = Modifier
						.fillMaxWidth()
						.padding(start = 40.dp, top = 12.dp)
				) {
					step.description.forEachIndexed { index, description ->
						Row(
							modifier = Modifier
								.fillMaxWidth()
								.padding(vertical = 4.dp)
						) {
							Text(
								text = "${index + 1}.",
								style = MaterialTheme.typography.bodyMedium,
								color = textColor,
								modifier = Modifier.width(24.dp)
							)
							Text(
								text = description,
								style = MaterialTheme.typography.bodyMedium,
								color = textColor,
								modifier = Modifier.padding(start = 8.dp),
								textAlign = TextAlign.Justify
							)
						}
					}
				}
			}
		}
	}
}