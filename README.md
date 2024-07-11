# Movie App

This is a movie app built with Kotlin and Jetpack components, using the TMDB API to fetch movie data. The app includes features such as Room database for local storage, Retrofit for network calls, Paging for infinite scrolling, onboarding screen, splash screen, Shimmer effect for loading states, and a bookmark functionality.

## Features

- **Onboarding Screen:** Introduction screen for first-time users.
- **Splash Screen:** Initial screen displayed while the app is loading.
- **Movie List:** Display a list of movies fetched from the TMDB API.
- **Movie Details:** Show detailed information about a selected movie.
- **Shimmer Effect:** Loading animation for better user experience.
- **Paging:** Infinite scrolling for movie lists.
- **Bookmark Movies:** Save favorite movies to a local database using Room.

## Video
https://github.com/mohitdamke/Movie-New/assets/112572179/fc736804-a3eb-4ce6-b908-12429f9a5d9c

## Screenshots
![Movie2](https://github.com/mohitdamke/Movie-New/assets/112572179/5946cb0a-2e80-460b-a8be-663255928854)
![Movie1](https://github.com/mohitdamke/Movie-New/assets/112572179/d2c3b1f5-4fbd-4c8f-89fd-ec55739a6228)


## Getting Started

### Prerequisites

- Android Studio
- Kotlin 1.4+
- TMDB API Key

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/movie-app.git
   cd movie-app

    Open the project in Android Studio:
        File -> Open -> Select the cloned repository.

    Add your TMDB API key:
        Create a local.properties file in the root project directory.
        Add the following line to local.properties:

        properties

        tmdb_api_key=YOUR_API_KEY

    Build the project:
        Build -> Make Project

    Run the app:
        Run -> Run 'app'

Usage
Onboarding Screen

The onboarding screen will be shown to first-time users to introduce the app's features.
Splash Screen

The splash screen is displayed while the app is loading.
Movie List

Browse through the list of movies fetched from the TMDB API. The list supports infinite scrolling using the Paging library.
Movie Details

Click on a movie to see detailed information about it.
Bookmark Movies

To bookmark a movie, click on the bookmark icon. You can view your bookmarked movies in the "Bookmarked" section of the app.
Architecture

The app follows the MVVM (Model-View-ViewModel) architecture pattern and uses the following components:

    Room Database: For local storage of bookmarked movies.
    Retrofit: For making network calls to the TMDB API.
    Paging: For infinite scrolling support.
    ViewModel: To manage UI-related data in a lifecycle-conscious way.
    LiveData: To handle data observation.

Contributing

    Fork the repository.
    Create a new branch (git checkout -b feature-branch).
    Make your changes.
    Commit your changes (git commit -m 'Add some feature').
    Push to the branch (git push origin feature-branch).
    Open a pull request.

License

This project is licensed under the MIT License - see the LICENSE file for details.
Acknowledgements

    TMDB API for providing movie data.
    Shimmer for the loading animations.

css


You can adjust the content and paths according to your specific project details and repos
