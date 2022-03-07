//testing json validatiy witth better error feedback
const { readFile } = require('fs');
readFile('./Data.dat',(err,data) => {
    console.log(JSON.parse(data));
});