var functions = require('firebase-functions');
var admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.pushNotification = functions.database.ref('/notifications/{notificationId}').onWrite(event => {
    console.log('Push notification event triggered');

    var topic = "notifications";
    var snapShot = event.data;

    var payload = {
        data: {
            title: snapShot.child("position").val(),
            message: snapShot.child("status").val()
        }
    };

    return admin.messaging().sendToTopic(topic, payload);
});