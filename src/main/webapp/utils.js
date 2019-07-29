// parsing server objects 

function extractPriceValue(priceObject) {	
	return  split('|')[1] ;
}
function extractPriceId(priceObject) {	
	return priceObject.split('|')[2];
	
}
function extractPatternStatusAsText(statusObject) {	
	return statusObject.split('|')[0];
}

function extractPatternStatusAsInteger(statusObject) {	
	return statusObject.split('|')[1];
}

