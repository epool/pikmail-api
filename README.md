# [Pikmail Api](https://pikmail.herokuapp.com)

# DEPRECATED

**UPDATE:**
Since this API was fully based on the [Picasa API](https://developers.google.com/picasa-web/) and that API has been depreated, Therefore this API is being deprecated too unless we found a workaround to this.

```
This API is being deprecated and will be turned down in January 2019. Migrate to Google Photos Library API as soon as possible to avoid disruptions to your application.
```

Info [Here](https://developers.google.com/picasa-web/)

================================================================================


This is a pretty basic rest api written with [Ktor](http://ktor.io/) that uses the [Pikmail](https://github.com/epool/pikmail) library for mapping gmail addresses to google profile pictures.

You can read [this blog post](https://nearsoft.com/blog/pikmail-emails-to-pictures-using-kotlin/) to know how Pikmail Api works intenally.

## Usage

This api is hosted on heroku as `https://pikmail.herokuapp.com/` and it just exposes one endpoint.

#### Gmail endpoint

- ##### Url: `https://pikmail.herokuapp.com/{gmail}?size=100`
  Change `{gmail}` with the desired Gmail address.

- ##### Method: `GET`

- ##### URL parameters:
  Parameter | Description
  --------- | -----------
  gmail | The Gmail address of the profile to retrieve


- ##### Query parameters:
  Parameter | Required | Default | Description
  --------- | ------- | ------- |-----------
  size | NO |  | If set, it defines the size of the profile picture, default google's size if omitted.

- ##### Success Response: 
  Status `302`, the request will be redirected to the google profile picture url with the specified size.
  
- ##### Error Response: 
  The Pikmail API uses the following error codes:
  
  Error Code | Meaning
  ---------- | -------
  302 | Found -- The request will be redirected to the google profile picture url with the specified size if set.
  404 | Not Found -- The specified Gmail account could not be found.
  500 | Internal Server Error -- We had a problem with our server. Try again later.
  503 | Service Unavailable -- We're temporarily offline for maintenance. Please try again later.

#### Examples

###### Curl

```bash
curl -X GET 'https://pikmail.herokuapp.com/eduardo.alejandro.pool.ake@gmail.com?size=100' --verbose
```

###### Html

```html
<img src="https://pikmail.herokuapp.com/eduardo.alejandro.pool.ake@gmail.com?size=100" alt="Profile Picture">
```
<img src="https://pikmail.herokuapp.com/eduardo.alejandro.pool.ake@gmail.com?size=100" alt="Profile Picture">

###### Android Picasso

```java
Picasso.with(context).load("https://pikmail.herokuapp.com/eduardo.alejandro.pool.ake@gmail.com?size=100").into(imageView);
```
