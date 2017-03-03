
<h1>AppNetworkDataReader</h1>
a network data reader lib can fully support all version of android.

[![](https://jitpack.io/v/SwiftyWang/AppNetworkDataReader.svg)](https://jitpack.io/#SwiftyWang/AppNetworkDataReader)

Add it in your root build.gradle at the end of repositories:
```java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```java
	dependencies {
	        compile 'com.github.SwiftyWang:AppNetworkDataReader:v0.01'
	}
```

```java
if (AppDataReader.checkPermission(this)) {
            dataReader = new AppDataReader(this);
            //do things
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            Toast.makeText(this, getText(com.swifty.datareader.R.string.please_grant_permission), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }

```