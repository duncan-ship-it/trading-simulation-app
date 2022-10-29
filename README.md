# Trading Simulation App report

[Repository link](https://github.com/duncan-ship-it/trading-simulation-app)

## Summary

An Android app that allows users to test various market strategies using real-time stock data.

## Libraries used

### Stock API

[Tiingo REST API for IEX](https://api.tiingo.com/documentation/iex)

- alternatives:
  - finnhub
  - tiingo websocket
  - yahoo finance wrapper
  - etc.
  - 
### HTTP library
[OkHttp3](https://square.github.io/okhttp/)

### Firebase SDK


### Chart layout component

[MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)


### Prototyping tool

Adobe XD

## App Design

### Initial prototype

[**Click here to go to interactive prototype**](https://xd.adobe.com/view/cce651a8-982f-415b-8d1d-8265a1076c6d-99b5/)

![Design of app](assets/design.JPG)

### MainActivity

This serves as a main menu for the app, whilst providing helpful metrics which help the user identify their performance. From here, the user can navigate to either their portfolio or the stocks activity, which lists available stocks which they can order.

The grey bar will display the stocks the user is currently invested in, with an arrow indicating an increase or decrease in the daily price. When the user presses the reset button, all orders are cleared, as well as the amount invested, resetting this figure to 0.

The background color of this activity is dependant on the net gain figure. It will transition from green to red, with green corresponding to good performance, and red the opposite.


### StocksActivity

This activity 

### PortfolioActivity



### DetailActivity

This activity appears when the user taps on a stocks row in either the StocksActivity or the Portfolio activity, which provides extra detail on each stock, whilst allowing the user to modify or create new orders (sell and buy respectively). 


### UI patterns utilised

Color

## Feedback received

1. _For added functionality I would suggest a saved portfolio page where users can see and interact with all their stocks they saved and bought or sold with._

    _For UX, it would be better for the buy and sell buttons and TextInputs to be smaller and centered because here the cost of making a mistake is high since people are supposed to trade with their real money. So, if the buttons are bigger, theres more room to make mistakes._

2. _Great application idea. Stock Simulator is always interesting as different stock simulator applications compete to provide functionalities that are as accurate to a real stock exchange application as much as possible. Many of the stock simulators application out there also provide additional features to attract more users. I like your idea of utilizing the entire screen to display the list of stocks, as it provides clarity to the data. The red and green coloring of the stock's rates is also very well implemented as it highlights the importance of each stock. There are a few considerations/suggestions I have in mind:_

    * _I think with the large screen size, the text on each row can be larger. Enhancing visibility and readability would reduce the chances of user reading wrong information._

    * _I think that the sell and buy buttons should do a page transition instead of showing a toast/snackbar notification. Toast and snackbar notifications are usually meant for not-so-important to fairly important information. As for this case, buying and selling have a large impact on the user. If the user did not notice, they could also click the buttons several times; which could cause more serious issues._

    * _I understand this is probably out-of-scope for this assignment, but many stock applications also provide the ability to see real-time data and changes of the data. A graph usually highlight these changes very well. Incorporation of a graph would make it even more helpful to the user._

    _However, I think overall, your application idea and execution were done to a great quality. I also particularly enjoyed the demo. Great stuff!_

3. _The app is very well designed I like the layout of the data and the color scheme. However the only thing I have issue with is perhaps the buttons should be a little more stylized and smaller rather than taking up the whole screen._

    _A great application Idea with great execution!_

## Improvements based on feedback

The feedback received was largely related to the UI of the app. 

### 

## App limitations

### Live updates

The app currently does not update on intervals (e.g., every 5 seconds), however will refresh on activity restart or resume. For instance, if the portfolio or stocks activity are browsed, and then exited, the main activity will update it's data.

A solution to this drawback would be to run a thread that would call the update methods on a set interval, (e.g., 5 seconds) which would allow the data to be updated without relying on lifecycle event handling methods such as `onCreate` and `onResume`.

### Amount of stocks

Currently the app allows the user to trade only 100 of the most traded symbols on the IEX (out of the thousands that are listed on the exchange), limiting the user's ability to test out strategies on less traded symbols.

A workaround to this would be to 

### Choosing which stock order to sell

Currently 

## External references

- https://subscription.packtpub.com/book/application-development/9781788472142/7/ch07lvl1sec82/reading-all-lines-in-a-file-using-bufferedreader
- https://developer.android.com/topic/libraries/architecture/livedata
- https://stackoverflow.com/questions/44318859/fetching-a-url-in-android-kotlin-asynchronously
- https://developer.android.com/topic/libraries/architecture/viewmodel
- https://programmer.ink/think/a-new-way-to-create-a-viewmodel-creationextras.html
- https://medium.com/@lucasnrb/advanced-viewmodel-9cca1499addb
- https://stackoverflow.com/questions/61364874/view-models-for-recyclerview-items
- https://www.baeldung.com/guide-to-okhttp
- https://guides.codepath.com/android/storing-secret-keys-in-android