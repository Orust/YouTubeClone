# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an Android application that serves as a YouTube clone with an intelligent recommendation system. The app records user behavioral metrics (scroll depth, query length, reading time, cumulative clicks) and uses a classifier to determine user task types ("lookup", "exploration", "repeat") to provide personalized video recommendations.

This application was used in a thesis published at [Hosei University](https://hosobe.cis.k.hosei.ac.jp/lab/wp-content/uploads/2021/04/t_ide-bthesis-abstract.pdf).

## Development Environment

This is an Android Java application. The project structure follows standard Android conventions with source code under `main/java/com/example/youtubeclone/`.

**Note**: This repository does not contain standard Gradle build files (build.gradle, gradlew). Development must be done in Android Studio by importing the `main/` directory as an Android module.

## Architecture Overview

### Core Application Components

**Classifier (Application Class)**
- Extends `Application` and serves as the global state manager
- Manages YouTube API keys and OAuth 2.0 access tokens
- Tracks user behavior metrics: query_length, reading_time, scroll_depth, cumulative_clicks
- Implements the classification algorithm that categorizes user behavior into three task types:
  - **"lookup"**: query_length > 0 AND scroll_depth <= 16
  - **"exploration"**: query_length > 0 AND scroll_depth > 16, OR query_length <= 0 AND scroll_depth > 13
  - **"repeat"**: query_length <= 0 AND scroll_depth <= 13
- Stores participant data with preferred categories and channel IDs for personalized recommendations
- Referenced throughout the app via `(Classifier) getApplication()`

**Participant System**
- Each participant has preferred video categories (by ID) and subscribed channel IDs
- Used by the recommendation system to personalize video suggestions based on classified task type

### Activity Flow

1. **AccountActivity**: OAuth 2.0 authentication and participant selection
   - Handles Google OAuth flow to obtain access tokens for YouTube API
   - Uses custom URI scheme `oauthviewer://callback` and `urn:ietf:wg:oauth:2.0:oob`
   - Allows switching between API keys and toggling the recommendation system
   - Participant selection initializes their preference profile

2. **MainActivity**: Main hub with bottom navigation
   - Contains BottomNavigationView with 5 tabs: Home, Explore, Subscriptions, Notifications, Library
   - Implements search functionality via SearchView in action bar
   - Uses Navigation Component with NavHostFragment

3. **SearchResultsActivity**: Displays search results
   - Handles ACTION_SEARCH intents from the SearchView
   - Records query length for the classifier

4. **VideoPlayActivity**: Video player and related videos
   - Extends YouTubeBaseActivity for embedded YouTube player
   - Displays related videos in a ListView below the player
   - **Critical behavior tracking**:
     - Tracks scroll depth in the related videos list (incremented when user scrolls to a new item)
     - Records reading time between first and second video click
     - On second video view, writes behavioral data to `data1.txt` file
   - **Intelligent recommendations**: When `system` flag is true and viewing the 3rd related video:
     - For "exploration" tasks: Inserts videos from user's preferred categories at position 2
     - For "repeat" tasks: Inserts videos from user's subscribed channels at position 2
     - For "lookup" tasks: No modification to related videos

### Fragment Structure (Bottom Navigation Tabs)

Each fragment (Home, Explore, Subscriptions, Library, Notifications) follows MVVM pattern with Fragment + ViewModel pairs. They:
- Display video lists via ListView with MyCustomAdapter
- Track scroll depth when count < 2 (before second video click)
- Use LiveData to observe video lists from ViewModels

**SubscriptionsFragment** specifically uses the OAuth access token to fetch personalized subscription data when authorized.

### Data Models

**VideoDetails**: Represents a video with videoId, title, description, thumbnail URL, and categoryId

**MyCustomAdapter**: Custom ArrayAdapter for displaying video lists with thumbnails

### YouTube API Integration

The app uses YouTube Data API v3 for:
- Searching videos
- Fetching related videos
- Getting popular videos by category
- Accessing user subscriptions (when OAuth authorized)

API keys are stored in `Classifier` class and can be rotated. The OAuth client ID is hardcoded in AccountActivity.

### Behavioral Data Collection

The app writes CSV data to `data1.txt` in the format:
```
participant_name
query_length,reading_time,scroll_depth,task_type
```

This data is written when the user clicks their second video, after the classifier has determined the task type.

## Key Implementation Details

### State Management
- Global state (user metrics, API keys, participant info) lives in the Classifier Application class
- Access it via `(Classifier) getApplication()` from Activities or `(Classifier) getActivity().getApplication()` from Fragments

### Scroll Depth Tracking
- Implemented via ListView.OnScrollListener
- Uses an index comparison pattern: if firstVisibleItem changes, increment scroll_depth
- Only tracks when count < 2 (before second video click)

### Task Classification Timing
- Classification happens in VideoPlayActivity when count == 2 (second video)
- Uses accumulated metrics: query_length from search, scroll_depth from scrolling, reading_time from time elapsed

### Recommendation System Toggle
- Controlled by `classifier.getSystem()` boolean flag
- Can be toggled in AccountActivity
- When enabled, modifies related video list based on classified task type

## Common Development Tasks

Since this project lacks standard Gradle build files, there are no command-line build commands. All development must be done through Android Studio:
1. Open Android Studio
2. Import the project by selecting the root directory
3. Configure the `main/` directory as the app module
4. Build and run through Android Studio's interface

## Important Notes

- API keys in Classifier.java should be replaced with valid YouTube Data API keys
- OAuth client ID in AccountActivity should match your Google Cloud Console configuration
- The app uses deprecated `YouTubeBaseActivity` from the YouTube Android Player API
- File I/O for data collection uses internal storage (getApplicationContext().getFilesDir())
