package com.moon.pharm.benchmark

import android.content.Intent
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapTransitionBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun mapLoadTimeTest() = benchmarkRule.measureRepeated(
        packageName = "com.moon.pharm",
        metrics = listOf(StartupTimingMetric()),
        compilationMode = CompilationMode.Partial(),
        iterations = 5,
        startupMode = StartupMode.COLD,
        setupBlock = {
            device.executeShellCommand("pm grant com.moon.pharm android.permission.ACCESS_FINE_LOCATION")
            device.executeShellCommand("pm grant com.moon.pharm android.permission.ACCESS_COARSE_LOCATION")
            pressHome()
            device.waitForIdle()
        }
    ) {
        val context = InstrumentationRegistry.getInstrumentation().context
        val intent = context.packageManager.getLaunchIntentForPackage("com.moon.pharm")
        checkNotNull(intent) { "앱의 실행 Intent를 찾을 수 없습니다." }

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("IS_BENCHMARK_TEST", true)
        intent.putExtra("TARGET_SCREEN", "MAP")
        startActivityAndWait(intent)

        val isMapLoaded = device.wait(Until.hasObject(By.desc("map_view")), 10000)
        check(isMapLoaded == true) { "지도 화면으로 전환하는 데 실패했습니다." }
    }
}