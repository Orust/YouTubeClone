# YouTubeClone

An Android application that provides intelligent video recommendations by analyzing user behavior patterns. This app tracks user actions (scroll depth, query length, reading time, cumulative clicks) and uses a classification algorithm to determine the user's task type, then adapts video recommendations accordingly.

This application was used in research published at [Hosei University](https://hosobe.cis.k.hosei.ac.jp/lab/wp-content/uploads/2021/04/t_ide-bthesis-abstract.pdf).

## Features

### Behavioral Tracking and Classification

The app monitors user interactions and classifies them into three task types:

- **Lookup**: User searches with specific intent (query_length > 0, scroll_depth ≤ 16)
- **Exploration**: User browses extensively to discover new content
  - With search: scroll_depth > 16
  - Without search: scroll_depth > 13
- **Repeat**: User returns to familiar content (query_length ≤ 0, scroll_depth ≤ 13)

### Intelligent Recommendations

Based on the classified task type, the app customizes related video suggestions:

- **Exploration tasks**: Inserts popular videos from user's preferred categories
- **Repeat tasks**: Adds videos from user's subscribed channels
- **Lookup tasks**: Shows standard YouTube related videos

### OAuth 2.0 Authorization

If the user agrees, the application gets the code and POSTs the code to get the access token.

<img src="https://user-images.githubusercontent.com/61633483/118401297-22373c80-b6a0-11eb-9f7f-67bf04501861.png" width="320px"> <img src="https://user-images.githubusercontent.com/61633483/118417527-6271db80-b6ef-11eb-9dc8-8cd6c35c1f73.png" width="320px">

When the application gets the access token, the video list such as on the subscribed channel tab is customized.

<img src="https://user-images.githubusercontent.com/61633483/118418053-cb5a5300-b6f1-11eb-8583-edddb68f6bcd.png" width="320px">

## Screenshots

### Search Videos

<img src="https://user-images.githubusercontent.com/61633483/118401412-9376ef80-b6a0-11eb-9b10-77b5ff28dac6.png" width="320px"> <img src="https://user-images.githubusercontent.com/61633483/118401394-7b9f6b80-b6a0-11eb-8622-362e6482eb1f.png" width="320px">

### Video Viewing

<img src="https://user-images.githubusercontent.com/61633483/118401423-9d005780-b6a0-11eb-9bb0-b433a25a9777.png" width="320px">

## Setup

### Prerequisites

- Android Studio
- YouTube Data API v3 key from [Google Cloud Console](https://console.cloud.google.com/)
- OAuth 2.0 client ID for Android (if using personalized subscriptions)

### Configuration

1. **API Key Setup**:
   - Open `main/java/com/example/youtubeclone/Classifier.java`
   - Replace `API_KEY` and `API_KEY2` with your YouTube Data API keys:
     ```java
     private String API_KEY = "YOUR_API_KEY_HERE";
     private String API_KEY2 = "YOUR_SECOND_API_KEY_HERE";
     ```

2. **OAuth Configuration** (optional, for subscription features):
   - Open `main/java/com/example/youtubeclone/AccountActivity.java`
   - Replace the client ID with your OAuth 2.0 client ID:
     ```java
     private final String clientId = "YOUR_CLIENT_ID.apps.googleusercontent.com";
     ```

3. **Participant Setup**:
   - Edit the participant preferences in `Classifier.java` init() method
   - Configure preferred video categories (by ID) and channel IDs for each participant

### Building and Running

1. Open Android Studio
2. Import the project by selecting the root directory
3. Configure the `main/` directory as the app module
4. Build and run on an Android device or emulator

## Architecture

- **Classifier**: Global Application class managing state, API keys, and classification logic
- **MainActivity**: Hub with bottom navigation (Home, Explore, Subscriptions, Notifications, Library)
- **AccountActivity**: OAuth authentication and participant selection
- **VideoPlayActivity**: YouTube player with intelligent related video recommendations
- **MVVM Pattern**: Fragments use ViewModels with LiveData for reactive UI updates

## Data Collection

The app records behavioral data to `data1.txt` in CSV format:
```
participant_name
query_length,reading_time,scroll_depth,task_type
```

Data is written when the user clicks their second video, after task classification is complete.

## Technologies Used

- Android SDK (Java)
- YouTube Android Player API
- YouTube Data API v3
- Google OAuth 2.0
- Volley (HTTP library)
- Android Navigation Component
- MVVM Architecture Pattern

## License

This project was developed for academic research purposes.
