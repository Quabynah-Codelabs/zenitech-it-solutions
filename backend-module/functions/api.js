const mongodb = require('mongodb');
const crypto = require('crypto');
const express = require('express');
const bodyParser = require('body-parser');
const morgan = require('morgan');
const cors = require('cors');
var jwt = require('jsonwebtoken');
var mongoose = require('mongoose');

var Customer = require('./models/customer');
var Cart = require('./models/cart');
var Product = require('./models/product');

// Get random values
const generateRandomString = (length) => {
    return crypto.randomBytes(Math.ceil(length / 2))
        .toString('hex')
        .slice(0, length)
}

// Create a SHA-512
const sha512 = (password, salt) => {
    var hash = crypto.createHmac('sha512', salt)
    hash.update(password)
    var value = hash.digest('hex')
    return {
        salt: salt,
        passwordHash: value
    }
}

// Add salt to password
const saltHashPassword = (password) => {
    const salt = generateRandomString(16)
    var passwordData = sha512(password, salt)
    return passwordData
}

// Check hashed password
const checkHashPassword = (password, salt) => {
    return sha512(password, salt)
}

// Get Express application
const app = express();

// Automatically allow cross-origin requests
app.use(cors({
    origin: true
}));

// Get mongo object id
const ObjectID = mongodb.ObjectID;

// Configure body parser
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));

// Apply Morgan
app.use(morgan("combined"));

// Setup mongoose
mongoose.Promise = global.Promise;


