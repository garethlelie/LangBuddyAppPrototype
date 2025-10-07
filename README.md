# 🌍 LangBuddy – Language Learning App

**LangBuddy** is an interactive Android language learning app that helps users learn new languages step by step.  
It combines lessons, quizzes, progress tracking, and gamification features like leaderboards to make learning engaging and effective.

---

## 🌟 Features

- ✅ **Interactive Lessons** – Vocabulary and phrases with real-world examples  
- ✅ **Quizzes** – Multiple-choice questions with instant feedback  
- ✅ **Progress Tracking** – Track your progress per lesson  
- ✅ **Leaderboards** – Compete and compare with other users  
- ✅ **User Profiles** – Manage and customize your profile  
- ✅ **Onboarding Flow** – Easy welcome screens for new users  
- ✅ **Firebase Integration** – Real-time database, notifications, and authentication  
- ✅ **Responsive UI** – Material Design principles for a smooth experience  

---

## 📦 Installation

### 1. Clone the Repository
```bash
git clone https://github.com/VCNMB-3rd-years/vcnmb-prog7314-2025-poe-langbuddy.git

### 2. Open in Android Studio
Go to File → Open → select the project root directory.

Wait for Gradle sync to complete.

3. Configure Firebase
Replace the google-services.json file in:

css
Copy code
app/src/main/
with your own Firebase configuration.

4. Run the App
Select a device or emulator → click Run ▶ → start learning!

🗂 Project Structure
bash
Copy code
src/
 ├─ androidTest/                  
 │   └─ java/student/projects/langbuddy/ExampleInstrumentedTest.kt
 ├─ main/
 │   ├─ AndroidManifest.xml
 │   ├─ java/student/projects/langbuddy/
 │   │   ├─ MainActivity.kt
 │   │   ├─ data/
 │   │   │   ├─ api/
 │   │   │   │   ├─ ApiClient.kt
 │   │   │   │   ├─ ApiService.kt
 │   │   │   │   ├─ FirebaseNotificationService.kt
 │   │   │   │   └─ FirebaseService.kt
 │   │   │   ├─ model/
 │   │   │   │   ├─ Attempt.kt
 │   │   │   │   ├─ Chapter.kt
 │   │   │   │   ├─ Choice.kt
 │   │   │   │   ├─ Language.kt
 │   │   │   │   ├─ LeaderboardEntry.kt
 │   │   │   │   ├─ Lesson.kt
 │   │   │   │   ├─ Profile.kt
 │   │   │   │   ├─ Question.kt
 │   │   │   │   └─ User.kt
 │   │   │   └─ repository/LessonRepository.kt
 │   │   ├─ ui/
 │   │   │   ├─ auth/              # Login/Register
 │   │   │   ├─ home/              # Home screen and lesson list
 │   │   │   ├─ leaderboard/       # Leaderboard
 │   │   │   ├─ lessons/           # Lesson screens
 │   │   │   ├─ onboarding/        # Onboarding screens
 │   │   │   ├─ profile/           # User profile
 │   │   │   ├─ settings/          # App settings
 │   │   │   ├─ splash/            # Splash screen
 │   │   │   └─ tests/             # Internal test activities
 │   │   └─ utils/NotificationHelper.kt
 │   └─ res/
 │       ├─ drawable/              # Images, icons, XML drawables
 │       ├─ layout/                # Activity & fragment XML layouts
 │       ├─ menu/                  # Bottom navigation menu
 │       ├─ mipmap-*/              # Launcher icons
 │       ├─ values/                # Colors, strings, themes
 │       ├─ values-night/          # Dark mode themes
 │       └─ xml/                   # Backup rules, data extraction
 └─ test/
     └─ java/student/projects/langbuddy/ExampleUnitTest.kt
🔑 Key Components
🧭 Authentication
LoginActivity.kt / RegisterActivity.kt – handle user login and registration

AuthViewModel.kt – manages authentication logic and Firebase integration

📘 Lessons
LessonDetailActivity.kt – interactive lesson view with quizzes

LessonRepository.kt – fetches lesson data from Firebase

LessonAdapter.kt – displays lessons dynamically

🏆 Leaderboards
LeaderboardFragment.kt – displays user rankings

LeaderboardViewModel.kt – fetches leaderboard data in real-time

👤 Profile & Settings
ProfileFragment.kt / SettingsFragment.kt – view and edit user info

ProfileViewModel.kt / SettingsViewModel.kt – manages logic

🔔 Notifications
NotificationHelper.kt – handles in-app notifications

FirebaseNotificationService.kt – integrates push notifications

📡 API Integration
Retrofit API
ApiService.kt – defines endpoints for lessons, users, and leaderboards

ApiClient.kt – initializes the Retrofit client

Firebase Realtime Database
Stores user progress, profiles, and leaderboard data

Sends notifications and updates in real-time

🎨 UI Highlights
Clean Material Design layouts

Lesson cards showing progress

Interactive multiple-choice quizzes

✅ Correct answers → Green highlight

❌ Wrong answers → Red highlight

Fully interactive Leaderboards, Profiles, and Settings screens

🏃‍♂️ Running the App
Ensure your google-services.json file is in app/src/main/

Open the project in Android Studio

Build and run the app on an emulator or device

Sign up or log in → start learning languages interactively

🏗 Contributing
We welcome contributions! 🎉

Fork the repository

Create your feature branch

bash
Copy code
git checkout -b feature/MyFeature
Commit your changes

bash
Copy code
git commit -m "Add my feature"
Push to your branch

bash
Copy code
git push origin feature/MyFeature
Open a Pull Request

🎯 Future Improvements
🎧 Add audio pronunciation for lessons

🧠 Implement adaptive difficulty for quizzes

🔥 Introduce daily challenges & streak tracking

🌐 Expand multi-language support

🧾 License
This project is licensed under the MIT License – see the LICENSE file for details.
