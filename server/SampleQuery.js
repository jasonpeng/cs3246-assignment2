var sys        = require('sys')
var exec       = require('child_process').exec;
var canRun = true;
var counter = 1;

function onJavaReturn(error, stdout, stderr) {
	
	// console.log("./Data/ImageQuery/query"+counter+".jpg");
	// console.log(stdout);
	var result = JSON.parse(stdout);
	var hit = 0;
	for(var j = 0; j < result.length; j++){
		var imageName = result[j];
		var name = imageName.split(".")[0];
		if (name > 20*(counter-1) && name <= counter*20){
			hit++;
		}
	}
	var P = hit*1.0/result.length;
	var R = hit*1.0/20;
	console.log(counter+"\t"+hit+"\t"+R.toFixed(2)+"\t"+P.toFixed(2)+"\t"+((2*P*R)/(P+R)).toFixed(2));
	// console.log("query\t"+counter);
	// console.log("Hits: \t"+ hit);
	// console.log("Recall: \t"+ R);
	// console.log("Precision: \t"+P);
	// console.log("F1: \t"+(2*P*R)/(P+R));

	counter++;
	if(counter <= 20){
		exec("java -jar -Xmx4g ImageQuery.jar ./Data/ImageQuery/query"+counter+".jpg 20", onJavaReturn);	
	}
}


	console.log("Query\tHits\tRecall\tPrecision\tFi")
		exec("java -jar -Xmx4g ImageQuery.jar ./Data/ImageQuery/query"+counter+".jpg 20", onJavaReturn);	
