# 待機日曆時鐘 Android App

功能：
- iPhone StandBy 風格：左側大時鐘、右側月曆。
- 橫向全螢幕顯示。
- 每秒更新時間。
- 自動顯示日期、星期與當月月曆。
- 充電中：使用 FLAG_KEEP_SCREEN_ON，保持螢幕不熄滅。
- 拔除充電線：清除 FLAG_KEEP_SCREEN_ON，恢復 Android 系統原本的螢幕逾時。
- App 切到背景：不再強制常亮，避免不必要耗電。
- 不需要特殊權限。

## Build APK
1. 用 Android Studio 開啟此資料夾。
2. 等待 Gradle Sync 完成。
3. 選單 Build > Build Bundle(s) / APK(s) > Build APK(s)。
4. Debug APK 通常位於：`app/build/outputs/apk/debug/app-debug.apk`

建議：Android Studio 使用 JDK 17，並安裝 Android SDK 35。
