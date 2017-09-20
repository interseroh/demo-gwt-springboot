/*
 * JavaScript MyFunction.
 *
 * This file needs to be injected, so that the MyJavaScriptHello class
 * can be compiled before.
 */
function myFunction() {
    var myJavaScriptHello = new MyJavaScriptHello('Lofi');
    console.log(myJavaScriptHello.click());
}