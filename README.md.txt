# Smart Pantry Manager

## Project Description

Smart Pantry Manager is a Java Android application that helps users manage leftover ingredients and reduce food waste.

The user can add, edit and delete pantry items. The app then suggests recipes that can be made using only the ingredients and quantities currently available in the pantry.

## Main Features

- Add pantry items
- Edit pantry items
- Delete pantry items
- Store ingredient name, quantity, unit and optional expiry date
- Validate pantry input
- Display pantry items using a custom ListView adapter
- Store 15 recipes
- Suggest recipes using strict ingredient matching
- Handle simple singular and plural ingredient names
- Handle basic unit conversions
- Display recipe ingredients and preparation steps
- Show expiring-soon alerts
- Turn expiry alerts on or off in Settings
- Navigate using the app menu

## Database

The application uses SQLite with SQLiteOpenHelper.

SQLite was chosen because it stores the data locally on the Android device and does not require an internet connection or external database server.

The database stores pantry items, recipes and recipe ingredients. Pantry data remains saved after the application is closed and reopened.

## Software Requirements

- Android Studio
- Java
- Android SDK
- Android emulator or Android device

## How to Run the Application

1. Open Android Studio.
2. Select Open.
3. Open the MobileApp700 project folder.
4. Wait for the Gradle project sync to finish.
5. Start an Android emulator or connect an Android device.
6. Click the Run button in Android Studio.
7. The Smart Pantry Manager application will open.

## How to Use the Application

1. Open My Pantry to add ingredients.
2. Enter the ingredient name, quantity and unit.
3. An expiry date can optionally be entered using yyyy-MM-dd.
4. Tap a pantry item to edit or delete it.
5. Open Suggested Recipes to view recipes that can be made using the current pantry.
6. Select a recipe to view its ingredients and preparation steps.
7. Open Settings to turn expiring-soon alerts on or off.

## Database Tables

The SQLite database contains the following tables:

- pantry
- recipes
- recipe_ingredients