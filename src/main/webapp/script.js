var endpointHost = '';// no need to change it if resides within the war
var warName="sushi/data";
var time = [];
var prices = [];
var chartDataAsObject = [];
var patternData = [];
var patternStatus = 'waiting';
var exchange = ''; 
var currency = '';
var started = false;
var dataPoint = 1;
var htmlPattenStatusID="patternStatusEndpointStatus" ;
var htmlPriceStatusID="priceStatusEndpointStatus" ;
var ctx = document.getElementById('chart');
// chart object
var chart = new Chart(ctx, {
	type: 'line',

	data: {
		labels: time,
		datasets: [
			{
				data: prices,
				label: currency,
				borderColor: '#6A84DD',
				pointBackgroundColor: [],
				pointBorderColor: [],
				fill: false,
				responsive: true
			}
		]
	}
});

// when the user selects an exchange
function setExchange() {
	var e = document.getElementById('exchange');
 
	exchange = e.options[e.selectedIndex].text;
	
	if(exchange==='select') { exchange='';return ;}
	if (exchange === 'Binance') currency = 'NEOETH';
	if (exchange === 'Kraken') currency = 'XRPEUR';
	chart.data.datasets[0].label = currency;
	chart.update();
}

// when the user clicks start/stop
function start() {
	if(exchange=='') { alert("must select an exchange") ; return ;}
	var endpoint = '';
	if (started == false) {
		endpoint =
			endpointHost + '/'+warName+'/patterns/start?exchange=' + exchange + '&tradingpair=' + currency + '&noise=0';
		started = true;
		document.getElementById('actionButton').value = 'stop';
	} else {
		endpoint =endpointHost + '/'+warName+ '/patterns/stop';
		started = false;
		document.getElementById('actionButton').value = 'start';
		
	}
	axios
		.get(endpoint)
		.then(function(response) {
			console.log(response.data);
		})
		.catch(function(error) {
			// handle error
			console.log(error);
		})
		.finally(function() {
			// always executed
		});
}

 
// update periodically pattern status on the UI
window.setInterval(function() {
	document.getElementById(htmlPattenStatusID).innerHTML = '&nbsp;&nbsp;';
				document.getElementById(htmlPattenStatusID).style.backgroundColor = 'red';
	if (started) {
		axios
			.get(endpointHost + '/'+warName+ '/patterns/status')
			.then(function(response) {
				// update http call status
				 
		 
				
				// hand
				patternStatus = response.data;
				document.getElementById('status').innerHTML =   extractPatternStatusAsText(patternStatus);
				var e = document.getElementById('statuscolor');
				var color = '';
                patternStatus=parseInt(extractPatternStatusAsInteger(patternStatus));
		 
				if (patternStatus == 1) color = 'white';  // started
				if (patternStatus == 2) color = 'white'; //initialization
				if (patternStatus == 3) color = 'orange'; //processing
				if (patternStatus == 4) color = 'green'; // pattern Found
				if (patternStatus == 5) color = 'green';// watching pattern validity
				if (patternStatus == 6) color = 'grey';// pattern expired
				if (patternStatus == 7) color = 'grey'; // reloading new data
				 

				e.style.backgroundColor = color;
 				document.getElementById(htmlPattenStatusID).style.backgroundColor = 'green';
			})
			.catch(function(error) {
				// handle error
				console.log(error);
			document.getElementById(htmlPattenStatusID).style.backgroundColor = 'red';

			})
			.finally(function() {
				// always executed
			});
	}
}, 1000);

// update the chart with new price
window.setInterval(function() {
		document.getElementById(htmlPriceStatusID).innerHTML = '&nbsp;&nbsp;';
		document.getElementById(htmlPriceStatusID).style.backgroundColor = 'red';
	if (started) {
	
		axios
			.get(endpointHost + '/'+warName+ '/patterns/price')
			.then(function(response) {
				if (
					response.data.split(',')[0].split('|')[1] !== undefined &&
					response.data.split(',')[0].split('|')[1] > 0
				) {
					var price = response.data.split(',')[0].split('|')[1];

					chart.data.datasets[0].data.push(price);
					chartDataAsObject.push(response.data.split(',')[0]);
					chart.data.labels.push(dataPoint);
					dataPoint++;
					if (chart.data.datasets[0].data.length > 60) {
						chart.data.datasets[0].data.shift();
						chartDataAsObject.shift();
						chart.data.labels.shift();
					}
					document.getElementById(htmlPriceStatusID).style.backgroundColor = 'green';

					chart.update();
				} else {
					console.log(response.data + ' bad data' );
				document.getElementById(htmlPriceStatusID).style.backgroundColor = 'red';

				}
			})
			.catch(function(error) {
				// handle error
				console.log(error);
			document.getElementById(htmlPriceStatusID).style.backgroundColor = 'red';

			})
			.finally(function() {});
	}
}, 1000);


// update the chart when pattern is found
window.setInterval(function() {
	if (started) {
		axios
			.get(endpointHost + '/'+warName+ '/patterns/patterndata')
			.then(function(response) {
				//  chart.data.datasets[0].pointBorderColor[30] = "#cc000a";
				patternData = response.data.split(',');

				if (response.data.length > 0) {
					showPattern(patternData);
				}
			})
			.catch(function(error) {
				// handle error
				console.log(error);
			})
			.finally(function() {});
	}
}, 1000);
// helper for pattern data
function showPattern(patternData) {
	var i = 0;
	var p = patternData;
	while (i < p.length) {
		var j = 0;
		while (j < chartDataAsObject.length) {
			if (chartDataAsObject[j].split('|')[2] == p[i].split('|')[2]) {
				chart.data.datasets[0].pointBorderColor[j] = '#cc000a';
				console.log('found points to update ' + j);
			}
			j++;
		}
		i++;
	}
}
