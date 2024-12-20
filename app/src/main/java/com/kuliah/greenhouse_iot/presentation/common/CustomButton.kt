package com.kuliah.greenhouse_iot.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	textColor: Color = MaterialTheme.colorScheme.onBackground,
	backgroundColor: Color = MaterialTheme.colorScheme.primary,
	borderColor: Color? = null,
	icon: ImageVector? = null,
	enabled: Boolean = true
) {
	Button(
		onClick = onClick,
		enabled = enabled,
		colors = ButtonDefaults.buttonColors(
			containerColor = backgroundColor,
			contentColor = textColor,
			disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
			disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
		),
		border = borderColor?.let { BorderStroke(1.dp, it) },
		modifier = modifier
			.height(48.dp)
			.padding(horizontal = 8.dp),
		shape = MaterialTheme.shapes.small
	) {
		if (icon != null) {
			Icon(icon, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
		}
		Text(
			text = if (text.isNotEmpty()) text else "Select Date",
			style = MaterialTheme.typography.labelLarge,
			maxLines = 1,
			overflow = TextOverflow.Ellipsis
		)
	}
}