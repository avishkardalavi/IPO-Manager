# 📊 IPO Manager

A native Android application for managing IPO profits, IPO fund transfers, analytics, and personal IPO records in one place.

## 📱 About the Project

**IPO Manager** is a personal Android application designed to make it easy to record and track IPO-related financial activity.

The application provides two main sections:

- **IPO Profits** – Record IPO profit/loss transactions.
- **IPO Fund Transfers** – Track money transferred between bank accounts for IPO applications.

It also includes analytics, search/filtering, local data persistence, and data backup/restore.

The project currently works completely offline and does not require login, Firebase, a backend server, or hosting.

## ✨ Features

### 1. IPO Profits

Record and manage IPO profit/loss entries with information such as:

- IPO/company name
- Profit or loss amount
- Bank/account
- Person/account holder
- Year
- Profit/Loss selection
- Date and related transaction information

The application provides:

- Total Profit
- Total Loss
- Net Profit
- Profit/loss history
- Search and filtering
- Edit and delete functionality

### 2. IPO Fund Transfers

Track money transferred for IPO applications.

The transfer section records information such as:

- Amount
- Source account
- Destination account
- Purpose
- Transfer date
- Direct transfer status

Users can also edit and delete transfer records.

### 3. Analytics

The Analytics section provides a consolidated view of IPO activity, including:

- Overall profit
- Overall loss
- Net profit
- Year-wise analysis
- Person-wise analysis
- IPO transaction summaries

### 4. Local Database

The application uses **Room Database** for persistent local storage.

Data remains available after:

- Closing the application
- Removing the application from recent apps
- Reopening the application

No online database is required.

### 5. Export & Import

The application supports data backup and restoration using JSON files.

**Export Data**
- Creates a JSON backup file.
- Can be saved to the device or another available storage location.

**Import Data**
- Reads a previously exported JSON backup.
- Imports missing records.
- Does not intentionally erase existing records during import.

## 🛠️ Technologies Used

- **Java**
- **Android SDK**
- **Android Studio**
- **Gradle**
- **Room Database**
- **SQLite**
- **JSON**
- **Android Storage Access Framework**

## 📂 Project Structure

```text
IPO-Manager/
│
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/avishkar/ipomanager/
│   │       │       ├── MainActivity.java
│   │       │       ├── AnalyticsActivity.java
│   │       │       ├── AppDatabase.java
│   │       │       ├── Profit.java
│   │       │       ├── ProfitDao.java
│   │       │       ├── Transfer.java
│   │       │       └── TransferDao.java
│   │       │
│   │       └── res/
│   │
│   └── build.gradle.kts
│
├── gradle/
│   └── wrapper/
│
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
└── README.md
```

## ⚙️ Requirements

- Android Studio
- Android SDK
- JDK 17
- Android device or emulator

Project configuration:

- **Compile SDK:** 35
- **Target SDK:** 35
- **Minimum SDK:** 23
- **Java:** 17
- **Room:** 2.8.5
- **Application ID:** `com.avishkar.ipomanager`

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/avishkardalavi/IPO-Manager.git
```

### 2. Open in Android Studio

Open the project **root folder** in Android Studio.

### 3. Sync the Project

Allow Android Studio to download the required Gradle dependencies and complete project synchronization.

### 4. Connect an Android Device

Enable Developer Options and USB Debugging on your Android device, or use an Android emulator.

### 5. Run the Application

Click **Run ▶** in Android Studio and select the connected device/emulator.

## 📦 Building an APK

In Android Studio:

1. Select **Build**.
2. Select **Build Bundle(s) / APK(s)**.
3. Select **Build APK(s)**.

The debug APK is normally available under:

```text
app/build/outputs/apk/debug/
```

## 📲 Installing on Another Android Device

The APK can be transferred to another Android device and installed manually.

Because the application stores data locally using Room Database, each device has its own local database.

To transfer existing IPO records:

1. Export data from the original device.
2. Transfer the exported JSON file.
3. Install IPO Manager on the new device.
4. Open **Settings → Import Data**.
5. Select the exported JSON file.

## 🔐 Privacy

IPO Manager is currently designed as an offline personal application.

- No user account is required.
- No login system is required.
- No Firebase is used.
- No backend server is required.
- No hosting is required for the application.
- IPO records are stored locally on the device.

Keep exported backup files secure because they may contain personal financial records.

## 💾 Data Persistence

IPO records are stored using Room Database.

The application uses persistent local storage rather than temporary in-memory storage, allowing records to remain available when the application is closed and reopened.

## 🔄 Backup Recommendation

Periodically use:

**Settings → Export Data**

to create a backup of important IPO records.

Keep the exported JSON backup in a safe location.

## 🎯 Project Goals

- Simplify personal IPO record keeping.
- Track IPO profits and losses.
- Track IPO-related fund transfers.
- Provide useful financial analytics.
- Keep data available offline.
- Provide easy backup and restoration.
- Maintain a simple mobile-friendly interface.

## 🧪 Current Status

- [x] IPO Profit Management
- [x] IPO Fund Transfer Management
- [x] Local Room Database
- [x] Edit Records
- [x] Delete Records
- [x] Search/Filtering
- [x] Analytics
- [x] Export Data
- [x] Import Data
- [x] Android App Icon
- [x] Mobile UI
- [x] Offline Data Storage

## 👨‍💻 Author

**Avishkar Dalavi**

GitHub: https://github.com/avishkardalavi

## 📄 License

This project is currently a personal project.

If you plan to distribute or reuse the project publicly, add an appropriate open-source license such as MIT, Apache-2.0, or another license that matches your intended usage.
