/**
 * 
 */

var AppComm = function() {
};

AppComm.prototype.scan = function(successCallback, failureCallback) {
	return PhoneGap.exec(successCallback, failureCallback, 'AppComm',
			'scan', []);
};

PhoneGap.addConstructor(function() {
	PhoneGap.addPlugin("appcomm", new AppComm());
});