# Hotspots

Table of Contents:
* [Introduction](#introduction)
* [Design](#design)

## <a name="introduction"></a>Introduction
<i>HotSpot</i> uses the ever popular location services of your Android phone to judge how "hot" the spot you're at is.
The score you receive, out of 10, takes into consideration what is around you and how popular it is based on recent Foursquare reviews.
You then have a list of the most highly reviewed venues at that location and it's associated information a fingertip's reach away.
Your "spots" are marked, given a custom name at your choosing, and saved locally on your device.
That way you can find that hip, young neighborhood you walked through yesterday or remind yourself of that failed gentrification effort
on the north side of the city.

## <a name="design"></a>Design

![savinglocation](https://user-images.githubusercontent.com/7444521/47698871-3f818980-dbce-11e8-970a-0032ff14a6b4.gif)

Upon entering the app the user is presented with a random location marked on the map and a FAB that can be used to obtain the user's current 
location.  Pressing the FAB again gives the user the option to mark their current location.  Both the map and location services use
Google Maps API v2 built into our activity.  

If they want to mark their location a Fragment slides into view where they can edit the 
name for their location.  On submission, the location and it's associated information is inserted into the SQLite Database via a Content Provider.
An extended AsyncTask task is launched in the background that makes a query to the Foursquare API to obtain the Venues nearby.  Within this
thread a ThreadPoolExecutor executes an AsyncTask for each Venue initially returned to gain more detailed information.  If the query returnes
a value greater than 0 it is insterted into the database via content provider.

<br>
![venueandfoursquare](https://user-images.githubusercontent.com/7444521/47698847-2f69aa00-dbce-11e8-8aa6-c11eb5ac6ea8.gif)

The FAB with the "next" arrow will appear only once all background processes are completed.  Clicking the FAB once will slide in another
Fragment showing the results of the query to the Foursquare API.  The nearby venues, each venue's rating, and the "score" of the general 
location.  The ListView of nearby Venues is populated using a custom Cursor Adapter that is wrapped by our Content Provider.  

Clicking a list item will highlight it.  Subsequently clicking the Foursquare icon will launch an implicit intent that will send the user 
to the Foursquare website or their native Android app.

<br>
![previtems4real](https://user-images.githubusercontent.com/7444521/47698902-50ca9600-dbce-11e8-9dea-bc1c6284c104.gif)

After getting their score the user will be able to view previous locations via markers or list contained in the navigation drawer.
