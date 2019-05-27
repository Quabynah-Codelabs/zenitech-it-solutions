const mongoose = require('mongoose');
const validator = require('validator');

// Create schema
var productSchema = new mongoose.Schema({
    key: {
        required: true,
        type: String,
        unique: true
    },
    name: {
        required: true,
        type: String,
        validate: (value) => {
            return !validator.isEmpty(value)
        }
    },
    desc: {
        required: true,
        type: String,
        validate: (value) => {
            return !validator.isEmpty(value)
        }
    },
    price: {
        required: true,
        type: String,
        validate: (value) => {
            return !validator.isEmpty(value);
        }
    },
    category: String,
    image: String,
    quantity: Number,
    synced: {
        required: false,
        default: false
    },
    isWishListItem: {
        required: false,
        default: false
    },
    url: {
        type: String,
        default: `https://zenitech-solutions.web.app/shop/`
    },
    ratings: {
        type: Number,
        default: 3.5,
        required: false
    },
    uploadTime: {
        required: false,
        type: Number,
        default: new Date().getTime()
    },
    updatedAt: {
        required: false,
        type: Number,
        default: new Date().getTime()
    }
});

// Export model
module.exports = mongoose.model('Product', productSchema);