var testUrl = 'mongodb://localhost:27017';
var url = 'mongodb+srv://quabynah:bilghazyllc2018@clustercompanion-nb97l.mongodb.net/test?retryWrites=true';
var MongoClient = mongodb.MongoClient;
MongoClient.connect(url, {
    useNewUrlParser: true
}, (err, client) => {

    if (err) {
        return console.log(err.message);
    } else {
        console.log('Connected to MongoDB successfully');

        var customers = client.db('zeniteck').collection('customers');
        var products = client.db('zeniteck').collection('products');
        var cart = client.db('zeniteck').collection('cart');

        // Registration
        app.post('/register', async (req, res, next) => {
            var body = req.body;

            var email = body.email;
            var password = body.password;

            // Query user information
            customers.findOne({
                email: email
            }).then(async user => {
                if (user) {
                    return res.status(401).send({
                        message: 'User account already exists'
                    })
                } else {
                    var hashedPassword = saltHashPassword(password);

                    // Get auth key
                    var key = await jwt.sign({
                        foo: 'bar'
                    }, 'shhhhh');

                    // Create a new user
                    customers.insertOne(new Customer({
                        key,
                        email,
                        password: hashedPassword.passwordHash,
                        salt: hashedPassword.salt,
                        name: 'New user',
                        avatar: '',
                        type: 'guest',
                        createdAt: new Date().getTime()
                    })).then((response) => {
                        if (response.result) {
                            return res.status(200).send(response.result);
                        } else {
                            return res.status(401).send({
                                message: 'Unable to create this account'
                            })
                        }
                    }).catch(err => {
                        return res.status(200).send({
                            message: 'An error occurred while creating this user'
                        });
                    });
                }
            }).catch(err => {
                return res.status(404).send({
                    err
                })
            });
        });

        // OAuth
        app.post('/oauth', async (req, res, next) => {
            var body = req.body;

            if (body) {
                var email = body.email;
                var username = body.username;
                var accountId = body.accountId
                var avatar = body.avatar

                // Query user information
                customers.findOne({
                    email: email
                }).then(async user => {
                    if (user) {
                        return res.status(200).send(user);
                    } else {

                        // Get auth key
                        var key = await jwt.sign({
                            foo: 'bar'
                        }, 'shhhhh');

                        // Create a new user
                        customers.insertOne(new Customer({
                            key,
                            email,
                            password: accountId,
                            salt: "",
                            name: username,
                            avatar: avatar,
                            type: 'guest',
                            createdAt: new Date().getTime()
                        })).then((response) => {
                            if (response.result) {
                                return res.status(200).send(response.result);
                            } else {
                                return res.status(401).send({
                                    message: 'Unable to create this account'
                                })
                            }
                        }).catch(err => {
                            return res.status(200).send({
                                message: 'An error occurred while creating this user'
                            });
                        });
                    }
                }).catch(err => {
                    return res.status(404).send({
                        err
                    })
                });
            } else {
                return res.status(400).send({
                    message: 'Bad login credentials'
                });
            }
        });

        // Login
        app.post('/login', async (req, res, next) => {
            var body = req.body;

            if (body) {
                var email = body.email;
                var password = body.password;

                // Query user information
                customers.findOne({
                    email: email
                }).then(user => {
                    if (user) {
                        var hashedPassword = checkHashPassword(password, user.salt);
                        if (hashedPassword.passwordHash == user.password) {
                            return res.status(200).send(user);
                        } else {
                            return res.status(400).send({
                                message: 'Wrong password'
                            });
                        }
                    } else {
                        return res.status(200).send({
                            message: 'User could not be found'
                        });
                    }
                }).catch(err => {
                    return res.status(404).send({
                        err
                    })
                });

            } else {
                return res.status(400).send({
                    message: 'Bad login credentials'
                });
            }
        });

        // Customer information
        app.post('/me', (req, res, next) => {
            if (req.body) {
                // Get the user ID
                var key = req.body.key;

                // Get the user
                customers.findOne({
                    key
                }).then(user => {
                    if (user) {
                        return res.status(200).send(user);
                    } else {
                        return res.status(401).send({
                            message: 'Cannot get this user.'
                        })
                    }
                })

            } else {
                return res.status(404).send({
                    message: 'Your request is invalid. Please attacha request body!'
                });
            }


        });

        // Customer update
        app.post('/customers/:id', (req, res, next) => {
            var userId = req.params.id;
            var body = req.body;

            if (body) {
                customers.findOneAndUpdate({
                    key: userId
                }, {
                    $set: {
                        token: body.token,
                        key: body.key,
                        email: body.email,
                        name: body.name,
                        avatar: body.avatar,
                        type: body.type,
                        updatedAt: new Date().getTime()
                    }
                }).then(response => {
                    if (response) {
                        return res.status(200).send({
                            message: 'User updated successfully'
                        })
                    } else {
                        return res.status(404).send({
                            message: `User was not found.${err}`
                        });
                    }
                }).catch(err => {
                    return res.status(500).send({
                        message: `Server is unavailable now.${err}`
                    })
                });
            } else {
                return res.status(500).send({
                    message: `Please upload a new token for this customer`
                })
            }

        });

        // Products
        app.post('/products', (req, res) => {
            products.find({}).limit(1000).toArray().then(docs => {
                return res.status(200).send(docs);
            }).catch(err => {
                return res.status(400).send({
                    message: `Unable to load products.${err}`
                });
            });
        });

        // Single Product
        app.post('/products/:id', (req, res) => {
            products.findOne({
                key: req.params.id
            }).then(product => {
                if (product) {
                    return res.status(200).send(product);
                } else {
                    return res.status(401).send({
                        message: 'Cannot find product'
                    })
                }
            }).catch(err => {
                return res.status(404).send({
                    message: `Error occurred while getting this product. ${err}`
                })
            });
        });

        // Shopping cart for user
        app.post('/cart/:id', (req, res) => {
            // User ID
            var key = req.params.id;

            // Find shopping cart content
            cart.find({
                user: key
            }).limit(1000).toArray().then(docs => {
                return res.status(200).send(docs);
            }).catch(err => {
                return res.status(400).send({
                    message: `Unable to load products.${err}`
                });
            });
        });

        // Get cart item
        app.post('/cart/:user/:id', (req, res) => {
            cart.findOne({
                user: req.params.user,
                key: req.params.id
            }).then(result => {
                if (result) {
                    return res.status(200).send(result);
                } else {
                    return res.status(404).send({
                        message: "Cannot find user's shopping cart details"
                    })
                }
            }).catch(err => {
                return res.status(404).send({
                    message: `Error occurred while getting this product. ${err}`
                })
            });
        });

        // Add to cart
        app.post('/cart', async (req, res) => {
            var body = req.body;

            if (body && body.user && body.product) {
                // Get key
                var key = await jwt.sign({
                    foo: 'bar'
                }, 'shhhhh');

                cart.insertOne(new Cart({
                    key,
                    product: body.product,
                    user: body.user
                })).then(result => {
                    return res.status(200).send({
                        message: 'Item added to cart successfully'
                    })
                }).catch(err => {
                    return res.status(404).send({
                        message: `An Error occurred. ${err}`
                    });
                });
            } else {
                return res.status(401).send({
                    message: `Invalid request. ${err}`
                });
            }
        });

        // Delete from cart
        app.delete('/cart/:id', (req, res) => {
            var key = req.params.id;

            cart.findOneAndDelete({
                key
            }).then(result => {
                return res.status(200).send({
                    message: 'Item deleted successfully'
                })
            }).catch(err => {
                return res.status(404).send({
                    message: err
                })
            });
        });

        // Add product
        app.post('/products/add', async (req, res) => {
            var body = req.body;

            if (body) {
                // Get key
                var key = await jwt.sign({
                    foo: 'bar'
                }, 'shhhhh');

                products.insertOne(new Product({
                    key,
                    image: body.image,
                    name: body.name,
                    price: body.price,
                    desc: body.description,
                    category: body.category
                })).then(result => {
                    return res.status(200).send({
                        message: 'Product added successfully'
                    })
                }).catch(err => {
                    return res.status(401).send({
                        message: `Invalid request. ${err}`
                    });
                });
            } else {
                return res.status(401).send({
                    message: `Invalid request. ${err}`
                });
            }
        });

        // Update product
        app.put('/products/:id', (req, res) => {
            var key = req.params.id;
            var body = req.body;

            if (body) {
                products.findOneAndUpdate({
                    key
                }, {
                    $set: {
                        image: body.image,
                        desc: body.desc,
                        name: body.name,
                        category: body.category,
                        url: body.url,
                        isWishListItem: body.isWishListItem,
                        synced: body.synced,
                        quantity: body.quantity,
                        price: body.price,
                        ratings: body.ratings,
                        updatedAt: new Date().getTime()
                    }
                }).then(response => {
                    if (response) {
                        return res.status(200).send({
                            message: 'User updated successfully'
                        })
                    } else {
                        return res.status(404).send({
                            message: `User was not found.${err}`
                        });
                    }
                }).catch(err => {
                    return res.status(500).send({
                        message: `Server is unavailable now.${err}`
                    })
                });
            } else {
                return res.status(500).send({
                    message: `Invalid request.${err}`
                });
            }
        });

        // Remove product
        app.delete('/products/:id', (req, res) => {
            var key = req.params.id;

            products.findOneAndDelete({
                key
            }).then(result => {
                return res.status(200).send({
                    message: 'Item deleted successfully'
                })
            }).catch(err => {
                return res.status(404).send({
                    message: err
                })
            });
        })

        app.listen(3000, () => console.log('Connected on port 3000'));
    }
});

