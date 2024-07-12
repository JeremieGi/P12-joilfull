package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme

/**
 * Composable qui s'affiche lors du chargement
 */

@Composable
fun LoadingScreen(modifier: Modifier = Modifier){

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = 100.dp
            ),
        //verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.chargement),
            modifier = modifier
        )

        CircularProgressIndicator(
            modifier = modifier.padding(
                top = 10.dp
            )
        )

    }

}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    JoilfullTheme {
        LoadingScreen()
    }
}
