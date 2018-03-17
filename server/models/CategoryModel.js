var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var CategorySchema = new Schema({
    id: {
        type: Number,
        required: true
    },
    name : {
        type : String,
        default: ""
    },
});

module.exports  = mongoose.model('Category', CategorySchema);