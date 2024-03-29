package com.rocqjones.dvt.weatherapp.ui.design.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.rocqjones.dvt.weatherapp.R
import com.rocqjones.dvt.weatherapp.configs.BaseAppConfig
import com.rocqjones.dvt.weatherapp.logic.models.entities.CurrentWeatherModel
import com.rocqjones.dvt.weatherapp.logic.models.entities.ForecastWeatherModel
import com.rocqjones.dvt.weatherapp.logic.models.sealed.Screen
import com.rocqjones.dvt.weatherapp.logic.utils.DataFormatUtil
import com.rocqjones.dvt.weatherapp.logic.utils.HelperUtil
import com.rocqjones.dvt.weatherapp.logic.vm.CurrentViewModelFactory
import com.rocqjones.dvt.weatherapp.logic.vm.ForecastViewModelFactory
import com.rocqjones.dvt.weatherapp.logic.vm.ViewModelCurrent
import com.rocqjones.dvt.weatherapp.logic.vm.ViewModelForecast
import com.rocqjones.dvt.weatherapp.ui.theme.sunnyBg

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current

    val viewModelCurrent: ViewModelCurrent = viewModel(
        factory = CurrentViewModelFactory(
            (context.applicationContext as BaseAppConfig).currentRepository
        )
    )

    val viewModelForecast: ViewModelForecast = viewModel(
        factory = ForecastViewModelFactory(
            (context.applicationContext as BaseAppConfig).forecastRepository
        )
    )

    // Observe the LiveData and convert it to a state
    val dataCurrent by viewModelCurrent.getAllCurrentWeather.observeAsState(initial = emptyList())
    val dataForecast by viewModelForecast.getAllForecastWeather.observeAsState(initial = emptyList())

    var bgColor: Color by remember { mutableStateOf(sunnyBg) }
    var bgDrawable: Int by remember { mutableStateOf(R.drawable.forest_sunny) }
    if (dataCurrent.toMutableList().isNotEmpty()) {
        val it = dataCurrent[0]
        bgColor = HelperUtil.getBgColor(it.weatherMain)
        bgDrawable = HelperUtil.getBgDrawable(it.weatherMain)
    }

    when {
        !HelperUtil.isConnectedToInternet(context) -> {
            Toast.makeText(
                context,
                stringResource(R.string.you_re_currently_in_offline_mode),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CurrentContentView(
            dataCurrent,
            bgDrawable,
            navController,
            modifier = Modifier.weight(1f)
        )
        CurrentTempContentView(
            dataCurrent,
            bgColor,
            modifier = Modifier.weight(0.2f)
        )
        ForecastContentView(
            dataForecast,
            bgColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CurrentContentView(
    data: List<CurrentWeatherModel>,
    bgDrawable: Int,
    navController: NavHostController,
    modifier: Modifier
) {
    if (data.toMutableList().isNotEmpty()) {
        val it = data[0]
        Log.d("loadCurrentObj", "$it")

        Box(
            modifier = modifier
                .fillMaxSize()
                .paint(
                    painterResource(id = bgDrawable),
                    contentScale = ContentScale.FillBounds
                ),
            contentAlignment = Alignment.Center
        ) {
            // Toolbar at the top, fill width
            Toolbar(
                navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val tempC = "${DataFormatUtil.convertKelvinToCelsius(it.temperature ?: 0.0)}℃"
                val description = (it.weatherMain ?: "").uppercase()

                Text(
                    text = it.locationName ?: "",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                )

                Text(
                    text = tempC,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun Toolbar(navController: NavHostController, modifier: Modifier) {
    val context = LocalContext.current
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween // Spread images to ends
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_add_location_alt_24),
                contentDescription = stringResource(R.string.icon),
                contentScale = ContentScale.None,
                modifier = Modifier
                    .padding(26.dp)
                    .clickable { /* Go to search places */
                        when {
                            HelperUtil.isConnectedToInternet(context) -> {
                                navController.navigate(Screen.SearchPlacesScreen.route)
                            }

                            else -> {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.to_you_this_feature_connect_to_internet),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.baseline_bookmarks_24),
                contentDescription = stringResource(R.string.icon),
                contentScale = ContentScale.None,
                modifier = Modifier
                    .padding(26.dp)
                    .clickable { /* Go to favourites */
                        when {
                            HelperUtil.isConnectedToInternet(context) -> {
                                navController.navigate(Screen.FavouritePlacesScreen.route)
                            }

                            else -> {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.to_you_this_feature_connect_to_internet),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            )
        }
    }
}

@Composable
fun CurrentTempContentView(
    dataCurrent: List<CurrentWeatherModel>,
    bgColor: Color,
    modifier: Modifier
) {
    if (dataCurrent.toMutableList().isNotEmpty()) {
        val it = dataCurrent[0]
        Log.d("loadCurrentObj", "$it")

        Column(
            modifier = modifier.background(bgColor)
        ) {
            // Temp
            val minTemp = "${DataFormatUtil.convertKelvinToCelsius(it.temp_min ?: 0.0)}℃"
            val currentTemp = "${DataFormatUtil.convertKelvinToCelsius(it.temperature ?: 0.0)}℃"
            val maxTemp = "${DataFormatUtil.convertKelvinToCelsius(it.temp_max ?: 0.0)}℃"

            Spacer(modifier = Modifier.padding(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = minTemp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = currentTemp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = maxTemp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.padding(2.dp))

            // Desc
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Min",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Current",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Max",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.padding(4.dp))
            Divider()
        }
    }
}

@Composable
fun ForecastContentView(
    dataForecastList: List<ForecastWeatherModel>,
    bgColor: Color,
    modifier: Modifier
) {

    if (dataForecastList.toMutableList().isNotEmpty()) {
        Log.d("loadForecastObj", "$dataForecastList")
        LazyColumn(
            modifier = modifier.background(bgColor),
        ) {
            items(dataForecastList) { item ->
                // setIcon
                val bgIcon: Int by remember {
                    mutableStateOf(HelperUtil.getBgIcon(item.weatherMain))
                }

                ListRowView(
                    dayString = DataFormatUtil.dateTimeConverter(item.forecastDate.toString()),
                    icon = bgIcon,
                    tempString = "${DataFormatUtil.convertKelvinToCelsius(item.temperature ?: 0.0)} ℃",
                )
            }
        }
    }
}

@Composable
private fun ListRowView(
    dayString: String,
    icon: Int,
    tempString: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = dayString,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            textAlign = TextAlign.Start
        )

        // icon
        Image(
            painter = painterResource(id = icon),
            contentDescription = stringResource(R.string.icon),
            contentScale = ContentScale.None,
            modifier = Modifier.weight(1f),
            alignment = Alignment.Center,
        )

        Text(
            text = tempString,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
    }

    Spacer(modifier = Modifier.padding(4.dp))
}