# Pikmail Api

This is a pretty basic rest api written with [Kort](http://ktor.io/) that uses the [Pikmail]() library for mapping gmail addresses to google profile pictures.

## Usage

This api is hosted on heroku as `https://pikmail.herokuapp.com/` and it just exposes one endpoint.

#### Gmail endpoint

- ##### Url: `https://pikmail.herokuapp.com/{gmail}?size=100`
  Change `{gmail}` with the desired Gmail address.

- ##### Method: `GET`

- ##### Query parameters:
  Optional: `size=[Integer]` defines the size of the profile picture, default google size if omitted.

- ##### Success Response: 
  Status `302`, the request will be redirected to the google profile picture url with the specified size.
  
- ##### Error Response: 
  Status `404`, if the Gmail address is invalid or not found.

#### Examples

###### Curl

```bash
curl -X GET 'https://pikmail.herokuapp.com/eduardo.alejandro.pool.ake@gmail.com?size=100' --verbose
```

###### Html

```html
<img src="https://pikmail.herokuapp.com/eduardo.alejandro.pool.ake@gmail.com?size=100" alt="Smiley face">
```
<img src="https://pikmail.herokuapp.com/eduardo.alejandro.pool.ake@gmail.com?size=100" alt="Smiley face">

###### Android Picasso

```java
Picasso.with(context).load("https://pikmail.herokuapp.com/eduardo.alejandro.pool.ake@gmail.com?size=100").into(imageView);
```