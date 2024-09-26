/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptime.ui.theme.BMICalculatorTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            BMICalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    BMICalculatorLayout()
                }
            }
        }
    }
}

@Composable
fun BMICalculatorLayout() {
    var heightInput by remember { mutableStateOf("")}
    var weightInput by remember { mutableStateOf("")}
    val height = heightInput.toDoubleOrNull()
    val weight = weightInput.toDoubleOrNull()
    val bmi: Double? = calculateBMI(height,weight)
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculate_bmi),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth(),
            label = R.string.height,
            value = heightInput,
            onValueChange = {heightInput = it}
        )
        EditNumberField(
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth(),
            label = R.string.weight,
            value = weightInput,
            onValueChange = {weightInput = it}
        )
        if (bmi != null) {
            Text(
                text = stringResource(R.string.bmi, bmi),
                style = MaterialTheme.typography.displaySmall
            )
            if (bmi < 16) {
                Text(text = "Severe Thinness")
            } else if (bmi < 17) {
                Text(text = "Moderate Thinness")
            } else if (bmi < 18.5) {
                Text(text = "Mild Thinness")
            } else if (bmi < 25) {
                Text(text = "Normal")
            } else if (bmi < 30) {
                Text(text = "Overweight")
            } else if (bmi < 35) {
                Text(text = "Obese Class I")
            } else if (bmi < 40) {
                Text(text = "Obese Class II")
            } else {
                Text(text = "Obese Class III")
            }
        }
        Button(onClick = {
            heightInput = ""
            weightInput = "" },
        ) { Text(text="Clear")}
        Spacer(modifier = Modifier.height(150.dp))
    }
}

private fun calculateBMI(height: Double?, weight: Double?): Double? {
    if(height == null || weight == null || height <= 0 || weight <=0){
        return null
    }
    val h = height/100
    val bmi = weight/(h*h)
    return bmi
}

@Preview(showBackground = true)
@Composable
fun BMICalculatorLayoutPreview() {
    BMICalculatorTheme {
        BMICalculatorLayout()
    }
}

@Composable
fun EditNumberField(modifier: Modifier = Modifier,
                    @StringRes label: Int,
                    value: String,
                    onValueChange: (String) -> Unit)
{
    TextField(
        value = value,
        onValueChange =  onValueChange,
        label = { Text(stringResource(label))},
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}
