package com.moon.pharm.benchmark

import android.content.Intent
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
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
    fun mapTransitionTest() {
        var targetButtonToClick: androidx.test.uiautomator.UiObject2? = null

        benchmarkRule.measureRepeated(
            packageName = "com.moon.pharm",
            metrics = listOf(FrameTimingMetric()),
            compilationMode = CompilationMode.Partial(),
            iterations = 5,
            setupBlock = {
                device.executeShellCommand("pm grant com.moon.pharm android.permission.ACCESS_FINE_LOCATION")
                device.executeShellCommand("pm grant com.moon.pharm android.permission.ACCESS_COARSE_LOCATION")

                pressHome()
                val context = InstrumentationRegistry.getInstrumentation().context
                val intent = context.packageManager.getLaunchIntentForPackage("com.moon.pharm")
                checkNotNull(intent) { "앱의 실행 Intent를 찾을 수 없습니다." }

                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("IS_BENCHMARK_TEST", true)
                startActivityAndWait(intent)

                device.waitForIdle()

                val consultTab = device.wait(Until.findObject(By.text("상담")), 5000)
                    ?: device.wait(Until.findObject(By.desc("상담")), 5000)
                checkNotNull(consultTab) { "하단 네비게이션 바에서 '상담' 탭을 찾을 수 없습니다." }
                consultTab.click()
                device.waitForIdle()

                val addButton =
                    device.wait(Until.findObject(By.desc("consult_add_button")), 5000)
                checkNotNull(addButton) { "작성하기 버튼(consult_add_button)을 찾을 수 없습니다." }
                addButton.click()
                device.waitForIdle()

                val titleInput = device.wait(Until.findObject(By.desc("title_input")), 5000)
                checkNotNull(titleInput) { "제목 입력창을 찾을 수 없습니다." }

                titleInput.click()
                device.waitForIdle()

                device.executeShellCommand("input text 'TestTitle123'")
                device.waitForIdle()

                val contentInput = device.wait(Until.findObject(By.desc("content_input")), 5000)
                checkNotNull(contentInput) { "내용 입력창을 찾을 수 없습니다." }

                contentInput.click()
                device.waitForIdle()

                device.executeShellCommand("input text 'TestContent123456789'")
                device.waitForIdle()

                device.pressBack()
                device.waitForIdle()

                val nextButton = device.wait(Until.findObject(By.desc("next_button")), 5000)
                checkNotNull(nextButton) { "다음 버튼을 찾을 수 없거나 활성화되지 않았습니다." }
                nextButton.click()
                device.waitForIdle()

                targetButtonToClick =
                    device.wait(Until.findObject(By.desc("navigate_to_map_button")), 10000)
                checkNotNull(targetButtonToClick) { "약사 선택 화면에서 '지도 보기' 버튼을 찾을 수 없습니다." }

                Thread.sleep(2000)
                device.waitForIdle()
            }
        ) {
            val mapButton = device.wait(Until.findObject(By.desc("navigate_to_map_button")), 5000)
            checkNotNull(mapButton) { "측정 구간: 지도 보기 버튼을 클릭할 수 없습니다." }

            mapButton.click()

            val isMapLoaded = device.wait(Until.hasObject(By.desc("map_view")), 10000)
            check(isMapLoaded) { "지도 화면(map_view)이 제시간에 로딩되지 않았습니다." }
            device.waitForIdle()

            val mapView = device.findObject(By.desc("map_view"))
            if (mapView != null) {
                mapView.setGestureMargin(200)
                mapView.swipe(androidx.test.uiautomator.Direction.LEFT, 0.5f)
            }
            device.waitForIdle()

            Thread.sleep(1000)
        }
    }
}