When you open a new bug ticket please provide us with the following information:

* Java version
* SDK version
* Play version
* the *important* JSON traffic, set "logger.sphere=TRACE" in application.conf for Play projects
     * this is important to understand if it is SPHERE.IO problem or of the SDK
* the steps to reproduce the problem for noobs
* what is the expected output and what is current output?
* a test case would be ultimate


## Things that help often

* call `sbt clean` or `play clean`
* restart SBT
* Refresh Product Search, see Merchant Center | Developers | Danger Zone
* use [SBT](http://www.scala-sbt.org) directly instead of the Play launcher, sometimes there is a version mismatch
* update to the latest SDK version
* take a break and drink water/tee/coffee/juice
* [regenerate the IDE files](https://www.playframework.com/documentation/2.2.x/IDE)
* take in mind that products 
    * need to be published
    * have current and staged representations
    * have multiple variants
    * may be out of stock and therefore something does not work (e.g., add to cart, discount)
