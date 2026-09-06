# 不用 Android Studio 直接取得 APK

## 方法：GitHub Actions 雲端編譯

1. 登入 GitHub，建立一個新的 repository。
2. 將本專案 ZIP 解壓縮後，把所有檔案上傳到 repository 根目錄。
3. 進入 GitHub repository 的 **Actions** 分頁。
4. 點左側 **Build APK**。
5. 點 **Run workflow** → 再按一次 **Run workflow**。
6. 等待流程完成後，點進成功的執行紀錄。
7. 在頁面最下方 **Artifacts** 下載 `StandbyCalendarClock-APK`。
8. 解壓縮後會得到 `app-debug.apk`，即可傳到 Android 手機安裝。

## App 功能

- 橫向待機大時鐘與月曆。
- 偵測手機充電狀態。
- 充電時自動設定 `FLAG_KEEP_SCREEN_ON`，保持螢幕不熄滅。
- 拔除充電器後會移除常亮旗標，恢復手機原本的螢幕逾時設定。
- 不需要網路權限。

## Android 安裝提醒

第一次安裝自行編譯的 APK 時，Android 可能要求允許「安裝未知應用程式」。請只對你用來開啟 APK 的檔案管理器或瀏覽器暫時授權。
