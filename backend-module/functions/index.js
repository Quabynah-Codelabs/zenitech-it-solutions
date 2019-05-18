const functions = require('firebase-functions');
const admin = require('firebase-admin');
var jwt = require('jsonwebtoken');
var rn = require('random-number');
var gen = rn.generator({
    min: 12.99,
    max: 1999.99,
    integer: false
});

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
        var authKey = await jwt.sign({
            foo: 'bar'
        }, 'shhhhh');

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
        var authKey = await jwt.sign({
            foo: 'bar'
        }, 'shhhhh');

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

exports.products = functions.https.onRequest(async (req, res) => {
    const products = await addFakeProducts();
    return res.status(200).send(products);
});

const addFakeProducts = () => {
    var products = [];
    for (let index = 0; index < 200; index++) {
        const product = {
            key: `${index}`,
            name: `Item ${index}`,
            price: gen(),
            desc: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            image: "",
            url: `https://codelabs.netlify.com/products/${index}`,
            uploadTime: new Date().getTime(),
            quantity: 100,
            category: "Other",
            synced: false,
            isWishListItem: false
        };
        products.push(product);
    }
    return products;
}