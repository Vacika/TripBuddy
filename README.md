# NajdiPrevoz
 
Najdi Prevoz is application platform where the user could share a ride with other users of the platform.

Trip Roles: <br/>
 - <b>Driver</b> - The user wants to share the costs of a his trip with other passengers, so he finds passengers here. <br/>
 - <b>Passenger </b>- User looks for active published trip for the route he wants to travel.

<b>Public pages(no Authorization): </b> 
<ul>
 <li> Landing page </li>
  <li> Search trips page </li>
  <li> Trip details dialog </li>
  <li> User profile's page </li>
</ul>
<br/>
<b>Authorized user pages(Logged in): </b> 
<ul>
 <li> Add trip page </li>
  <li> Control panel page </li>
  <li> Notifications page </li>
  <li> Reservation dialog page </li>
</ul>
<br/>

<b>Admin pages(Admin Role): </b> 
<ul>
 <li> Admin Panel</li>
</ul>
<br/>
<b> Features to be implemented: </b>
<ul>
 <li> Add additional 'luggage' parameter in trips (Think how to implement this to be intuitive) </li>
 <li><s>SMS Notify feature when a search is made and no trips were found. User enters phone number and notification validity time (ex:2days) and receives SMS if someone publishes a trip on the location he searched for</s></li>
  <li>Top 3 drivers - Display on landing page a top 3 trivers dashboard, which will be the best-rated drivers</li>
 <li>Most recent published trips -- Display on landing page the 5 most recent published trips</li>
 <li>Make city search input box an autocomplete</li>
  <li>Add validators for registration forms input box</li>
 <li>Implement OAuth2 (If we can fetch phone number / mail / profile photo / full name / date of birth from FB/Google)</li>
 <li> Move filters on sidebar when searching trips instead of the top of the screen </li>
 <li> Add two more input boxes on registration form, a 'Confirm password' with validator and a 'Profile photo' file upload </li>
 <li> Edit trip functionality </li>
 <li> Create a mobile application using Ionic framework </li>
 <li> Split application into modules - both front end and back end (Back-end : Mail Module, Sms Sender module, perhaps Notifications module)</li>
 <li> <s>Responsive</s> </li>
 <li> <s> Set a minimum {X} hours before cancelling a trip </s> </li>
 <li> <s> Admin Panel for Admin users - list all users </s> </li>
 <li> <s> Ban / Unban user functionality for Admin </s> </li>
 <li> <s> Manually activate user functionality for Admin </s> </li>
 <li> <s> Manually activate user functionality for Admin </s> </li>
 <li> <s> Notifications </s> </li>
 <li> <s> Reset Password with token functionality </s> </li>
 <li> <s> Activate user with token functionality  </s> </li>
 <li> <s> JWT Security Layer </s> </li>
 <li> <s> Max {X} seats per ride configurable </s> </li>
 <li> <s> Control panel for user </s> </li>
 <li> <s>Ability to submit rating for a finished trip </s> </li>
 <li> <s> Cancel trip </s> </li>
 <li> <s> Add new trip </s> </li>
 <li> <s> Make a reservation with additional description(optional) </s> </li>
 <li> <s> Accept / Deny reservation, both from control panel and notifications screen </s> </li>
 <li> <s> Cancel a reservation request (the requester can cancel the reservation whether is approved or pending)</s> </li>
 <li> <s> Control panel for user </s> </li>
 <li> <s> Ability for users to can change password / gender / profile photo / date of birth </s> </li>
 <li> <s> Listing of all trips as driver/passenger with list of available actions to be taken </s> </li>
 <li> <s> Listing of all sent/received reservations with list of available actions to be taken </s> </li>
 <li> <s> List of all received ratings for the logged user + see trip details for which the rating was submitted </s> </li>
 <li> <s> User info page - available for any logged user to check someone's profile </s> </li>
 <li> <s> MULTI LANGUAGE - MK - EN - AL </s> </li>
 <li> <s> Errors thrown from back-end translated to front-end on the selected language </s> </li>

</ul>


Database Diagram:
[![Najdi-Prevoz-2.png](https://i.postimg.cc/1zfcT89F/Najdi-Prevoz-2.png)](https://postimg.cc/XZ6CB7BN)
