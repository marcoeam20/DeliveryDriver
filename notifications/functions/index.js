'use strict'


const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/Notifications/{driver_id}/{notification_id}').onWrite(event =>

      const driver_id = event.params.driver_id;
      const Notifications = event.params.Notifications;

      console.log('The Driver id is: ', driver_id);

      if(!event.data.val()){

        return console.log('A Notification has been deleted from the database: ', notification_id);


      }

      const deviceToken = admin.database.ref(`/Orders/${driver_id}/deviceToken`).once('value');

      return deviceToken.then(result => {

        const payload = {
          Notifications: {
            title: "Incoming Order",
            body: "You have received a New Order",
            icon: "default"
          }

        };

        return admin.messaging().sendToDevice(,payload).then(response => {

          console.log('This was the Notification Feature');


      });



      });



 });
