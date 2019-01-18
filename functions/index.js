const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.pushNotification = functions.database.ref('/notifications/{pushId}')
    .onCreate((snapShot, context) => {
        console.log('Push notification event triggered');

        const topic = "notifications";
        const data = snapShot.val();

        const payload = {
            notification: {
                title: data.position,
                body: data.status,
                sound: "default" 
            },
        };

        return admin.messaging().sendToTopic(topic, payload);
    });