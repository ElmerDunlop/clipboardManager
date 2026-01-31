# 剪贴板管理器 (Clipboard Manager)

一个Android应用程序，用于自动监听和保存手机剪贴板内容。

## 功能特性

- ✅ 自动监听剪贴板变化
- ✅ 本地存储剪贴板历史记录
- ✅ 显示剪贴板历史列表
- ✅ 点击历史记录可复制到剪贴板
- ✅ 长按删除单条记录
- ✅ 一键清空所有历史记录
- ✅ 开始/停止监听功能

## 技术栈

- Kotlin
- Android SDK (minSdk 24, targetSdk 34)
- Room Database - 本地数据存储
- LiveData & ViewModel - MVVM架构
- RecyclerView - 列表显示
- Material Design Components - UI组件

## 项目结构

```
app/src/main/java/com/clipboard/manager/
├── database/
│   ├── ClipboardEntry.kt      # 数据实体
│   ├── ClipboardDao.kt        # 数据访问对象
│   ├── ClipboardDatabase.kt   # Room数据库
│   └── ClipboardRepository.kt # 数据仓库
├── service/
│   └── ClipboardMonitorService.kt  # 剪贴板监听服务
├── ui/
│   └── ClipboardViewModel.kt  # ViewModel
├── adapter/
│   └── ClipboardAdapter.kt    # RecyclerView适配器
└── MainActivity.kt             # 主界面
```

## 使用说明

1. 安装应用到Android设备（需要Android 7.0或更高版本）
2. 打开应用
3. 点击"开始监听"按钮启动剪贴板监听
4. 复制任何文本内容，应用会自动保存
5. 在应用中查看剪贴板历史
6. 点击历史记录可重新复制到剪贴板
7. 长按历史记录可删除单条记录
8. 使用"清空历史"按钮可删除所有记录

## 构建项目

使用Android Studio打开项目，或使用Gradle Wrapper命令行：

```bash
./gradlew assembleDebug
```

## 隐私说明

所有剪贴板数据仅存储在设备本地，不会上传到任何服务器。
