# Shale Namma Pride 📚🏫

A modern Android application built using **Jetpack Compose** and **Firebase Realtime Database** to improve transparency and communication between schools and parents.

The app provides information about:

* 🍛 Daily Mid-day Meals
* 🏫 School Facilities
* ⭐ Student Achievements
* 📝 Parent Feedback

The application supports both **English** and **Kannada** languages.

---

## ✨ Features

### 🏠 Home Dashboard

* Simple and clean UI
* Navigation to all modules
* Language switcher (English / Kannada)

### 🍱 Daily Meal Screen

* Displays today’s meal menu
* Meal image support
* Real-time updates from Firebase

### 🏫 Facilities Gallery

* Displays school infrastructure photos
* Facility titles with images

### ⭐ Student Achievements

* Showcase student accomplishments
* Student photo support

### 📝 Parent Feedback

* Parents can submit suggestions
* Anonymous feedback option
* Feedback stored in Firebase

---

## 🛠️ Tech Stack

| Technology                 | Usage                |
| -------------------------- | -------------------- |
| Kotlin                     | Programming Language |
| Jetpack Compose            | UI Toolkit           |
| Material 3                 | UI Design            |
| Firebase Realtime Database | Backend Database     |
| Coil                       | Image Loading        |
| Navigation Compose         | Screen Navigation    |

---

## 📱 Screenshots

Add your app screenshots here.

Example:

```md
![Home Screen](screenshots/home.png)
![Meal Screen](screenshots/meal.png)
```

---

## 📂 Project Structure

```text
com.example.shalenammapride
│
├── MainActivity.kt
│
├── Screens
│   ├── Home
│   ├── MealScreen
│   ├── GalleryScreen
│   ├── StudentScreen
│   └── FeedbackScreen
│
└── Firebase Integration
```

---

## 🔥 Firebase Setup

### 1. Create Firebase Project

Go to:

* [Firebase Console](https://console.firebase.google.com?utm_source=chatgpt.com)

### 2. Enable Realtime Database

* Create Realtime Database
* Start in test mode (for development)

### 3. Add Android App

* Register your package name:

  ```text
  com.example.shalenammapride
  ```

### 4. Download `google-services.json`

Place it inside:

```text
app/google-services.json
```

### 5. Update Firebase URL

```kotlin
private const val DB_URL = "YOUR_FIREBASE_DATABASE_URL"
```

---

## 🗄️ Firebase Database Structure

```json
{
  "dailyMeal": {
    "menu": "Rice and Sambar",
    "image": "image_url",
    "date": "15 May 2026"
  },

  "facilities": {
    "1": {
      "title": "Library",
      "image": "image_url"
    }
  },

  "students": {
    "1": {
      "name": "Student Name",
      "achievement": "Won Science Fair",
      "image": "image_url"
    }
  },

  "feedback": {
    "feedbackId": {
      "text": "Great school!",
      "anonymous": true,
      "timestamp": 123456789
    }
  }
}
```

---

## 📦 Dependencies

Add these dependencies in your `build.gradle`:

```gradle
dependencies {

    implementation "androidx.compose.ui:ui:<version>"
    implementation "androidx.compose.material3:material3:<version>"

    implementation "androidx.navigation:navigation-compose:<version>"

    implementation "io.coil-kt:coil-compose:<version>"

    implementation platform("com.google.firebase:firebase-bom:<version>")
    implementation "com.google.firebase:firebase-database-ktx"
}
```

---

## ▶️ Running the Project

### Clone Repository

```bash
git clone https://github.com/your-username/shale-namma-pride.git
```

### Open in Android Studio

* Open project folder
* Sync Gradle

### Run App

* Connect Android device or emulator
* Click ▶ Run

---

## 🌐 Language Support

* English 🇬🇧
* Kannada 🇮🇳

---

## 🎯 Future Improvements

* Admin Panel
* Push Notifications
* Authentication
* Offline Support
* Student Attendance Module
* Teacher Announcements
* Dark Mode

---

## 🤝 Contribution

Contributions are welcome!

1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License.

---

## 👨‍💻 Developer

Developed with ❤️ using Kotlin and Jetpack Compose.
