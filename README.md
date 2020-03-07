# TagCloud
<strong>TagCloud</strong> is an Android library that provides the feature of displaying a list of tags, including the adding and removing operations of tags.

## Features
* "is_indicator" attribute
* Customizable tag colors
* Listenable tag strings confliction event (already has the same tag in a certain tag-cloud)
* Listenable clicking event on tags
* Listenable removing event on tags

## Jitpack
1. Add the maven into your project gradle file.
<pre><code>allprojects {
  repositories {
    google()
    jcenter()
    maven { url 'https://jitpack.io' }
  }
}
</code></pre>

2. Add the dependency into your app gradle file.
<pre><code>dependencies {
  ...
  implementation 'com.github.rekkursion:TagView:1.0.5'
}
</code></pre>

## Usage
See <a href="https://github.com/Rekkursion/TagView/blob/master/app/src/main/java/com/rekkursion/tagviewsample/MainActivity.kt">this example</a>.
