window.onload = function() {
	var pathname = window.location.pathname;
	var redirectUrl = "http://"+window.location.host + "/connect/twitter";
	if (pathname == "" || pathname == "/") {
		window.location = redirectUrl;
	} else {
		document.getElementsByTagName("BODY")[0].style.display = "block";
		document.getElementsByTagName("HTML")[0].style.background = "#027BC7";
	}
}