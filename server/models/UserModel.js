//Require Mongoose
var mongoose = require('mongoose');
var bcrypt = require('bcrypt');

//Define a schema
var UserSchema = new mongoose.Schema({
    username:{ type: String, unique: true, required: true },
    hash_password: { type: String, required: true},
    email: { type: String , lowercase: true, trim: true},
    created_date: String,
    first_name: String,
    last_name: String,
    birth_date: String,
    gender: String,
    avatar: String
}, {
    collection: 'user'
});

// comparePassword
UserSchema.methods.comparePassword = function (password) {
    return bcrypt.compareSync(password, this.hash_password)
}

//Export function to create "UserSchema" models class
module.exports = mongoose.model('User', UserSchema );