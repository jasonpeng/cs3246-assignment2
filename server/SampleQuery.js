var sys        = require('sys')
var exec       = require('child_process').exec;
var canRun = true;
var counter = 1;

function onJavaReturn(error, stdout, stderr) {
  console.log(stderr);
  var result = JSON.parse(stdout);
  var hit = 0;
  for(var j = 0; j < result.length; j++){
    var imageName = result[j];
    var name = imageName.split(".")[0];
    name = parseInt(name, 10);
    if (name > 20*(counter-1) && name <= counter*20){
      hit++;
    }
  }
  var P = hit*1.0/result.length;
  var R = hit*1.0/20;
  var F1 = (2*P*R)/(P+R);
  console.log(counter+"\t"+hit+"\t"+R.toFixed(3)+"\t"+P.toFixed(3)+"\t\t"+F1.toFixed(3));

  counter ++;
  if (counter <= 20) {
    exec("java -Xmx4g -cp " + jarFile + ":lib/gson-2.2.4.jar cs3246/a2/web/WebServiceHandler " + imagePath + "query" + counter + ".jpg " + numResult, {cwd: cwd}, onJavaReturn);
  }
}

console.log("Running SampleQuery.js \n");
// print process.argv
process.argv.forEach(function(val, index, array) {
   console.log(index + ': ' + val);
});

var cwd = process.argv[2];
var jarFile = process.argv[3];
var imagePath = process.argv[4];
var numResult = process.argv[5];

console.log("Query\tHits\tRecall\tPrecision\tFi");
exec("java -Xmx4g -cp " + jarFile + ":lib/gson-2.2.4.jar cs3246/a2/web/WebServiceHandler " + imagePath + "query" + counter + ".jpg " + numResult, {cwd: cwd}, onJavaReturn);
