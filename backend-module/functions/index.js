const functions = require('firebase-functions');
const admin = require('firebase-admin');
var jwt = require('jsonwebtoken');

// Initialize Firebase Admin
admin.initializeApp();

exports.testAPI = functions.https.onRequest((req, res) => {
    return res.send({
        message: 'Your API works fine'
    });
});

// Login Function
exports.login = functions.https.onRequest(async (req, res) => {
    var body = req.body;
    // { "email" : "quaynah@gmail.com", "password" : "quabynah" }

    if (body && req.method == 'POST') {
        var email = body.email;
        // var password = body.password;

        // Get auth key
        var authKey = await jwt.sign({ foo: 'bar' }, 'shhhhh');

        return res.status(201).send({
           key: authKey,
           name: '',
           email: email,
           avatar: '',
           type: 'guest',
           createdAt: new Date().getTime()
        });

    } else {
        return res.status(400).send({
            message: 'Bad login credentials'
        });
    }

});

// Login Function
exports.register = functions.https.onRequest(async (req, res) => {
    var body = req.body;
    // { "email" : "quaynah@gmail.com", "password" : "quabynah" }

    if (body && req.method == 'POST') {
        var email = body.email;
        // var password = body.password;

        // Get auth key
        var authKey = await jwt.sign({ foo: 'bar' }, 'shhhhh');

        return res.status(201).send({
           key: authKey,
           name: '',
           email: email,
           avatar: '',
           type: 'guest',
           createdAt: new Date().getTime()
        });

    } else {
        return res.status(400).send({
            message: 'Bad login credentials'
        });
    }

});