// exports.products = functions.https.onRequest(async (req, res) => {
//     var path = req.path;
//     if (path !== undefined && path == '/') {
//         const products = await addFakeProducts();
//         return res.status(200).send(products);
//     } else {
//         var index = path.substr(1);
//         return res.status(200).send({
//             key: `${index}`,
//             name: `Item ${index}`,
//             price: gen(),
//             desc: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
//             image: `${index % 2 == 0 ? "http://static.techspot.com/images/products/2019/laptops/org/2019-03-22-product-6.jpg": "https://www.androidpolice.com/wp-content/uploads/2018/12/Samsung-Galaxy-S10-Plus-5K_2.png"}`,
//             url: `https://us-central1-zenitech-solutions.cloudfunctions.net/products/${index}`,
//             uploadTime: new Date().getTime(),
//             quantity: 100,
//             category: "Other",
//             synced: false,
//             isWishListItem: false
//         });
//     }

// });

// const addFakeProducts = () => {
//     var products = [];
//     for (let index = 0; index < 200; index++) {
//         const product = {
//             key: `${index}`,
//             name: `Item ${index}`,
//             price: gen(),
//             desc: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
//             image: `${index % 2 == 0 ? "http://static.techspot.com/images/products/2019/laptops/org/2019-03-22-product-6.jpg": "https://www.androidpolice.com/wp-content/uploads/2018/12/Samsung-Galaxy-S10-Plus-5K_2.png"}`,
//             url: `https://us-central1-zenitech-solutions.cloudfunctions.net/products/${index}`,
//             uploadTime: new Date().getTime(),
//             quantity: 100,
//             category: "Other",
//             synced: false,
//             isWishListItem: false
//         };
//         products.push(product);
//     }
//     return products;
// };


module.exports = app;